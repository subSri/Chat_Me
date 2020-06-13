package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private DatabaseReference reference;
    private ArrayList<User> userList;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ChatlistAdapter adpaterchatlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle("Recent Chats");


         floatingActionButton = findViewById(R.id.floatingActionButton);

         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, NewChatActivity.class);
                 startActivity(intent);
             }
         });
//         User usr = new User();
//         ArrayList<User> dummy = new ArrayList<>();
//         dummy.add(usr);
         recyclerView = findViewById(R.id.chats_recycler_view);
//        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
//        adpaterchatlist =  new ChatlistAdapter(this, dummy);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adpaterchatlist);
        final List<ChatList> chatlist = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(currentUser.getUid());
        Log.d("entring","getting in chatlist");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatList chat = ds.getValue(ChatList.class);
                    chatlist.add(chat);

                }
                getChats(chatlist,currentUser);
//                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        public void lastMessage(final String uid, final ChatlistAdapter adpaterchatlist, final FirebaseUser currentUser){
            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("messages");
            reference1.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String lastMessage = "default";
                    Log.d("lastenter","Entering Last message");
                    ChatList chat = null;
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        for (DataSnapshot dss:ds.getChildren()) {
                             chat = dss.getValue(ChatList.class);
//                            Log.d("lastmsg",chat.getMessage());


                        if (chat==null){
                            continue;
                        }
                        String sender = chat.getSender();
//                        Log.d("Sender",sender);
                        String receiver = chat.getReceiver();
//                        Log.d("Receiver",receiver);
                        if (sender == null || receiver==null){
                            continue;
                        }
                        if (chat.getReceiver().equals(currentUser.getUid()) &&
                        chat.getSender().equals(uid) ||
                                (chat.getSender().equals(currentUser.getUid())) &&
                                        chat.getReceiver().equals(uid)) {
                            lastMessage = chat.getMessage();
                        }

//
                        }

                    }
                    adpaterchatlist.setLastMessageMap(uid, lastMessage);
                    adpaterchatlist.notifyDataSetChanged();




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //signed in
        } else {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                getActivity(this).finish();
        }
    }

    private void getChats(final List<ChatList> chatlist, final FirebaseUser currentUser){

        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {





            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                // data has all users
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    // data has all chats,receiver,sender in current User
                    for (ChatList clst : chatlist){
                        //
                        if (user.getUid() !=null && !user.getUid().equals(currentUser.getUid())){
                            if (user.getUid().equals(clst.getReceiver()) || user.getUid().equals(clst.getSender()))
                            {
//                                Log.d("recentuserlist",user.getName());
                                userList.add(user);
                                break;
                            }
                        }
                    }
                    adpaterchatlist =  new ChatlistAdapter(MainActivity.this, userList);
                    recyclerView.setAdapter(adpaterchatlist);

                     for (int i=0; i<userList.size(); i++) {
                         lastMessage(userList.get(i).getUid(),adpaterchatlist, currentUser);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
