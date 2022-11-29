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

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Email> emailArrayList;
    ArrayList<Email> emailArrayListFull;
    EmailAdapter.RecyclerViewClickListener listener;

    public EmailAdapter(Context context, ArrayList<Email> emailArrayList, EmailAdapter.RecyclerViewClickListener listener){
        this.context = context;
        this.emailArrayList = emailArrayList;
        this.listener = listener;
        emailArrayListFull = new ArrayList<>(emailArrayList);
    }

    @Override
    public Filter getFilter() {
        return transactionsFilter;
    }

    private Filter transactionsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Email> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(emailArrayListFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Email item : emailArrayListFull){
                    if(item.getEmail().getName().toLowerCase().contains(filterPattern)){
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
            emailArrayList.clear();
            emailArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public EmailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.rowtextformat,parent,false);
        return new EmailAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailAdapter.MyViewHolder holder, int position){
        Email e = emailArrayList.get(position);
        holder.name.setText(e.getEmail().getName());
    }

    @Override
    public int getItemCount(){
        return emailArrayList.size();
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
