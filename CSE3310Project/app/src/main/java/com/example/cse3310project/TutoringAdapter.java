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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class TutoringAdapter extends RecyclerView.Adapter<TutoringAdapter.ViewHolder>{
    Context context;
    ArrayList<tutorpost> tutors;
    AlertDialog.Builder pop;
    AlertDialog dialog;

    FirebaseFirestore ff = FirebaseFirestore.getInstance();

    public TutoringAdapter(Context context, ArrayList<tutorpost> tutors){
        this.context = context;
        this.tutors = tutors;
    }

    @NonNull
    @Override
    public TutoringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.tutoringpostformat, parent, false);
        return new TutoringAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutoringAdapter.ViewHolder holder, final int position)
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
            Email = itemView.findViewById(R.id.email);
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

        ImageButton r1, r2, r3, r4, r5, exit;
        TextView namerate;

        namerate = popupView.findViewById(R.id.name);

        ff.collection("Users").document(tutorpost.getPosterid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        int sum = 0;
                        int num = 0;
                        ArrayList<Integer> rates = doc.get("ratings", ArrayList.class);
                        if(!rates.isEmpty()){
                            for(float f : rates){
                                sum += f;
                                num++;
                            }
                            float avg = sum/num;
                            namerate.setText(tutorpost.getName() + "(" + avg +")");
                        } else {
                            namerate.setText(tutorpost.getName());
                        }
                    }
                }
            }
        });

        r1 = (ImageButton) popupView.findViewById(R.id.r1);
        r2 = (ImageButton) popupView.findViewById(R.id.r2);
        r3 = (ImageButton) popupView.findViewById(R.id.r3);
        r4 = (ImageButton) popupView.findViewById(R.id.r4);
        r5 = (ImageButton) popupView.findViewById(R.id.r5);
        exit = (ImageButton) popupView.findViewById(R.id.exit);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 1;
                ff.collection("User").document(tutorpost.getPosterid()).update("ratings", rate);
                dialog.dismiss();
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 2;
                ff.collection("User").document(tutorpost.getPosterid()).update("ratings", rate);
                dialog.dismiss();
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 3;
                ff.collection("User").document(tutorpost.getPosterid()).update("ratings", rate);
                dialog.dismiss();
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 4;
                ff.collection("User").document(tutorpost.getPosterid()).update("ratings", rate);
                dialog.dismiss();
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 5;
                ff.collection("User").document(tutorpost.getPosterid()).update("ratings", rate);
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();
    }
}
