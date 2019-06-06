package com.stem.chatcake.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityRoomBinding;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.RoomViewModel;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRoomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        View parent = binding.getRoot();

        LinearLayout mainContent = parent.findViewById(R.id.room_main_content);
        ListView messagesListView = parent.findViewById(R.id.room_messages_list_view);

        // dependency injection
        RoomViewModel viewModel = RoomViewModel.builder()
                .host(this)
                .httpService(HttpService.getInstance())
                .storageService(StorageService.getInstance(this))
                .socketService(SocketService.getInstance())
                .data(getRoomFromIntent())
                .mainContent(mainContent)
                .messagesListView(messagesListView)
                .build();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.init();

    }

    private Room getRoomFromIntent () {
        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String roomId = intent.getStringExtra("roomId");
        Room room = Room.builder()
                .name(roomName)
                .id(roomId)
                .build();
        return room;
    }
}
