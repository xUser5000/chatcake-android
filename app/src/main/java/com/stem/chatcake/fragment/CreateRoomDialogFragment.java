package com.stem.chatcake.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.stem.chatcake.R;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.viewmodel.RoomsViewModel;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Setter
@Getter
public class CreateRoomDialogFragment extends DialogFragment {

    // dependencies
    private HttpService httpService;
    private LocalStorageService localStorageService;

    private EditText roomNameText;
    private Button createRoomButton;

    private RoomsViewModel hostViewModel;

    public CreateRoomDialogFragment () {}

    public static CreateRoomDialogFragment newInstance (HttpService httpService, LocalStorageService localStorageService) {
        CreateRoomDialogFragment fragment = new CreateRoomDialogFragment();
        fragment.setHttpService(httpService);
        fragment.setLocalStorageService(localStorageService);
        return fragment;
    }

    public interface OnDialogDismissListener {
        void onDialogDismissed (Room room);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_room_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        roomNameText = view.findViewById(R.id.create_room_name);
        createRoomButton = view.findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });
    }

    public void createRoom () {
        String token = localStorageService.getToken();
        String roomName = roomNameText.getText().toString().trim();
        Call<Room> call = httpService.getApi().createRoom(token, roomName);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {

                    if (hostViewModel != null)
                        hostViewModel.onDialogDismissed(response.body());

                    dismissAllowingStateLoss();

                } else httpService.showErrors(getContext(), response);
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                httpService.showClientErrors(getContext(), t);
            }
        });
    }
}
