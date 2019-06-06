package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.HomeActivity;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class LoginViewModel {

    // dependencies
    private Context context;
    private HttpService httpService;
    private StorageService storageService;

    // state
    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    public void login () {
        if (username.get() == null || password.get() == null) {
            Toast.makeText(context, "Please, fill out all the field", Toast.LENGTH_SHORT).show();
            return;
        }

        // constructing the user
        User user = User.builder()
                .username(username.get())
                .password(password.get())
                .build();

        Call<User> call = httpService.getApi().login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(context, response);
                    return;
                }

                // save user info in the local storage
                storageService.saveUserInfo(response.body());

                // display a welcome message
                Toast.makeText(context, "Welcome " + response.body().getName(), Toast.LENGTH_SHORT).show();

                // redirect to the home page
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                httpService.showClientErrors(context, t);
            }
        });
    }

}
