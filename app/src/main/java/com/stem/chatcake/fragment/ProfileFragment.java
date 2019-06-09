package com.stem.chatcake.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.FargmentProfileBinding;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.viewmodel.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FargmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fargment_profile, container, false);
        View parent = binding.getRoot();

        // dependency Injection
        viewModel = ProfileViewModel.builder()
                .context(getContext())
                .httpService(HttpService.getInstance())
                .localStorageService(LocalStorageService.getInstance(getContext()))
                .build();
        binding.setViewModel(viewModel);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.init();
    }
}
