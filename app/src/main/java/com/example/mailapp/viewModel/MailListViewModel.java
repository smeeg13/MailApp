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
import com.example.mailapp.database.repository.MailRepository;

import java.util.List;

public class MailListViewModel  extends AndroidViewModel {

    private MailRepository repository;

    private Context applicationContext;
    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<MailEntity>> observableMails;

    public MailListViewModel(@NonNull Application application, MailRepository clientRepository) {
        super(application);

        repository = clientRepository;

        applicationContext = application.getApplicationContext();

        observableMails = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableMails.setValue(null);

        LiveData<List<MailEntity>> mails = repository.getAllMails(applicationContext);

        // observe the changes of the entities from the database and forward them
        observableMails.addSource(mails, observableMails::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final MailRepository mailRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            mailRepository = MailRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MailListViewModel(application, mailRepository);
        }
    }

    /**
     * Expose the LiveData MailEntities query so the UI can observe it.
     */
    public LiveData<List<MailEntity>> getClients() {
        return observableMails;
    }
}
