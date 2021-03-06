package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.stem.chatcake.model.User;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.activity.LoginActivity;

import lombok.Builder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class RegisterViewModel {

    // dependencies
    private Context context;
    private HttpService httpService;
    private ConnectionService connectionService;

    // state
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    // register a new user
    public void register () {

        // check internet connection
        if (connectionService.getConnectionState(context)) {
            connectionService.showMessage(context);
            return;
        }

        // validation
        if (name.get() == null || username.get() == null || password.get() == null) {
            Toast.makeText(context, "Please, fill out all the field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.get().length() < 6) {
            Toast.makeText(context, "Password must be greater than 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        // constructing the user
        User user = User.builder()
                .name(name.get())
                .password(password.get())
                .username(username.get())
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
