package com.example.cse3310project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TutoringAdapter extends RecyclerView.Adapter<TutoringAdapter.ViewHolder>{
    Context context;
    ArrayList<tutorpost> tutors;
    LayoutInflater mInflater;
    AlertDialog.Builder pop;
    AlertDialog dialog;

    public TutoringAdapter(Context context, ArrayList<tutorpost> tutors){
        this.context = context;
        this.tutors = tutors;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public TutoringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.tutoringpostformat, null);
        return new TutoringAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TutoringAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(tutors.get(position));
    }

    @Override
    public int getItemCount(){
        return tutors.size();
    }

    public void setItems(ArrayList<tutorpost> tutors)
    {
        this.tutors = tutors;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView Name, Email, Study, Schedule, Price;
        ImageButton message, email, rate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            Email = itemView.findViewById(R.id.Email);
            Study = itemView.findViewById(R.id.study);
            Schedule = itemView.findViewById(R.id.schedule);
            Price = itemView.findViewById(R.id.price);

            rate = itemView.findViewById(R.id.rate);
            message = itemView.findViewById(R.id.messagetutor);
            email = itemView.findViewById(R.id.emailtutor);

            rate.setOnClickListener(this);
            message.setOnClickListener(this);
            email.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.messagetutor:
                    break;
                case R.id.emailtutor:
                    break;
                case R.id.rate:
                    int pos = getLayoutPosition();
                    tutorpost t = tutors.get(pos);
                    rateTutor(t);
                    break;
            }
        }

        void bindData(final tutorpost post)
        {
            Name.setText(post.getName());
            Email.setText(post.getEmail());
            Study.setText(post.getField());
            Schedule.setText(post.getSchedule());
            Price.setText(post.getPrice());

            if(post.getRequest()){
                rate.setVisibility(View.INVISIBLE);
                rate.setClickable(false);
            } else {
                rate.setVisibility(View.VISIBLE);
                rate.setClickable(true);
            }
        }
    }

    public void rateTutor(tutorpost tutorpost) {
        pop = new AlertDialog.Builder(context);
        final View popupView = LayoutInflater.from(context).inflate(R.layout.ratingtutorpopup, null);

        ImageButton r1, r2, r3, r4, r5;

        r1 = (ImageButton) popupView.findViewById(R.id.r1);
        r2 = (ImageButton) popupView.findViewById(R.id.r2);
        r3 = (ImageButton) popupView.findViewById(R.id.r3);
        r4 = (ImageButton) popupView.findViewById(R.id.r4);
        r5 = (ImageButton) popupView.findViewById(R.id.r5);

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();
    }
}
