package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.widget.Toast;

import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.view.LoginActivity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@Builder
public class RegisterViewModel extends BaseObservable {

    // dependencies
    private Context context;
    private HttpService httpService;

    // state
    private String name;
    private String username;
    private String password;

    // register a new user
    public void register () {

        // validation

        if (name == null || username == null || password == null) {
            Toast.makeText(context, "Please, fill out all the field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "Password must be greater than 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        // constructing the user
        User user = User.builder()
                .name(name)
                .password(password)
                .username(username)
                .build();

        Call<ResponseBody> call = httpService.getApi().register(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(context, response);
                    return;
                }

                Toast.makeText(context, "Registered", Toast.LENGTH_SHORT).show();

                // redirect to the login screen
                context.startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpService.showClientErrors(context, t);
            }
        });
    }
}
