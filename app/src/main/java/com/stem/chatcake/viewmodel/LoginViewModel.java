package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.widget.Toast;

import com.stem.chatcake.BR;
import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseObservable {

    private Context context;

    private String username;
    private String password;

    private HttpService httpService;
    private StorageService storageService;

    public LoginViewModel (Context context) {
        this.context = context;
        httpService = HttpService.getInstance();
        storageService = StorageService.getInstance(context);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.viewModel);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.viewModel);
    }

    public void login () {
        if (username == null || password == null) {
            Toast.makeText(context, "Please, fill out all the field", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password);

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

                // redirect to the home page
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
