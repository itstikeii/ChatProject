package com.tikeii.nghienchat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.rpc.context.AttributeContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class login_step2 extends AppCompatActivity {
    EditText otp;
    ImageButton tiep;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView reSend;
    String sdt,phienxacthuc,codexacthuc;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step2);

        progressBar = findViewById(R.id.login_stp2_proogressBar); progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminateDrawable(new FadingCircle());
        otp =  findViewById(R.id.login_stp2_otp);
        tiep = findViewById(R.id.login_stp2_next);
        reSend = findViewById(R.id.login_stp2_reSendOTP);
        auth = FirebaseAuth.getInstance();
        Intent i = getIntent();
        sdt = i.getStringExtra("PHONE_INPUT");

        sendOTP(sdt);

        tiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xacthuc(otp.getText().toString());
                proressing(true);
            }
        });
    }

    private void sendOTP(String sdt) {
        proressing(true);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(sdt)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            String otp_send = credential.getSmsCode();
            if (otp_send != null) {
                xacthuc(otp_send);
                proressing(false);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(),"OTP không chính xác, Vui lòng thử lại !",Toast.LENGTH_LONG).show();
            proressing(false);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            phienxacthuc = s;
            Toast.makeText(getApplicationContext(),"Đã gửi OTP !",Toast.LENGTH_LONG).show();
            proressing(false);
        }
    };

    private void xacthuc(String otp_enter) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(phienxacthuc,otp_enter);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Xác thực thành công !",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),login_step3.class);
                            i.putExtra("PHONE_INPUT",sdt);
                            startActivity(i);
                        }
                    }
                });
    }


    void proressing(boolean guilai) {
        if (guilai) {
            progressBar.setVisibility(View.VISIBLE);
            tiep.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            tiep.setVisibility(View.VISIBLE);
        }
    }

}