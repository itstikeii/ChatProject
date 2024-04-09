package com.tikeii.nghienchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;

public class MainActivity extends AppCompatActivity {

    ImageButton addUser;
    chats_fragment chatsFragment;
    Profile_fragment profileFragment;
    BottomNavigationView navigationView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatsFragment = new chats_fragment();
        profileFragment = new Profile_fragment();

        navigationView = findViewById(R.id.botDrawer_main);
        addUser = findViewById(R.id.main_chat_add_btn);


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), add_user.class);
                startActivity(i);
            }
        });

        navigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.botDrawer_chats) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,chatsFragment).commit();
                }
                if (item.getItemId()==R.id.botDrawer_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,profileFragment).commit();
                }
            }
        });
        navigationView.setSelectedItemId(R.id.botDrawer_chats);

        getFCMToken();

    }

    private void getFCMToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    FireBaseUltil.getUser_withAuthUDI().update("token",token);
                }
            }
        });
    }
}