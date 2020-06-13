package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NewChatActivity extends AppCompatActivity {
    List<User> userlist;
    RecyclerView recyclerView ;
    RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        userlist = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager;


        recyclerView = findViewById(R.id.users_recycler_view);


        getSupportActionBar().setTitle("All Users");
        recyclerView.setHasFixedSize(true);
        Log.d("TAG", "Now should print mydata");
        Log.d("TAG", String.valueOf(userlist.size()));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(this,userlist,null);
        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnClick(NewChatActivity.this);
        readUserData();



    }




    public void readUserData(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Log.d("TAG","Hello Its ME");

        reference.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User Fbuser = null;
                NewChatActivity.this.userlist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getUid().equals(firebaseUser.getUid())){
                        NewChatActivity.this.userlist.add(user);
                        Log.d("TAG", user.getName());
                    }
                    else{
                         Fbuser = ds.getValue(User.class);
                    }
                }
                mAdapter = new MyAdapter(NewChatActivity.this,userlist,Fbuser);
                recyclerView.setAdapter(mAdapter);
//                mAdapter.setOnClick(NewChatActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error");
            }


        });



    }

//
//    @Override
//    public void onItemClick(int position) {
//
//
//    }
}




