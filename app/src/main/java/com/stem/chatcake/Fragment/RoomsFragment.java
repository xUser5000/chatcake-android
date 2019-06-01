package com.stem.chatcake.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.FragmentRoomsBinding;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.RoomsViewModel;

public class RoomsFragment extends Fragment {

    public RoomsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentRoomsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rooms, container, false);
        View parent = binding.getRoot();

        SwipeRefreshLayout refreshLayout = parent.findViewById(R.id.rooms_swipe_refresh);
        refreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent
        );

        // Dependency Injection
        RoomsViewModel viewModel = RoomsViewModel.builder()
                .context(getContext())
                .httpService(HttpService.getInstance())
                .storageService(StorageService.getInstance(getContext()))
                .refreshLayout(refreshLayout)
                .build();

        binding.setViewModel(viewModel);

        return parent;
    }


}
