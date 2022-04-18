package com.example.mailapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.util.OnAsyncEventListener;

public class MailViewModel  extends AndroidViewModel {

    private MailRepository repository;
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<MailEntity> observableMail;

    public MailViewModel(@NonNull Application application,
                               final String idMail, MailRepository repo) {
        super(application);

        repository = repo;
        observableMail = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableMail.setValue(null);

        if (idMail != null){
            LiveData<MailEntity> mail = this.repository.getMailById(idMail);
            // observe the changes of the client entity from the database and forward them
            observableMail.addSource(mail, observableMail::setValue);
        }
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String MMailID;

        private final MailRepository repository;

        public Factory(@NonNull Application application, String mailID) {
            mApplication = application;
            MMailID = mailID;
            repository = ((BaseApplication) application).getMailRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MailViewModel(mApplication, MMailID, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<MailEntity> getMail() {
        return observableMail;
    }

    public void createMail(MailEntity mail, OnAsyncEventListener callback) {
        ((BaseApplication) getApplication()).getMailRepository()
                .insert(mail, callback);    }

    public void updateMail(MailEntity mail, OnAsyncEventListener callback) {
        ((BaseApplication) getApplication()).getMailRepository()
                .update(mail, callback);    }

    public void deleteMail(MailEntity mail, OnAsyncEventListener callback) {
        ((BaseApplication) getApplication()).getMailRepository()
                .delete(mail, callback);
    }
}