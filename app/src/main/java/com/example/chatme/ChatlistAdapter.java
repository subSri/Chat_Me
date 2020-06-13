package com.example.chatme;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatlistAdapter extends RecyclerView.Adapter<ChatlistAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userchalist;
    HashMap<String,String> lastMessageMap;

    public ChatlistAdapter(Context context, ArrayList<User> myDataset) {
        lastMessageMap = new HashMap<>();
        this.userchalist = myDataset;
        this.context = context;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView namechatlist;
        public ImageView imageViewX;
        public TextView lastmessage;


        public MyViewHolder(View v) {
            super(v);
            namechatlist = v.findViewById(R.id.namechatlist);
            lastmessage = v.findViewById(R.id.lastmessage);
            imageViewX = v.findViewById(R.id.imageViewX);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_chatlist, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String hisUid = userchalist.get(position).getUid();
        String userName = userchalist.get(position).getName();
        String lastmessage = lastMessageMap.get(hisUid);
//        holder.imageViewX.setBackgroundResource(R.mipmap.ic_launcher_round);

        holder.namechatlist.setText(userName);
        if (lastmessage == null || lastmessage.equals("default")){
            holder.lastmessage.setVisibility(View.GONE);
        }
        else{
            holder.lastmessage.setVisibility(View.VISIBLE);

            holder.lastmessage.setText(lastmessage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedChatActivity.class);
//                intent.putExtra("SenderObj", currentUser.getUid());
                intent.putExtra("ReceiverObj", userchalist.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return userchalist.size();
    }




    public void setLastMessageMap(String userId, String lastMessage){
        lastMessageMap.put(userId, lastMessage);
    }


}


