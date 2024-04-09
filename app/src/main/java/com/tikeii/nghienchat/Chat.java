package com.tikeii.nghienchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.adapter.ChatTextAdapter;
import com.tikeii.nghienchat.adapter.search_user_adapter;
import com.tikeii.nghienchat.model.ChatTextModel;
import com.tikeii.nghienchat.model.ChatroomModel;
import com.tikeii.nghienchat.model.UserModel;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chat extends AppCompatActivity {
    UserModel friends;
    CircleImageView friend_avt;
    TextView friend_name;
    EditText text;
    ImageButton back,send,del;
    RecyclerView recyclerView;
    String roomChatID;
    ChatroomModel chatroomModel;
    ChatTextAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        friends = AndroidUntil.getDataUserfromIntent(getIntent());

        roomChatID = FireBaseUltil.getdata_ChatRoomID(FireBaseUltil.currentUserID(),friends.getID());

        friend_avt = findViewById(R.id.chat_userAVT);
        friend_name = findViewById(R.id.chat_username);
        text = findViewById(R.id.chat_text);
        back = findViewById(R.id.chat_back);
        send = findViewById(R.id.chat_send);
        del = findViewById(R.id.chat_delete);
        recyclerView = findViewById(R.id.chat_recycle);

        FireBaseUltil.getFriendsAVT(friends.getID()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            AndroidUntil.setUserAVT(getApplicationContext(),uri,friend_avt);
                        }
                    }
                });

        text.requestFocus();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("CHATROOMS").document(roomChatID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Đã xóa cuôc trò truyện !",Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Vui lòng thử lại !",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        friend_name.setText(friends.getName());


        getOnCreateChatRoomModel();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messege = text.getText().toString().trim();
                if (messege.isEmpty()){
                    return;
                }
                sendToFriend(messege);


            }
        });

        ChatsSetup();

    }

    private void ChatsSetup() {
        Query query = FireBaseUltil.getChatRoomTextReference(roomChatID).orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatTextModel> options = new FirestoreRecyclerOptions.Builder<ChatTextModel>().setQuery(query, ChatTextModel.class).build();

        adapter = new ChatTextAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendToFriend(String messege) {
        chatroomModel.setLastTextSentTime(Timestamp.now());
        chatroomModel.setLastTextSenderID(FireBaseUltil.currentUserID());
        chatroomModel.setLastText(messege);
        FireBaseUltil.getChatRoomReferance(roomChatID).set(chatroomModel);
        ChatTextModel chatTextModel = new ChatTextModel(messege,FireBaseUltil.currentUserID(),Timestamp.now());
        FireBaseUltil.getChatRoomTextReference(roomChatID).add(chatTextModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            text.setText("");
                            upTexttoNoti(messege);
                        }
                    }
                });
    }

    private void upTexttoNoti(String messege) {
        FireBaseUltil.getUser_withAuthUDI().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel curentUser = task.getResult().toObject(UserModel.class);
                try {
                    JSONObject jsonObject = new JSONObject();



                    JSONObject notify = new JSONObject();
                    notify.put("title",curentUser.getName());
                    notify.put("body",messege);

                    JSONObject data = new JSONObject();
                    data.put("USER_ID",curentUser.getID());

                    jsonObject.put("notification",notify);
                    jsonObject.put("data",data);
                    jsonObject.put("to",friends.getToken());

                    callAPI(jsonObject);
                } catch (Exception e){

                }
            }
        });
    }


    void callAPI(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAA2F8GNrg:APA91bGA18OWJkKB21nMhH1SigSIOplIxzIXG_mP-_Gqed1W62tSVVqdGRJBPQO-ytS9MkFvpI5hTxx22UD3CxMoTvkX3QHq6VLWZ6cEcLn2Hz_079Ig2bhjpq5-wyedHHuIq93x-1In")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }


    private void getOnCreateChatRoomModel() {
        FireBaseUltil.getChatRoomReferance(roomChatID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    chatroomModel = task.getResult().toObject(ChatroomModel.class);
                    if (chatroomModel == null) {
                        chatroomModel = new ChatroomModel(roomChatID, Arrays.asList(FireBaseUltil.currentUserID(),friends.getID()), Timestamp.now(),"");
                        FireBaseUltil.getChatRoomReferance(roomChatID).set(chatroomModel);
                    }
                }
            }
        });

    }

}