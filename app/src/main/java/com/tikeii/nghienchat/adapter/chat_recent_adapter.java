package com.tikeii.nghienchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tikeii.nghienchat.Chat;
import com.tikeii.nghienchat.R;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.ChatroomModel;
import com.tikeii.nghienchat.model.UserModel;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_recent_adapter extends FirestoreRecyclerAdapter<ChatroomModel, chat_recent_adapter.ChatTextModelHolder> {
    Context context;


    public chat_recent_adapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatTextModelHolder holder, int position, @NonNull ChatroomModel model) {
            FireBaseUltil.getFriendsFromChatRoom(model.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                   if (t.isSuccessful()) {
                       boolean ifsendbyme = model.getLastTextSenderID().equals(FireBaseUltil.currentUserID());
                       UserModel friends = t.getResult().toObject(UserModel.class);
                        //---------------setAVT-----------
                       FireBaseUltil.getFriendsAVT(friends.getID()).getDownloadUrl()
                               .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Uri> task) {
                                       if (task.isSuccessful()) {
                                           Uri uri = task.getResult();
                                           AndroidUntil.setUserAVT(context,uri,holder.userAVT);
                                       }
                                   }
                               });
                       //-------------------------------
                       holder.username.setText(friends.getName());
                       holder.lastText.setText(model.getLastText());
                       if (ifsendbyme == true) {
                           holder.lastText.setText("Tôi : "+model.getLastText());
                       }

                       holder.lastTime.setText(FireBaseUltil.timeStampToString(model.getLastTextSentTime()));

                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent i =  new Intent(context, Chat.class);
                               AndroidUntil.sendUserModelData(i,friends);
                               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               context.startActivity(i);
                           }
                       });

                   }

                }
            });
    }

    @NonNull
    @Override
    public ChatTextModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_recent_row,parent,false);
        return new ChatTextModelHolder(view);
    }

    class ChatTextModelHolder extends RecyclerView.ViewHolder{
        TextView username,lastText,lastTime;
        CircleImageView userAVT;
        public ChatTextModelHolder(@NonNull View itemView) {
            super(itemView);
            userAVT = itemView.findViewById(R.id.chat_recent_row_userAVT);
            username = itemView.findViewById(R.id.chat_recent_row_username);
            lastText = itemView.findViewById(R.id.chat_recent_row_lassText);
            lastTime = itemView.findViewById(R.id.chat_recent_row_lassTextTime);
        }
    }
}
