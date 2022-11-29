package com.example.cse3310project;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

            rate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
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
                        User user = doc.toObject(User.class);
                        ArrayList<String> rates = user.getRatings();
                        if(rates != null && !rates.isEmpty()){
                            int sum = 0;
                            int num = 0;
                            for(String f : rates){
                                sum += Integer.parseInt(f);
                                num++;
                            }
                            float avg = sum/num;
                            namerate.setText(tutorpost.getName() + "(" + avg +")");
                        } else {
                            namerate.setText(tutorpost.getName() + "No current ratings");
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
                addRate(rate, tutorpost);
                dialog.dismiss();
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 2;
                addRate(rate, tutorpost);
                dialog.dismiss();
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 3;
                addRate(rate, tutorpost);
                dialog.dismiss();
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 4;
                addRate(rate, tutorpost);
                dialog.dismiss();
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = 5;
                addRate(rate, tutorpost);
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

    public void addRate(int rate, tutorpost post) {
        ff.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        User user = (dc.getDocument().toObject(User.class));
                        if(user.getUserID().equals(post.getPosterid())) {
                            if (user.getRatings() != null){
                                int[] rates = new int[user.getRatings().size()+1];
                                for(int x = 0; x< user.getRatings().size(); x++) {
                                        rates[x] = Integer.parseInt(user.getRatings().get(x));
                                }
                                rates[user.getRatings().size()] = rate;
                                ArrayList<String> rateTutor = new ArrayList<>();
                                for(int r : rates){
                                    rateTutor.add(Integer.toString(r));
                                }
                                ff.collection("Users").document(post.getPosterid()).update("ratings", rateTutor);
                            } else {
                                ArrayList<String> rateTutor = new ArrayList<>();
                                rateTutor.add(Integer.toString(rate));
                                ff.collection("Users").document(post.getPosterid()).update("ratings", rateTutor);
                            }
                        }
                    }
                }
            }
        });
    }
}
