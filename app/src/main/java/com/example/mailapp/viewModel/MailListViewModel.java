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
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.OnAsyncEventListener;

import java.util.List;

public class MailListViewModel  extends AndroidViewModel {

    private static final String TAG = "MailListViewModel";


    private MailRepository repository;


    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<MailEntity>> observableOwnMails;
    private final MediatorLiveData<List<MailEntity>> observableOwnMailsInProg;


    public MailListViewModel(@NonNull Application application,final String IdPostWorker, MailRepository mailRepository, PostworkerRepository postworkerRepository) {
        super(application);

        repository = mailRepository;


        observableOwnMails = new MediatorLiveData<>();
        observableOwnMailsInProg = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        observableOwnMails.setValue(null);
        observableOwnMailsInProg.setValue(null);

        LiveData<List<MailEntity>> ownMails = repository.getAllByPostworker(IdPostWorker);

         LiveData<List<MailEntity>> ownMailsInProg = repository.getAllByPostworker(IdPostWorker);
        // LiveData<List<MailEntity>> ownMailsInProg = repository.getInProgressByPostworker(IdPostWorker,"In Progress");

        // observe the changes of the entities from the database and forward them
        observableOwnMails.addSource(ownMails, observableOwnMails::setValue);
        observableOwnMailsInProg.addSource(ownMailsInProg, observableOwnMailsInProg::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String idPostworker;
        private final MailRepository mailRepository;
        private final PostworkerRepository postworkerRepository;


        public Factory(@NonNull Application application,String id) {
            this.application = application;
            this.idPostworker = id;
            mailRepository =  ((BaseApplication) application).getMailRepository();
            postworkerRepository = ((BaseApplication) application).getPostworkerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MailListViewModel(application, idPostworker, mailRepository,postworkerRepository);
        }
    }

    /**
     * Expose the LiveData MailEntities query so the UI can observe it.
     */

    public LiveData<List<MailEntity>> getOwnMails() {
        return observableOwnMails;
    }
    public LiveData<List<MailEntity>> getOwnMailsInProgress() {
        return observableOwnMailsInProg;
    }

    public void deleteMail(MailEntity mail, OnAsyncEventListener callback) {
        ((BaseApplication) getApplication()).getMailRepository()
                .delete(mail, callback);    }

    public void updateMail(MailEntity mail, OnAsyncEventListener callback) {
        ((BaseApplication) getApplication()).getMailRepository().update(mail, callback);
    }

}
