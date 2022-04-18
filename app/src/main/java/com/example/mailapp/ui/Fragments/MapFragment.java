package com.example.mailapp.ui.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.viewModel.MailListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {

    private static final String TAG = "MapFragment";
    private FloatingActionButton myPositionButton;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private String workerConnectedIdStr;
    private List<MailEntity> mailsInProgress;
    private ArrayList<LatLng> markersLatLng = new ArrayList<>();
    private MailListViewModel viewModel;
    List<String> addressesStrings;
    private int markerClicked = 0;
    private List<Address> addresses;
    private FusedLocationProviderClient fusedLocationClient;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("### MAP OPEN");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myPositionButton = v.findViewById(R.id.myPositionButton);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        workerConnectedIdStr = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Get back own mails NOT DONE
        MailListViewModel.Factory factory = new MailListViewModel.Factory(
                getActivity().getApplication(), workerConnectedIdStr);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MailListViewModel.class);
        viewModel.getOwnMails().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsInProgress = mailEntities;
                mailsInProgress.removeIf(mail -> !mail.getStatus().equals("In Progress"));

                //Take back all mail's Addresses
                addressesStrings = new ArrayList<>();
                for (MailEntity mail : mailsInProgress) {
                    addressesStrings.add(mail.getAddress() + " " + mail.getZip() + " " + mail.getCity() + "_" + mail.getIdMail());
                }

                supportMapFragment.getMapAsync(googleMap -> {
                    String addressStr = null;
                    String idmail = null;
                    //For each mail take back the address and add a marker
                    for (String address : addressesStrings) {
                        if (address.contains("_")) {
                            // Split it.
                            String[] parts = address.split("_");
                            addressStr = parts[0];
                            idmail = parts[1];
                        }
                        if (idmail != null) {
                            LatLng latLng = getLocationFromAddress(getContext(), addressStr);

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("ID Mail : " + idmail);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.addMarker(options);
                        }
                    }

                    googleMap.setOnMarkerClickListener(marker -> {
                        String id = null;
                        if (markerClicked == 0) {
                            markerClicked++;
                        } else {
                            if (markerClicked == 1) {
                                System.out.println("Marker Clicked Title : " + marker.getTitle());
                                String idstr = marker.getTitle();

                                if (idstr.contains(" : ")) {
                                    // Split it.
                                    String[] parts = idstr.split(" : ");
                                    id = parts[1];
                                }
                                if (id != null) {
                                    Bundle datas = new Bundle();
                                    datas.putString("MailID", id);
                                    datas.putBoolean("Enable", false);
                                    MailDetailFragment newfragment = new MailDetailFragment();
                                    newfragment.setArguments(datas);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.HomeFrameLayout, newfragment)
                                            .commit();
                                }
                                markerClicked = 0;
                            }
                        }
                        return false;
                    });

                });
            }
        });

        myPositionButton.setOnClickListener(view -> getMyLocation());
        return v;
    }



    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            supportMapFragment.getMapAsync(googleMap -> {
                                LatLng latLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());

                                MarkerOptions options = new MarkerOptions().position(latLng)
                                        .title("I'm here !");

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                googleMap.addMarker(options);
                            });
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            } else {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }


    static String getJsonFromAssets(Activity activity, String fileName) {
        String jsonString;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static boolean readCitiesAuthorizedFromJSON(String cityEntered, Activity activity) {
        boolean isValid = false;
        try {
            JSONArray m_jArry = new JSONArray(getJsonFromAssets(activity, "Cities_in_CH.json"));

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String city_name = jo_inside.getString("city");
                Log.i("Detail Json :", city_name);
                //Add your values in your `ArrayList` as below:

                city_name = city_name.toLowerCase(Locale.ROOT);
                cityEntered = cityEntered.toLowerCase(Locale.ROOT);
                if (city_name.equals(cityEntered)) {
                    isValid = true;
                    System.out.println("The city entered : " + cityEntered + " correspond to : " + city_name);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(TAG + " :  " + e);
        }
        return isValid;
    }
}