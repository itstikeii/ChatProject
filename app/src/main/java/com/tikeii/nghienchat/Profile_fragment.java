package com.tikeii.nghienchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Profile_fragment extends Fragment {

    EditText name,phone;
    ImageButton logout,del;
    AppCompatButton save;
    ProgressBar progressBar;
    UserModel userModel;
    CircleImageView avt;
    ActivityResultLauncher<Intent> avtPicker;
    Uri selectAVT;


    public Profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avtPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectAVT = data.getData();
                            AndroidUntil.setUserAVT(getContext(),selectAVT,avt);
                        }
                    }
                });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        name = view.findViewById(R.id.profile_update_name);
        phone = view.findViewById(R.id.profile_update_phone);
        logout = view.findViewById(R.id.profile_logout);
        save = view.findViewById(R.id.profile_update_save);
        avt = view.findViewById(R.id.profile_update_avt);
        progressBar = view.findViewById(R.id.profile_update_prBar);
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminateDrawable(new FadingCircle());
        del = view.findViewById(R.id.profile_delete);


        getData_user();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FireBaseUltil.logout();
                            Intent i = new Intent(getContext(), splash_layout.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                });

            }
        });

        avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(Profile_fragment.this).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                avtPicker.launch(intent);
                                return null;
                            }
                        });
            }
        });
        FirebaseFirestore db  = FirebaseFirestore.getInstance();

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("USERS").document(FireBaseUltil.currentUserID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FireBaseUltil.logout();
                                Toast.makeText(getContext(),"Xóa tài khoản thành công !",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getContext(), splash_layout.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Vui lòng thử lại !",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });



        return view;
    }

    private void getData_user() {
        proressing(true);
        FireBaseUltil.getUserAVT().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri = task.getResult();
                                    AndroidUntil.setUserAVT(getContext(),uri,avt);
                                }
                            }
                        });
        FireBaseUltil.getUser_withAuthUDI().get().addOnCompleteListener(task -> {
            proressing(false);
            userModel = task.getResult().toObject(UserModel.class);
            name.setText(userModel.getName());
            phone.setText(userModel.getPhone());

        });
    }

    void updateUser() {
        String name_new = name.getText().toString();
        if (name_new.isEmpty() || name_new.length()<3) {
            name.setError("Tên đăng nhập từ 3 ký tự trở lên");
            return;
        }
        userModel.setName(name_new);
        proressing(true);
        if (selectAVT!=null) {
            FireBaseUltil.getUserAVT().putFile(selectAVT)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            updateTOFireBase();
                        }
                    });
        } else {
            updateTOFireBase();
        }


    }


    void updateTOFireBase() {
        FireBaseUltil.getUser_withAuthUDI().set(userModel).addOnCompleteListener(task -> {
           proressing(false);
           if (task.isSuccessful()){
               Toast.makeText(getContext(), "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(getContext(), "Có lỗi xảy ra, Vui lòng thử lại !", Toast.LENGTH_SHORT).show();
           }
        });
    }

    void proressing(boolean guilai) {
        if (guilai) {
            progressBar.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }
    }
}