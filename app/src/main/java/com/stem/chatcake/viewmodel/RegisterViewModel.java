package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.widget.Toast;

import com.stem.chatcake.BR;
import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.view.LoginActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends BaseObservable {

    private Context context;

    private String name;
    private String username;
    private String password;

    private HttpService httpService;

    public RegisterViewModel(Context context) {
        this.context = context;
        httpService = HttpService.getInstance();
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.viewModel);
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

        User user = new User(name, username, password);

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
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
