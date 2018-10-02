package com.example.atharva.ocr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class UserInfo2 extends AppCompatActivity {

    EditText e;

    TextView PersonName, NumberPlate, Address;

    DatabaseReference databasePerson;

    String sp;
    Button button;
    public static final String EXTRA_MESSAGE = "com.example.atharva.tryocr";


    private boolean checkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        return activenetwork != null && activenetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info2);

        final Spinner mySpinner = findViewById(R.id.spinner1);

        e = (EditText) findViewById(R.id.editText1);

        button = (Button) findViewById(R.id.button1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(UserInfo2.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 sp = parent.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(),"Selected" + sp,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databasePerson = FirebaseDatabase.getInstance().getReference();

        databasePerson.child("MH 24 AA 1111").setValue(new Person("TANMAY", "LATUR", "MH 24 AA 1111"));
        databasePerson.child("MH 34 BB 2222").setValue(new Person("SHIVANI", "CHANDRAPUR", "MH 34 BB 2222"));
        databasePerson.child("MH 12 CC 3333").setValue(new Person("ATHARVA", "PUNE", "MH 12 CC 3333"));
        databasePerson.child("MH 15 DD 4444").setValue(new Person("MOHIT", "NASHIK", "MH 15 DD 4444"));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkConnection())
                {
                    Toast.makeText(getApplicationContext(),"Please Check your Internet Connection...",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String n = new StringBuilder().append(sp).append(" ").append(e.getText().toString()).toString();

                /*databasePerson.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                            Person p = personSnapshot.getValue(Person.class);

                            if (p.car_number.equals(n)) {
                                PersonName.setText(p.getName());
                                Address.setText(p.getLocation());
                                NumberPlate.setText(p.getCar_number());
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            PersonName.setText("NOT FOUND");
                            Address.setText("NOT FOUND");
                            NumberPlate.setText("NOT FOUND");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                Intent intent = new Intent(UserInfo2.this,User_Info.class);
                String data = n;
                intent.putExtra(EXTRA_MESSAGE,data);
                startActivity(intent);
                recreate();
            }
        });

    }
}