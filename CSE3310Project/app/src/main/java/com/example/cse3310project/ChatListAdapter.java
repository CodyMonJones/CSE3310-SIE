package com.example.cse3310project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    Context context;
    ArrayList<chatroom> chatroomArrayList;
    ChatListAdapter.RecyclerViewClickListener listener;

    public ChatListAdapter(Context context, ArrayList<chatroom> chatroomArrayList, ChatListAdapter.RecyclerViewClickListener listener){
        this.context = context;
        this.chatroomArrayList = chatroomArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.rowtextformat,parent,false);
        return new ChatListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, int position){
        chatroom c = chatroomArrayList.get(position);
        holder.name.setText(c.getName());
    }

    @Override
    public int getItemCount(){
        return chatroomArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactfullname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int pos);
    }
}
