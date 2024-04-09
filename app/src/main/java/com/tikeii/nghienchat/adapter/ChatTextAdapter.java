package com.tikeii.nghienchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.tikeii.nghienchat.Chat;
import com.tikeii.nghienchat.R;
import com.tikeii.nghienchat.Ultils.AndroidUntil;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.model.ChatTextModel;
import com.tikeii.nghienchat.model.ChatTextModel;

import org.w3c.dom.Text;

public class ChatTextAdapter extends FirestoreRecyclerAdapter<ChatTextModel,ChatTextAdapter.ChatTextModelHolder> {
    Context context;


    public ChatTextAdapter(@NonNull FirestoreRecyclerOptions<ChatTextModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatTextModelHolder holder, int position, @NonNull ChatTextModel model) {
        if (model.getSenderID().equals(FireBaseUltil.currentUserID())) {
            holder.you_layout.setVisibility(View.GONE);
            holder.me_layout.setVisibility(View.VISIBLE);
            holder.me.setText(model.getText());
        } else {
            holder.me_layout.setVisibility(View.GONE);
            holder.you_layout.setVisibility(View.VISIBLE);
            holder.you.setText(model.getText());
        }
    }
    @NonNull
    @Override
    public ChatTextModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_text_layout,parent,false);
        return new ChatTextModelHolder(view);
    }

    class ChatTextModelHolder extends RecyclerView.ViewHolder{
        LinearLayout me_layout,you_layout;
        TextView me,you;

        public ChatTextModelHolder(@NonNull View itemView) {
            super(itemView);
           me_layout = itemView.findViewById(R.id.chat_me_layout);
           me = itemView.findViewById(R.id.chat_text_me);
           you_layout = itemView.findViewById(R.id.chat_you_layout);
           you = itemView.findViewById(R.id.chat_text_you);
        }
    }
}
