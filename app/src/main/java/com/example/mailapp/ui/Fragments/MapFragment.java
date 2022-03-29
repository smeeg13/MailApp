package com.example.mailapp.ui.Fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.viewModel.MailListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment {

    private FloatingActionButton myPositionButton;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private String workerConnectedEmailStr;
    private List<MailEntity> mailsInProgress;
    private ArrayList<LatLng> markersLatLng = new ArrayList<>();
    private MailListViewModel viewModel;
    List<String> addressesStrings;
    private static final int REQUEST_CODE = 101;
    private static final String ADDRESS = "Route de Roumaz";
    private static final String ZIP = "1965";
    private static final String CITY = "Savièse";
    private int markerClicked =0;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("### MAP OPEN");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        myPositionButton = v.findViewById(R.id.myPositionButton);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        workerConnectedEmailStr = settings.getString(BaseActivity.PREFS_USER, null);
        String workerConnectedIdStr = settings.getString(BaseActivity.PREFS_ID_USER, null);
        int workerConnectedIdInt = Integer.parseInt(workerConnectedIdStr);

        //Get back own mails NOT DONE
        MailListViewModel.Factory factory = new MailListViewModel.Factory(
                getActivity().getApplication(), workerConnectedIdInt);
        viewModel = ViewModelProviders.of(this, factory).get(MailListViewModel.class);
        viewModel.getOwnMailsInProgress().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsInProgress = mailEntities;

                //Todo Place marker for every mails in prog
                addressesStrings = new ArrayList<>();
                for (MailEntity mail : mailsInProgress){
                    String coordinate = getCoordinates(mail.address+" "+mail.zip+" "+mail.city)+"-"+mail.idMail;
                    addressesStrings.add(coordinate);
                }
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        String latitude =null;
                        String longitude = null;
                        String idmail = null;
                        for (String coordinate : addressesStrings){
                            if (coordinate.contains("-")) {
                                // Split it.
                                String[] parts = coordinate.split("-");
                                 latitude = parts[0];
                                 longitude = parts[1];
                                 idmail = parts[2];
                            }
                            if(idmail!=null){
                                LatLng latLng = new LatLng(Double.parseDouble(latitude),
                                        Double.parseDouble(longitude));

                                MarkerOptions options = new MarkerOptions().position(latLng)
                                        .title("ID Mail : "+idmail);

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                googleMap.addMarker(options);
                            }
                        }


                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {


                                if(markerClicked ==0) {
                                    markerClicked++;
                                }else {
                                    if (markerClicked == 1){
                                        //TODO display mail choosed detail
                                        System.out.println("Marker Clicked Title: "+marker.getTitle());
                                        String idstr = marker.getTitle();
                                        String str = null;
                                        String id = null;
                                        if (idstr.contains(" : ")) {
                                            // Split it.
                                            String[] parts = idstr.split(" : ");
                                            str = parts[0];
                                            id = parts[1];
                                        }
                                        if(id!=null){
                                            Bundle datas = new Bundle();
                                            datas.putInt("MailID", Integer.parseInt(id));
                                            datas.putBoolean("Enable", false);
                                            MailDetailFragment newfragment = new MailDetailFragment();
                                            newfragment.setArguments(datas);
                                            getFragmentManager().beginTransaction()
                                                    .replace(R.id.HomeFrameLayout, newfragment)
                                                    .commit();
                                        }
                                        markerClicked =0;
                                    }

                                }



                                return false;
                            }
                        });

                    }
                });
            }
        });

        myPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

        return v;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                supportMapFragment.getMapAsync(googleMap -> {
                    LatLng latLng = new LatLng(location.getLatitude(),
                            location.getLongitude());

                    MarkerOptions options = new MarkerOptions().position(latLng)
                            .title("I'm here !");

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    googleMap.addMarker(options);
                });
            }
        });
    }
    private String getCoordinates(String address){
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = null;
        String latLongStg = "";
            try {
                addresses = (geocoder.getFromLocationName(address, 1));
                if (addresses != null){
                    double latit = addresses.get(0).getLatitude();
                    double longit = addresses.get(0).getLongitude();
                    latLongStg = latit+"-"+longit;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return latLongStg;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // getCurrentLocation();
            }
        }
    }
}