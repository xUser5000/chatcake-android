package com.stem.chatcake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomMembersAdapter extends ArrayAdapter<String> {

    // dependencies
    private Context context;
    private HttpService httpService;
    private LocalStorageService localStorageService;
    private StateService stateService;
    private ConnectionService connectionService;
    private boolean admin;

    public RoomMembersAdapter (Context context, int res, List<String> members) {
        super(context, res, members);
        this.context = context;
    }

    // setters required for injection
    public RoomMembersAdapter setHttpService(HttpService httpService) {
        this.httpService = httpService;
        return this;
    }

    public RoomMembersAdapter setLocalStorageService(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
        return this;
    }

    public RoomMembersAdapter setStateService(StateService stateService) {
        this.stateService = stateService;
        String adminUsername = stateService.getData().getAdmin();
        String currentUsername = localStorageService.getUsername();
        admin = adminUsername.equals(currentUsername);
        return this;
    }

    public RoomMembersAdapter setConnectionService(ConnectionService connectionService) {
        this.connectionService = connectionService;
        return this;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.members_list_item, parent, false);

        final TextView usernameText = listItem.findViewById(R.id.members_list_item_username);
        final ImageButton deleteButton = listItem.findViewById(R.id.members_list_item_delete_button);
        final ProgressBar progressBar = listItem.findViewById(R.id.members_list_item_progress_bar);

        usernameText.setText(getItem(position));

        if (!admin) {
            deleteButton.setVisibility(View.INVISIBLE);
            return listItem;
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check internet connection
                if (connectionService.getConnectionState(context)) {
                    connectionService.showMessage(context);
                    return;
                }

                String token = localStorageService.getToken();
                String roomId = stateService.getData().getId();
                final String username = getItem(position);
                startLoading(progressBar, deleteButton);
                Call<ResponseBody> call = httpService.getApi().removeMember(token, roomId, username);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        stopLoading(progressBar, deleteButton);
                        if (!response.isSuccessful()) {
                            httpService.showErrors(context, response);
                            return;
                        }

                        remove(username);
                        stateService.removeMember(username);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        stopLoading(progressBar, deleteButton);
                        httpService.showClientErrors(context, t);
                    }
                });
            }
        });

        return listItem;
    }

    private void startLoading (ProgressBar progressBar, ImageButton button) {
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
    }

    private void stopLoading (ProgressBar progressBar, ImageButton button) {
        progressBar.setVisibility(View.INVISIBLE);
        button.setVisibility(View.VISIBLE);
    }
}
