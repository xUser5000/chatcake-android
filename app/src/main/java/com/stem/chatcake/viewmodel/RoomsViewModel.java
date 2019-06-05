package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stem.chatcake.Fragment.CreateRoomDialogFragment;
import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.RoomActivity;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@Getter
@Setter
@Builder
public class RoomsViewModel extends BaseObservable implements
        SwipeRefreshLayout.OnRefreshListener,
        CreateRoomDialogFragment.OnDialogDismissListener,
        AdapterView.OnItemClickListener
{

    // dependencies
    private Context context;
    private HttpService httpService;
    private StorageService storageService;
    private FragmentManager fragmentManager;

    private SwipeRefreshLayout refreshLayout;
    private ListView roomsListView;
    private TextView noRoomsTextView;
    private CreateRoomDialogFragment dialogFragment;

    // state
    private List<Room> rooms;
    private ArrayAdapter<Room> roomsAdapter;
    private static final String CREATE_ROOM_DIALOG_TAG = "create_room_dialog";

    // init function
    public void init () {
        // config
        refreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent
        );
        refreshLayout.setOnRefreshListener(this);
        rooms = new ArrayList<>();
        roomsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, rooms);
        roomsListView.setAdapter(roomsAdapter);
        roomsListView.setOnItemClickListener(this);

        fetchRooms();
    }

    // get all rooms for the current user
    private void fetchRooms () {
        String token = storageService.getToken();
        startLoading();
        Call<List<Room>> call = httpService.getApi().getRooms(token);
        call.enqueue(new Callback<List<Room>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                stopLoading();
                if (response.isSuccessful()) {

                    hideNoRooms();

                    List<Room> rooms = response.body();

                    if (rooms.size() == 0) noRoomsTextView.setVisibility(View.VISIBLE);
                    else {
                        noRoomsTextView.setVisibility(View.INVISIBLE);
                        roomsAdapter.clear();
                        roomsAdapter.addAll(response.body());
                    }

                } else {
                    showNoRooms();
                    httpService.showErrors(context, response);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                stopLoading();
                showNoRooms();
                httpService.showClientErrors(context, t);
            }
        });
    }

    // start loading method
    private void startLoading () {
        refreshLayout.setRefreshing(true);
    }

    // stop loading method
    private void stopLoading () {
        refreshLayout.setRefreshing(false);
    }

    // show no rooms label
    private void showNoRooms () {
        noRoomsTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoRooms () {
        noRoomsTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRefresh() {
        fetchRooms();
    }

    // show create room dialog
    public void showCreateRoomDialog () {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(CREATE_ROOM_DIALOG_TAG);
        if (prev != null) transaction.remove(prev);
        dialogFragment.setHostViewModel(this);
        dialogFragment.show(transaction, CREATE_ROOM_DIALOG_TAG);
    }

    @Override
    public void onDialogDismissed(Room room) {
        roomsAdapter.add(room);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // go to the room activity
        Room room = roomsAdapter.getItem(position);
        Intent intent = new Intent(getContext(), RoomActivity.class);
        intent.putExtra("roomName", room.getName());
        intent.putExtra("roomId", room.getId());
        context.startActivity(intent);
    }
}
