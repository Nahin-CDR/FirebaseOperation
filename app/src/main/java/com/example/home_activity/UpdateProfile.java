package com.example.home_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {



    EditText fullName,fatherName,motherName,nidNumber,address;
    Button addFire;
    TextView displayView;
    private ValueEventListener eventListener;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference requestRf =  db.getReference("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        updateprofile();


    }

    private void updateprofile() {

        fullName =(EditText)findViewById(R.id.full_name_id);
        fatherName = (EditText)findViewById(R.id.father_name_id);
        motherName = (EditText)findViewById(R.id.mother_name_id);
        nidNumber = (EditText)findViewById(R.id.nid_id);
        address = (EditText)findViewById(R.id.address_id);

        displayView =(TextView)findViewById(R.id.display_id);

        addFire = (Button)findViewById(R.id.submit_button_id);
        addFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_fullname = fullName.getText().toString();
                String user_fathername = fatherName.getText().toString();
                String user_mothername = motherName.getText().toString();
                String user_nid_Number = nidNumber.getText().toString().trim();
                String user_address = address.getText().toString();

                if(!TextUtils.isEmpty(user_fullname) &&
                        !TextUtils.isEmpty(user_fathername) &&
                !TextUtils.isEmpty(user_mothername) &&
                !TextUtils.isEmpty(user_nid_Number) &&
                !TextUtils.isEmpty(user_address) && ( user_nid_Number.length()==10 ||
                        user_nid_Number.length()==17 )){

                    AddData sendData = new AddData(user_fullname,user_fathername,user_mothername,user_nid_Number,
                            user_address);

                    //push(); auto create key
                    requestRf.child(user_nid_Number).setValue(sendData);
                    Toast.makeText(UpdateProfile.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    fullName.setText("");
                    fatherName.setText("");
                    motherName.setText("");
                    nidNumber.setText("");
                    address.setText("");

                }


                else {
                    Toast.makeText(UpdateProfile.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    fullName.setText("");
                    fatherName.setText("");
                    motherName.setText("");
                    nidNumber.setText("");
                    address.setText("");

                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        eventListener = requestRf.orderByChild("nid_Number").equalTo("23456789764648844").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = "";
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    AddData addData = itemSnapshot.getValue(AddData.class);
                    String oName = addData.getFull_Name();
                    String nidNo = addData.getNID_Number();

                    data = data+"Full Name: "+oName+"\nNID: "+nidNo+"\n\n";
                }
                displayView.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
