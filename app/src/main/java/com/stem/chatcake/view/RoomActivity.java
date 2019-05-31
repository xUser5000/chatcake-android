package com.stem.chatcake.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView roomNameText;
    private ImageButton roomInfoButton;
    private HttpService httpService;
    private StorageService storageService;
    private RelativeLayout progressLayout;
    private RelativeLayout mainContent;

    private Room roomData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        roomNameText = findViewById(R.id.room_name);
        roomInfoButton = findViewById(R.id.room_room_info_button);
        progressLayout = findViewById(R.id.room_progress_layout);
        mainContent = findViewById(R.id.room_main_content);

        httpService = HttpService.getInstance();
        storageService = StorageService.getInstance(this);

        roomData = getRoomFromIntent();
        roomNameText.setText(roomData.getName());
        roomInfoButton.setOnClickListener(this);
    }

    private Room getRoomFromIntent () {
        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String roomId = intent.getStringExtra("roomId");
        Room room = new Room();
        room.setName(roomName);
        room.setId(roomId);
        return room;
    }

    private void fetchRoom () {
        String roomId = roomData.getId();
        String token = storageService.getToken();
        startLoading();
        Call<Room> call = httpService.getApi().getRoomInfo(token, roomId);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (!response.isSuccessful()) {
                    httpService.showErrors(RoomActivity.this, response);
                    return;
                }

                Room fetchedRoom = response.body();
                roomData.setAdmin(fetchedRoom.getAdmin());
                roomData.setMembers(fetchedRoom.getMembers());
                roomData.setMessages(fetchedRoom.getMessages());
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(RoomActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.room_room_info_button && roomData != null) {
            Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();
        }
    }

    // some utility methods :)
    private void startLoading () {
        progressLayout.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.INVISIBLE);
    }
    private void stopLoading () {
        progressLayout.setVisibility(View.INVISIBLE);
        mainContent.setVisibility(View.VISIBLE);
    }
}
