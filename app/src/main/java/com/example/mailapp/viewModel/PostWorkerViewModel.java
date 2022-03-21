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
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.OnAsyncEventListener;

public class PostWorkerViewModel extends AndroidViewModel {


    private PostworkerRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<PostWorkerEntity> observablePostworker;

    public PostWorkerViewModel(@NonNull Application application,
                               final String email, PostworkerRepository repository) {
        super(application);

        this.repository = repository;

        this.application = application;

        observablePostworker = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observablePostworker.setValue(null);

        LiveData<PostWorkerEntity> postworker = this.repository.getPostworkerByEmail(email, application);

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
            repository = ((BaseApplication)application).getPostworkerRepository();
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

    public void createPostWorker(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.insert(postWorker, callback, application);
    }

    public void updatePostWorker(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.update(postWorker, callback, application);
    }

    public void deletePostWorker(PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        repository.delete(postWorker, callback, application);
    }
}
