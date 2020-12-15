package com.example.learnfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends AppCompatActivity {

    EditText id, name, address, gender, email, officePhone, homePhone, mobilePhone;
    Button cancel,add;
    void Matching(){
        id = (EditText)findViewById(R.id.txtAddID);
        name = (EditText)findViewById(R.id.txtFullName);
        address = (EditText)findViewById(R.id.txtAddress);
        gender = (EditText)findViewById(R.id.txtGender);
        email = (EditText)findViewById(R.id.txtEmail);
        officePhone = (EditText)findViewById(R.id.txtOfficePhone);
        homePhone = (EditText)findViewById(R.id.txtHomePhone);
        mobilePhone = (EditText)findViewById(R.id.txtMobilePhone);

        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add = (Button) findViewById(R.id.btnAddContact);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContact(v);
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Matching();
    }

    void AddContact(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("contacts");
            String contactId=id.getText().toString();

            String sname = name.getText().toString();
            String saddress = address.getText().toString();
            String sgender = gender.getText().toString();
            String semail = email.getText().toString();
            String soffice = officePhone.getText().toString();
            String shome = homePhone.getText().toString();
            String smobile = mobilePhone.getText().toString();

            myRef.child(contactId).child("address").setValue(saddress);
            myRef.child(contactId).child("email").setValue(semail);
            myRef.child(contactId).child("name").setValue(sname);
            myRef.child(contactId).child("id").setValue(contactId);
            myRef.child(contactId).child("gender").setValue(sgender);
            myRef.child(contactId).child("phone").child("home").setValue(shome);
            myRef.child(contactId).child("phone").child("mobile").setValue(smobile);
            myRef.child(contactId).child("phone").child("office").setValue(soffice);
            finish();
        }
        catch (Exception ex) {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
