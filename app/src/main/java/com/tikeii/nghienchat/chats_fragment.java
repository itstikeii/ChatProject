package com.tikeii.nghienchat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.adapter.chat_recent_adapter;
import com.tikeii.nghienchat.adapter.search_user_adapter;
import com.tikeii.nghienchat.model.ChatroomModel;
import com.tikeii.nghienchat.model.UserModel;


public class chats_fragment extends Fragment {

    RecyclerView recyclerView;
    chat_recent_adapter adapter;
    public chats_fragment() {

    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chats_flagment, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recycle);
        setData_recycle();
        return view;
    }

    private void setData_recycle() {
        Query query = FireBaseUltil.allChatRoomReference().whereArrayContains("userID",FireBaseUltil.currentUserID())
                .orderBy("lastTextSentTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>().setQuery(query, ChatroomModel.class).build();

        //------------setAVT----------------

        adapter = new chat_recent_adapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}