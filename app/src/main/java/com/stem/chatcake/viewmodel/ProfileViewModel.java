package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.MainActivity;

import lombok.Builder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class ProfileViewModel {

    // dependencies
    private Context context;
    private StorageService storageService;
    private HttpService httpService;

    // state
    public final ObservableField<String> username = new ObservableField<>();

    public void init () {
        username.set("Username: " + storageService.getUsername());
    }

    // logout function
    public void logout () {
        String token = storageService.getToken();
        Call<ResponseBody> call = httpService.getApi().logout(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(context, response);
                    return;
                }

                // delete the token from the local storage
                storageService.deleteToken();

                // redirect to the main screen
                context.startActivity(new Intent(context, MainActivity.class));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpService.showClientErrors(context, t);
            }
        });
    }

}
