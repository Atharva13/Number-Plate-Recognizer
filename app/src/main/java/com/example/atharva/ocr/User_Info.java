package com.example.atharva.ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class User_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getSupportActionBar().show();
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        final TextView PersonName = findViewById(R.id.name_from_db);
        final TextView Address = findViewById(R.id.AddressFromDb);
        final TextView NumberPlate = findViewById(R.id.number_from_scanner);

        final CardView cardView1 = findViewById(R.id.cardview1);
        final CardView cardView2 = findViewById(R.id.cardview2);
        final CardView cardView3 = findViewById(R.id.cardview3);

        cardView2.setVisibility(View.INVISIBLE);
        cardView3.setVisibility(View.INVISIBLE);

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
        final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);

        cardView1.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView2.setVisibility(View.VISIBLE);
                cardView2.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView3.setVisibility(View.VISIBLE);
                cardView3.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        DatabaseReference databasePerson;

        databasePerson = FirebaseDatabase.getInstance().getReference();

        databasePerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag=0;
                for(DataSnapshot personSnapshot : dataSnapshot.getChildren()){
                    Person p = personSnapshot.getValue(Person.class);

                    if(p.car_number.equals(message.trim())){
                        PersonName.setText(p.getName());
                        Address.setText(p.getLocation());
                        NumberPlate.setText(p.getCar_number());
                        flag=1;
                    }
                }
                if(flag==0){
                    PersonName.setText("NOT FOUND");
                    Address.setText("NOT FOUND");
                    NumberPlate.setText("NOT FOUND");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Capture the layout's TextView and set the string as its text

    }
}
