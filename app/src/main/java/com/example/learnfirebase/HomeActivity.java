package com.example.learnfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

public class HomeActivity extends AppCompatActivity {

    ImageView accountInformation, addContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Go to account information page
        accountInformation = (ImageView)findViewById(R.id.AccountInfromation);
        accountInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                Logger.getLogger("Test").warning("Move to Profile ");
                startActivity(intent);
            }
        });

        addContact = (ImageView)findViewById(R.id.AddContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddContactActivity.class);
                Logger.getLogger("Test").warning("Move to Add Contact ");
                startActivity(intent);
            }
        });
    }
}
