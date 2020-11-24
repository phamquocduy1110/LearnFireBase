package com.example.learnfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    EditText fullname, dateofbirth, phone, email, currentammount, address, nationalId, description;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullname = (EditText)findViewById(R.id.txtFullName);
        dateofbirth = (EditText)findViewById(R.id.txtDateOfBirth);
        phone = (EditText)findViewById(R.id.txtPhoneNumber);
        email = (EditText)findViewById(R.id.txtEmail);
        currentammount = (EditText)findViewById(R.id.txtCurrentAmount);
        address = (EditText)findViewById(R.id.txtAddress);
        nationalId = (EditText)findViewById(R.id.txtNationalID);
        description = (EditText)findViewById(R.id.txtDescription);

        save = (Button)findViewById(R.id.BtnSaveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}