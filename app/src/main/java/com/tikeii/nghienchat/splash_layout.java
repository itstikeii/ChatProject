package com.tikeii.nghienchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.UserModel;

import java.util.concurrent.Delayed;

public class splash_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);

        ProgressBar progressBar = findViewById(R.id.splash_ProressBar);
        progressBar.setIndeterminateDrawable(new FadingCircle());

        if (FireBaseUltil.isLoggedIN() &&  getIntent().getExtras()!=null) {
            String userID = getIntent().getExtras().getString("USER_ID");
            FireBaseUltil.all().document(userID).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                UserModel model = task.getResult().toObject(UserModel.class);

                                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(mainIntent);

                                Intent i =  new Intent(getApplicationContext(), Chat.class);
                                AndroidUntil.sendUserModelData(i,model);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
        } else  {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FireBaseUltil.isLoggedIN()) {
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);   startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(),login_step1.class);   startActivity(i);
                    }
                    finish();
                }
            }, 1000);
        }


    }
}