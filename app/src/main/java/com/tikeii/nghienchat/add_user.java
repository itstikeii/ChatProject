package com.tikeii.nghienchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.tikeii.nghienchat.Ultils.FireBaseUltil;
import com.tikeii.nghienchat.adapter.search_user_adapter;
import com.tikeii.nghienchat.model.UserModel;

public class add_user extends AppCompatActivity {

    EditText user_input;
    ImageButton back,search;
    RecyclerView recyclerView;
    search_user_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        user_input = findViewById(R.id.add_user_search);
        back = findViewById(R.id.add_user_back);
        search = findViewById(R.id.add_user_find);
        recyclerView = findViewById(R.id.add_user_search_recycle);

        user_input.requestFocus();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = user_input.getText().toString();
                if (user.isEmpty() || user.length()<3) {
                    user_input.setError("Vui lòng nhập từ 3 ký tự !");
                }
                recyclerViewData(user);
            }
        });


    }

    private void recyclerViewData(String user) {
        Query query = FireBaseUltil.all().whereGreaterThanOrEqualTo("name",user);
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();

        adapter = new search_user_adapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}