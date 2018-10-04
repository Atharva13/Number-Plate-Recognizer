package com.example.atharva.ocr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragment_manual extends Fragment {

    EditText e;

    TextView PersonName, NumberPlate, Address;

    DatabaseReference databasePerson;

    String sp;
    Button button;
    public static final String EXTRA_MESSAGE = "com.example.atharva.tryocr";


    private boolean checkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        return activenetwork != null && activenetwork.isConnectedOrConnecting();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.activity_user_info2,container,false);

        final Spinner mySpinner = view1.findViewById(R.id.spinner1);

        e =  view1.findViewById(R.id.editText1);

        button =  view1.findViewById(R.id.button1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
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
                    Toast.makeText(getActivity().getApplicationContext(),"Please Check your Internet Connection...",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getActivity(),User_Info.class);
                String data = n;
                intent.putExtra(EXTRA_MESSAGE,data);
                startActivity(intent);
            }
        });

        return view1;
    }
}
