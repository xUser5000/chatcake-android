package com.stem.chatcake.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.adapter.RoomMessagesAdapter;
import com.stem.chatcake.model.Message;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StateService;
import com.stem.chatcake.activity.RoomInfoActivity;

import java.util.ArrayList;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class RoomViewModel extends DataSetObserver implements SocketService.OnMessageReceivedListener {

    // dependencies
    private Activity host;
    private HttpService httpService;
    private LocalStorageService localStorageService;
    private SocketService socketService;
    private StateService stateService;
    private ConnectionService connectionService;
    private Room data;

    private LinearLayout mainContent;
    private ListView messagesListView;
    private RoomMessagesAdapter messagesAdapter;

    // state
    public final ObservableField<String> roomName = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();

    private final String ROOM_INFO_DIALOG_TAG = "room_info";

    // init method
    public void init () {
        getData();
        roomName.set(data.getName());
        initMessagesListView();
        fetchRoom();
        subscribe();
    }

    private void subscribe () {
        socketService.subscribe(host, this);
    }

    private void getData () {
        data = stateService.getData();
    }

    private void initMessagesListView () {
        messagesAdapter = new RoomMessagesAdapter(
                host,
                R.layout.room_messages_list_item,
                new ArrayList<Message>(),
                localStorageService);
        messagesAdapter.registerDataSetObserver(this);
        messagesListView.setAdapter(messagesAdapter);
    }

    private void fetchRoom () {

        // check internet connection
        if (!connectionService.getConnectionState(host)) {
            connectionService.showMessage(host);
            return;
        }

        String roomId = data.getId();
        String token = localStorageService.getToken();
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

                // save to the state service and messages adapter
                stateService.setData(data);
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
        host.startActivity(new Intent(host, RoomInfoActivity.class));
    }

    private void startLoading () {
        mainContent.setVisibility(View.INVISIBLE);
    }

    private void stopLoading () {
        mainContent.setVisibility(View.VISIBLE);
    }

    public void sendMessage () {

        if (content.get() == null || content.get().equals("")) {
            Toast.makeText(host, "Write a message", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = Message.builder()
                .roomId(data.getId())
                .content(content.get())
                .from(localStorageService.getUsername())
                .build();
        socketService.sendMessage(message);
        content.set("");
        addMessage(message);
    }

    private void addMessage (Message message) {
        messagesAdapter.add(message);
        messagesListView.setSelection(messagesAdapter.getCount() - 1);
    }

    @Override
    public void OnMessageReceived(Message message) {
        messagesAdapter.add(message);
    }

    // scroll the list view to the bottom when the data set change

    @Override
    public void onChanged() {
        messagesListView.setSelection(messagesAdapter.getCount() - 1);
    }
}
