package com.stem.chatcake.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.RoomActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton createRoomFab;
    private CreateRoomDialogFragment dialogFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noRoomsText;

    private ListView roomsListView;
    private ArrayAdapter<Room> roomsAdapter;
    private ArrayList<Room> roomsList;

    private StorageService storageService;
    private HttpService httpService;

    public RoomsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parent = inflater.inflate(R.layout.fragment_rooms, container, false);

        createRoomFab = parent.findViewById(R.id.create_room_fab);
        swipeRefreshLayout = parent.findViewById(R.id.rooms_swipe_refresh);
        roomsListView = parent.findViewById(R.id.rooms_list_view);
        noRoomsText = parent.findViewById(R.id.rooms_no_rooms_text);

        storageService = StorageService.getInstance(getContext());
        httpService = HttpService.getInstance();

        initSwipeRefresh();
        initFab();
        initListViw();
        fetchRooms();
        return parent;
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent
        );
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initListViw() {
        roomsList = new ArrayList<>();
        roomsListView.setOnItemClickListener(this);
        roomsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, roomsList);
        roomsListView.setAdapter(roomsAdapter);
    }

    private void initFab() {
        createRoomFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_room_fab) {
            dialogFragment = new CreateRoomDialogFragment();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), "create_room_tag");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            String roomName = data.getStringExtra("roomName");
            String roomId = data.getStringExtra("roomId");
            Room room = new Room();
            room.setName(roomName);
            room.setId(roomId);
            roomsAdapter.add(room);
            if (roomsAdapter.getCount() == 0) showNoRooms();
            else hideNoRooms();
        }
    }

    private void fetchRooms() {
        swipeRefreshLayout.setRefreshing(true);
        String token = storageService.getToken();
        Call<List<Room>> call = httpService.getApi().getRooms(token);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (!response.isSuccessful()) {
                    httpService.showErrors(getContext(), response);
                    return;
                }

                List<Room> rooms = response.body();
                roomsAdapter.clear();
                roomsAdapter.addAll(rooms);
                storageService.saveRooms(rooms);

                if (rooms.size() == 0) showNoRooms();
                else hideNoRooms();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showNoRooms();
                t.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchRooms();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Room room = roomsList.get(position);
        Intent intent = new Intent(getContext(), RoomActivity.class);
        intent.putExtra("roomName", room.getName());
        intent.putExtra("roomId", room.getId());
        startActivity(intent);
    }

    private void showNoRooms() {
        roomsListView.setVisibility(View.INVISIBLE);
        noRoomsText.setVisibility(View.VISIBLE);
    }

    private void hideNoRooms() {
        roomsListView.setVisibility(View.VISIBLE);
        noRoomsText.setVisibility(View.INVISIBLE);
    }
}
