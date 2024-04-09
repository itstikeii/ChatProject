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
import com.tikeii.nghienchat.Chat;
import com.tikeii.nghienchat.R;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.UserModel;

import org.w3c.dom.Text;

public class search_user_adapter extends FirestoreRecyclerAdapter<UserModel, search_user_adapter.UserModelHolder> {
    Context context;


    public search_user_adapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelHolder holder, int position, @NonNull UserModel model) {
        holder.username.setText(model.getName());
        holder.phone.setText(model.getPhone());
        if (model.getID().equals(FireBaseUltil.currentUserID())) {
            holder.username.setText(model.getName()+" (tôi)");
        }

        //------------setAVT--------------
        FireBaseUltil.getFriendsAVT(model.getID()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            AndroidUntil.setUserAVT(context,uri,holder.userAVT);
                        }
                    }
                });
        //---------------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(context, Chat.class);
                AndroidUntil.sendUserModelData(i,model);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public UserModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_user_recycle_row,parent,false);
        return new UserModelHolder(view);
    }

    class UserModelHolder extends RecyclerView.ViewHolder{
        TextView username,phone;
        ImageView userAVT;
        public UserModelHolder(@NonNull View itemView) {
            super(itemView);
            userAVT = itemView.findViewById(R.id.search_row_userAVT);
            username = itemView.findViewById(R.id.search_row_username);
            phone = itemView.findViewById(R.id.search_row_sdt);
        }
    }
}
