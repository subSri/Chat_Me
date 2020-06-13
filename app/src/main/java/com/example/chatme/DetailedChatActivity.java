package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

//import com.firebase.client.ChildEventListener;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
//import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.Map;



public class DetailedChatActivity extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_chat);

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        String send = null;
        User rec = null;
        Bundle extraData = getIntent().getExtras();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        send = currentUser.getUid();
        if (extraData != null) {

            rec = (User) extraData.getSerializable("ReceiverObj");
            getSupportActionBar().setTitle(rec.getName());

        }



//        Log.d("TAG", send.getName());
//        Log.d("TAG", rec.getName());
//        final  = (User) getIntent().getSerializableExtra("SenderObj");
//        User rec = (User) getIntent().getSerializableExtra("ReceiverObj");
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://mychatapp-42587.firebaseio.com/messages/" + send);
        reference2 = new Firebase("https://mychatapp-42587.firebaseio.com/messages/" + rec.getUid());


        final User finalRec = rec;
        final String finalSend = send;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("receiver", finalRec.getUid());
                    map.put("sender", finalSend);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });


        final User finalRec1 = rec;
        final String finalSend1 = send;
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userID = map.get("sender").toString();
                String recId = map.get("receiver").toString();
                String recnm = finalRec1.getUid();
                String usnm = finalSend1;
                if((recId.equals(recnm) && userID.equals(usnm)) || (recId.equals(usnm) && userID.equals(recnm))  ) {
                    if (userID.equals(usnm)) {
                        addMessageBox(message, 1);
                    } else {
                        addMessageBox(message, 2);
                    }
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }




        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(DetailedChatActivity.this);
        textView.setTextColor(Color.WHITE);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;


        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.inside);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.outside);
        }
        textView.setLayoutParams(lp2);


        layout.addView(textView);
        Space spc = new Space(this);
        spc.setMinimumHeight(20);

        spc.setLayoutParams(lp2);
        layout.addView(spc);



        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}

