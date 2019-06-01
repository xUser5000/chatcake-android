package com.stem.chatcake.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentRoomsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rooms, container, false);
        View parent = binding.getRoot();

        // Dependency Injection
        RoomsViewModel viewModel = RoomsViewModel.builder()
                .context(getContext())
                .parent(parent)
                .httpService(HttpService.getInstance())
                .storageService(StorageService.getInstance(getContext()))
                .build();

        binding.setViewModel(viewModel);

        return parent;
    }
}
