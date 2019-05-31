package com.stem.chatcake.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityRegisterBinding;
import com.stem.chatcake.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        RegisterViewModel viewModel = new RegisterViewModel(this);
        binding.setViewModel(viewModel);
    }
}
