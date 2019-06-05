package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.widget.Toast;

import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.HomeActivity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@Builder
public class LoginViewModel extends BaseObservable {

    // dependencies
    private Context context;
    private HttpService httpService;
    private StorageService storageService;

    // state
    private String username;
    private String password;


    public void login () {
        if (username == null || password == null) {
            Toast.makeText(context, "Please, fill out all the field", Toast.LENGTH_SHORT).show();
            return;
        }

        // constructing the user
        User user = User.builder()
                .username(username)
                .password(password)
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
                Toast.makeText(context, "Welcome" + response.body().getName(), Toast.LENGTH_SHORT).show();

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
