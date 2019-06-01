package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@Builder
public class RoomsViewModel extends BaseObservable {

    // dependencies
    private Context context;
    private HttpService httpService;
    private StorageService storageService;
    private SwipeRefreshLayout refreshLayout;

    // state
    private boolean loading = false;

    public RoomsViewModel () {
        fetchRooms();
    }

    // get all rooms for the current user
    private void fetchRooms () {
        String token = storageService.getToken();
        startLoading();
        Call<List<Room>> call = httpService.getApi().getRooms(token);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                stopLoading();
                if (response.isSuccessful()) {

                    Toast.makeText(context, response.body().get(0).getAdmin(), Toast.LENGTH_SHORT).show();

                } else httpService.showErrors(context, response);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                stopLoading();
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // start loading method
    private void startLoading () {
        refreshLayout.setRefreshing(true);
    }

    // stop loading method
    private void stopLoading () {
        refreshLayout.setRefreshing(false);
    }

}
