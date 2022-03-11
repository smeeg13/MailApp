package com.example.mailapp.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.OnAsyncEventListener;

import java.util.List;

public class MailListViewModel  extends AndroidViewModel {

    private MailRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<MailEntity>> observableMails;
    private final MediatorLiveData<List<MailEntity>> observablePostworkers;

    public MailListViewModel(@NonNull Application application,final int IDPostWorker, MailRepository mailRepository, PostworkerRepository postworkerRepository) {
        super(application);

        repository = mailRepository;

        this.application = application;

        observableMails = new MediatorLiveData<>();
        observablePostworkers = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        observableMails.setValue(null);
        observablePostworkers.setValue(null);

        LiveData<List<MailEntity>> mails = repository.getAllMails(application);
        LiveData<List<MailEntity>> ownMails = repository.getAllByPostworker(IDPostWorker,application);

        // observe the changes of the entities from the database and forward them
        observableMails.addSource(mails, observableMails::setValue);
      //  observablePostworkers.addSource(ownMails, observablePostworkers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final int IdPostworker;
        private final MailRepository mailRepository;
        private final PostworkerRepository postworkerRepository;


        public Factory(@NonNull Application application,int idPostworker) {
            this.application = application;
            this.IdPostworker = idPostworker;
            mailRepository =  ((BaseApplication) application).getMailRepository();
            postworkerRepository = ((BaseApplication) application).getPostworkerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MailListViewModel(application,IdPostworker, mailRepository,postworkerRepository);
        }
    }

    /**
     * Expose the LiveData MailEntities query so the UI can observe it.
     */
    public LiveData<List<MailEntity>> getClients() {
        return observableMails;
    }
    public LiveData<List<MailEntity>> getOwnMails() {
        return observablePostworkers;
    }

    public void deleteAccount(MailEntity account, OnAsyncEventListener callback) {
        repository.delete(account, callback, application);
    }

}
