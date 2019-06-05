package com.stem.chatcake.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stem.chatcake.R;

import java.util.List;

public class RoomInfoActivity extends AppCompatActivity {

    private TextView adminText;
    private ListView membersListView;
    private ArrayAdapter<String> membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        adminText = findViewById(R.id.room_info_admin_text);
        membersListView = findViewById(R.id.room_info_members_list_view);

        String admin = getIntent().getStringExtra("admin");
        List<String> members = getIntent().getStringArrayListExtra("members");

        adminText.setText(admin);
        membersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, members);
        membersListView.setAdapter(membersAdapter);
    }
}
