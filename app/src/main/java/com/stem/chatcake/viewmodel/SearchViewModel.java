package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stem.chatcake.model.Room;
import com.stem.chatcake.model.User;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;

import java.util.List;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class SearchViewModel extends Observable.OnPropertyChangedCallback {

    // dependencies
    private Context context;
    private HttpService httpService;
    private LocalStorageService localStorageService;
    private StateService stateService;
    private ConnectionService connectionService;
    private Room room;

    private ListView resultsListView;
    private ArrayAdapter<User> resultsAdapter;

    private Call<List<User>> call;

    // state
    public final ObservableField<String> query = new ObservableField<>();
    public final ObservableField<Boolean> loading = new ObservableField<>();

    public void init () {
        room = stateService.getData();
        resultsListView.setAdapter(resultsAdapter);
        query.addOnPropertyChangedCallback(this);
    }

    private void fetchResults () {

        // check internet connection
        if (connectionService.getConnectionState(context)) {
            connectionService.showMessage(context);
            return;
        }

        String token = localStorageService.getToken();
        String queryString = query.get();
        call = httpService.getApi().searchForUser(token, queryString);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(context, response);
                    return;
                }

                if (response.body() == null || response.body().size() == 0) return;

                resultsAdapter.clear();
                resultsAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (call.isCanceled()) return;
                httpService.showClientErrors(context, t);
            }
        });
    }

    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
        if (query.get() == null || query.get().equals("")) return;

        if (call == null) fetchResults();
        else {
            call.cancel();
            fetchResults();
        }
    }
}
