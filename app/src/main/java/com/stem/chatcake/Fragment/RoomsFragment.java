package com.stem.chatcake.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.FragmentRoomsBinding;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.RoomsViewModel;

public class RoomsFragment extends Fragment {

    private RoomsViewModel viewModel;

    public RoomsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentRoomsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rooms, container, false);
        View parent = binding.getRoot();

        SwipeRefreshLayout refreshLayout = parent.findViewById(R.id.rooms_swipe_refresh);
        ListView roomsListView = parent.findViewById(R.id.rooms_list_view);
        TextView noRooms = parent.findViewById(R.id.rooms_no_rooms);
        FragmentManager fragmentManager = getFragmentManager();

        // Dependency Injection for the dialog
        CreateRoomDialogFragment dialogFragment = CreateRoomDialogFragment
                .newInstance(HttpService.getInstance(), StorageService.getInstance(getContext()));

        // Dependency Injection for the view model
        viewModel = RoomsViewModel.builder()
                .context(getContext())
                .httpService(HttpService.getInstance())
                .storageService(StorageService.getInstance(getContext()))
                .refreshLayout(refreshLayout)
                .roomsListView(roomsListView)
                .noRoomsTextView(noRooms)
                .dialogFragment(dialogFragment)
                .fragmentManager(fragmentManager)
                .build();

        binding.setViewModel(viewModel);

        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.init();
    }
}
