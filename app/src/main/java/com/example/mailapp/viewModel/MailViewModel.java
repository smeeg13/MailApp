package com.example.mailapp.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;

public class MailViewModel  extends AndroidViewModel {


    private MailRepository repository;

    private Context applicationContext;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<MailEntity> observablePostworker;

    public MailViewModel(@NonNull Application application,
                               final String email, MailRepository repository) {
        super(application);

        this.repository = repository;

        applicationContext = application.getApplicationContext();

        observablePostworker = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observablePostworker.setValue(null);

        LiveData<PostWorkerEntity> postworker = this.repository.getClientByEmail(email, applicationContext);

        // observe the changes of the client entity from the database and forward them
        observablePostworker.addSource(postworker, observablePostworker::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String email;

        private final PostworkerRepository repository;

        public Factory(@NonNull Application application, String postworkeremail) {
            this.application = application;
            this.email = postworkeremail;
            repository = PostworkerRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PostWorkerViewModel(application, email, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<PostWorkerEntity> getClient() {
        return observablePostworker;
    }

    public void createClient(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.insert(postWorker, callback, applicationContext);
    }

    public void updateClient(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.update(postWorker, callback, applicationContext);
    }

    public void deleteClient(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.delete(postWorker, callback, applicationContext);
    }
}