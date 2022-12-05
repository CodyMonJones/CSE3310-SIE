package com.example.cse3310project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> implements Filterable {
    Context context;
    static ArrayList<chatroom> chatroomArrayList;
    static ArrayList<chatroom> chatroomArrayListFull;
    ChatListAdapter.RecyclerViewClickListener listener;

    public ChatListAdapter(Context context, ArrayList<chatroom> chatroomArrayList, ChatListAdapter.RecyclerViewClickListener listener){
        this.context = context;
        this.chatroomArrayList = chatroomArrayList;
        this.listener = listener;
        chatroomArrayListFull = new ArrayList<>(chatroomArrayList);
    }

    @Override
    public Filter getFilter() {
        return transactionsFilter;
    }

    private Filter transactionsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<chatroom> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(chatroomArrayListFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (chatroom item : chatroomArrayListFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            chatroomArrayList.clear();
            chatroomArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

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
