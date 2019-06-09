package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;
import com.stem.chatcake.activity.SearchActivity;

import lombok.Builder;

@Builder
public class RoomInfoViewModel implements View.OnClickListener {

    // dependencies
    private Context context;
    private HttpService httpService;
    private LocalStorageService localStorageService;
    private StateService stateService;
    private Room data;

    private TextView adminText;
    private ListView membersListView;
    private ArrayAdapter<String> membersAdapter;
    private RelativeLayout fabLayout;
    private FloatingActionButton floatingActionButton;

    public void init () {

        data = stateService.getData();

        adminText.setText(data.getAdmin());

        membersAdapter.clear();
        membersAdapter.addAll(data.getMembers());
        membersListView.setAdapter(membersAdapter);

        // if the current user is an admin, then show the fab
        if (data.getAdmin().equals(localStorageService.getUsername())) {
            fabLayout.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(this);
        }
    }

    public void updateData () {
        data = stateService.getData();
        membersAdapter.clear();
        membersAdapter.addAll(data.getMembers());
    }

    // go to the search activity
    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
