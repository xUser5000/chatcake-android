package com.stem.chatcake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.model.User;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsAdapter extends ArrayAdapter<User> {

    private Context context;
    private HttpService httpService;
    private LocalStorageService localStorageService;
    private StateService stateService;
    private ConnectionService connectionService;

    private Room room;

    public SearchResultsAdapter (@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    // setters required for injection
    public SearchResultsAdapter setHttpService(HttpService httpService) {
        this.httpService = httpService;
        return this;
    }

    public SearchResultsAdapter setLocalStorageService(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
        return this;
    }

    public SearchResultsAdapter setStateService(StateService stateService) {
        this.stateService = stateService;
        return this;
    }

    public SearchResultsAdapter setConnectionService (ConnectionService connectionService) {
        this.connectionService = connectionService;
        return this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.search_results_list_item, parent, false);

        room = stateService.getData();

        TextView nameText = listItem.findViewById(R.id.search_list_item_name);
        TextView usernameText = listItem.findViewById(R.id.search_list_item_username);
        final Button addButton = listItem.findViewById(R.id.search_list_item_add_member_button);
        final TextView stateLabel = listItem.findViewById(R.id.search_list_item_state_label);

        String name = getItem(position).getName();
        final String username = getItem(position).getUsername();

        nameText.setText(name);
        usernameText.setText(username);

        if (
                room.getMembers().contains(username) ||
                room.getAdmin().equals(username)
        ) {

            // the user is already a member
            showLabel(addButton, stateLabel);
            stateLabel.setText("Member");
            stateLabel.setTextColor(context.getResources().getColor(android.R.color.darker_gray));

        } else {

            showButton(addButton, stateLabel);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // check internet connection
                    if (connectionService.getConnectionState(context)) {
                        connectionService.showMessage(context);
                        return;
                    }

                    showLabel(addButton, stateLabel);
                    stateLabel.setText("Adding....");
                    stateLabel.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    String token = localStorageService.getToken();
                    Call<ResponseBody> call = httpService.getApi().addMember(token, room.getId(), username);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                showButton(addButton, stateLabel);
                                httpService.showErrors(context, response);
                                return;
                            }

                            stateLabel.setText("Added");
                            stateLabel.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                            stateService.addMember(username);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            httpService.showClientErrors(context, t);
                            showButton(addButton, stateLabel);
                        }
                    });
                }
            });

        }

        return listItem;
    }

    private void showButton (Button button, TextView label) {
        button.setVisibility(View.VISIBLE);
        label.setVisibility(View.INVISIBLE);
    }

    private void showLabel (Button button, TextView label) {
        label.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
    }
}
