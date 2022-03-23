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

    private MailRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<MailEntity>> observableMails;
    private final MediatorLiveData<List<MailEntity>> observableOwnMails;

    public MailListViewModel(@NonNull Application application,final int IdPostWorker, MailRepository mailRepository, PostworkerRepository postworkerRepository) {
        super(application);

        repository = mailRepository;

        this.application = application;

        observableMails = new MediatorLiveData<>();
        observableOwnMails = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        observableMails.setValue(null);
        observableOwnMails.setValue(null);

        LiveData<List<MailEntity>> mails = repository.getAllMails(application);
        LiveData<List<MailEntity>> ownMails = repository.getAllByPostworker(IdPostWorker,application);

        // observe the changes of the entities from the database and forward them
        observableMails.addSource(mails, observableMails::setValue);
        observableOwnMails.addSource(ownMails, observableOwnMails::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final int idPostworker;
        private final MailRepository mailRepository;
        private final PostworkerRepository postworkerRepository;


        public Factory(@NonNull Application application,int id) {
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
    public LiveData<List<MailEntity>> getAllMails() {
        return observableMails;
    }

    public LiveData<List<MailEntity>> getOwnMails() {
        return observableOwnMails;
    }

    public void deleteMail(MailEntity mail, OnAsyncEventListener callback) {
        repository.delete(mail, callback, application);
    }

}
