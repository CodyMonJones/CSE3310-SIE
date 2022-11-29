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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<contact> contactArrayList;
    ArrayList<contact> contactArrayListFull;
    RecyclerViewClickListener listener;

    public ContactAdapter(Context context, ArrayList<contact> contactArrayList, RecyclerViewClickListener listener){
        this.context = context;
        this.contactArrayList = contactArrayList;
        this.listener = listener;
        contactArrayListFull = new ArrayList<>(contactArrayList);
    }

    @Override
    public Filter getFilter() {
        return transactionsFilter;
    }

    private Filter transactionsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<contact> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(contactArrayListFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (contact item : contactArrayListFull){
                    if(item.getFname().toLowerCase().contains(filterPattern) || item.getLname().toLowerCase().contains(filterPattern)){
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
            contactArrayList.clear();
            contactArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.rowtextformat,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position){
        contact c = contactArrayList.get(position);
        holder.name.setText(c.toString());
    }

    @Override
    public int getItemCount(){
        return contactArrayList.size();
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
