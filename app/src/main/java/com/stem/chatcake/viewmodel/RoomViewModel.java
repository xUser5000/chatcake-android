package com.stem.chatcake.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.adapter.RoomMessagesAdapter;
import com.stem.chatcake.model.Message;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.RoomInfoActivity;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@Builder
public class RoomViewModel extends BaseObservable implements SocketService.OnMessageReceivedListener {

    // dependencies
    private Activity host;
    private HttpService httpService;
    private StorageService storageService;
    private SocketService socketService;
    private Room data;

    private LinearLayout mainContent;
    private ListView messagesListView;
    private RoomMessagesAdapter messagesAdapter;

    // state
    private String roomName;
    private String content;

    private final String ROOM_INFO_DIALOG_TAG = "room_info";

    // init method
    public void init () {
        roomName = data.getName();
        initMessagesListView();
        subscribe();
        fetchRoom();
    }

    private void subscribe () {
        socketService.subscribe(data.getId(), host, this);
    }

    private void initMessagesListView () {
        messagesAdapter = new RoomMessagesAdapter(
                host,
                R.layout.room_messages_list_item,
                new ArrayList<Message>(),
                storageService);
        messagesListView.setAdapter(messagesAdapter);
    }

    private void fetchRoom () {
        String roomId = data.getId();
        String token = storageService.getToken();
        startLoading();
        Call<Room> call = httpService.getApi().getRoomInfo(token, roomId);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                stopLoading();
                if (!response.isSuccessful()) {
                    httpService.showErrors(host, response);
                    return;
                }

                Room fetchedRoom = response.body();
                data.setAdmin(fetchedRoom.getAdmin());
                data.setMembers(fetchedRoom.getMembers());
                data.setMessages(fetchedRoom.getMessages());

                messagesAdapter.addAll(data.getMessages());
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                stopLoading();
                httpService.showClientErrors(host, t);
            }
        });
    }

    public void goToRoomInfo () {
        if (data != null) {
            Intent intent = new Intent(host, RoomInfoActivity.class);
            intent.putExtra("admin", data.getAdmin());
            intent.putStringArrayListExtra("members", (ArrayList<String>) data.getMembers());
            host.startActivity(intent);
        }
    }

    private void startLoading () {
        mainContent.setVisibility(View.INVISIBLE);
    }

    private void stopLoading () {
        mainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void messageReceived(Message message) {
        Toast.makeText(host, message.getContent(), Toast.LENGTH_SHORT).show();
    }

    public void sendMessage () {
        Message message = Message.builder()
                .roomId(data.getId())
                .content(content)
                .from(storageService.getUsername())
                .build();
        socketService.sendMessage(message);
        messagesAdapter.add(message);
    }

}
