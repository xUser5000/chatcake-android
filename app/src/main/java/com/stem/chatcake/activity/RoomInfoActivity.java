package com.stem.chatcake.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.adapter.RoomMembersAdapter;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;
import com.stem.chatcake.viewmodel.RoomInfoViewModel;

import java.util.ArrayList;

public class RoomInfoActivity extends AppCompatActivity {

    private RoomInfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        TextView adminText = findViewById(R.id.room_info_admin_text);
        FloatingActionButton floatingActionButton = findViewById(R.id.room_info_add_member_button);
        ListView membersListView = findViewById(R.id.room_info_members_list_view);
        RelativeLayout fabLayout = findViewById(R.id.room_info_fab_layout);

        HttpService httpService = HttpService.getInstance();
        LocalStorageService localStorageService = LocalStorageService.getInstance(this);
        StateService stateService = StateService.getInstance();

        // constructing the members adapter
        RoomMembersAdapter roomMembersAdapter = new RoomMembersAdapter(this, 0, new ArrayList<String>())
                .setHttpService(httpService)
                .setLocalStorageService(localStorageService)
                .setStateService(stateService);

        // injecting dependencies into the view model
        viewModel = RoomInfoViewModel.builder()
                .context(this)
                .httpService(httpService)
                .localStorageService(localStorageService)
                .stateService(stateService)
                .membersListView(membersListView)
                .membersAdapter(roomMembersAdapter)
                .adminText(adminText)
                .fabLayout(fabLayout)
                .floatingActionButton(floatingActionButton)
                .build();

        viewModel.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.updateData();
    }
}
