package com.tikeii.nghienchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.UserModel;

public class login_step3 extends AppCompatActivity {

    EditText user;
    ProgressBar progressBar;
    ImageButton next;
    UserModel userModel;

    String sdt ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3_step3);
        user = findViewById(R.id.login_stp3_username);
        progressBar = findViewById(R.id.login_stp3_progressBar);
        next = findViewById(R.id.login_stp3_next);

        Intent i = getIntent();
        sdt = i.getStringExtra("PHONE_INPUT");
        getUser();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUser();
            }
        });
    }

    void  setUser() {
        proressing(true);
        String tennguoidung = user.getText().toString();
        if (tennguoidung.isEmpty() || tennguoidung.length()<3) {
            user.setError("Tên đăng nhập từ 3 ký tự trở lên");
            return;
        }

        if (userModel != null) {
            userModel.setName(tennguoidung);
        } else {
            userModel = new UserModel(tennguoidung,sdt,Timestamp.now(),FireBaseUltil.currentUserID());
        }
        FireBaseUltil.getUser_withAuthUDI().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                proressing(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }



    private void getUser() {
        proressing(true);
        FireBaseUltil.getUser_withAuthUDI().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                proressing(false);
                if (task.isSuccessful()) {
                    userModel = task.getResult().toObject(UserModel.class);
                    if (userModel != null) {
                        user.setText(userModel.getName());
                    }
                }
            }
        });
    }

    void proressing(boolean guilai) {
        if (guilai) {
            progressBar.setVisibility(View.VISIBLE);
            next.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        }
    }
}