package com.example.chatme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.List;

import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;
import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<User> myData;
    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;


        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textview);

        }
    }


    //declare interface
//    private OnItemClicked onClick;
//
//    //make interface like this
//    public interface OnItemClicked {
//        void onItemClick(int position);
//    }
//    public void setOnClick(OnItemClicked onClick)
//    {
//        this.onClick=onClick;
//    }


    // Provide a suitable constructor (depends on the kind of dataset)
    private User fbuser;
    public MyAdapter(android.content.Context context,List<User> myDataset,User Fbuser) {
        this.myData = myDataset;
        this.context = context;
        this.fbuser = Fbuser;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_view, parent, false);

        MyViewHolder hold = new MyViewHolder(view);
        return hold;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
         final int selectedPos = RecyclerView.NO_POSITION;
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(this.myData.get(position).getName());
//        holder.itemView.setSelected(selectedPos == position);
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.onItemClick(position);
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                holder.itemView.setBackgroundColor(Color.GRAY);

    // Add selector

//                notifyItemChanged(selectedPos);
//                selectedPos = getLayoutPosition();
//                notifyItemChanged(selectedPos);
//                if (selectedItems.get(getAdapterPosition(), false)) {


//                holder.itemView.setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(context,DetailedChatActivity.class);
//                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                intent.putExtra("ReceiverObj", myData.get(position));
                context.startActivity(intent);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myData.size();
    }


}
