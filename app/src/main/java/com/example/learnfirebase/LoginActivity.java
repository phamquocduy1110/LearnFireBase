package com.example.learnfirebase;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    Dialog dialog;
    EditText userName, password;
    Button login, ok, close;
    ImageView positiveClose;
//    TextView register;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new Dialog(this);

        userName = (EditText) findViewById(R.id.txtUserName);
        password = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);
//        register = (TextView) findViewById(R.id.btnRegister_Login);

        // Connect to Firebase
        firebaseAuth = firebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = userName.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (Name.isEmpty()) {
                    userName.setError("Full name is required! Please input your name");
                    return;
                }

                if (Password.isEmpty()) {
                    password.setError("Password is required! Please input your password");
                    return;
                }

                if (Password.length() < 6) {
                    password.setError("Password must be >= 6 characters! Please input your password again");
                    return;
                }

                // Authenticate the user
                firebaseAuth.signInWithEmailAndPassword(Name,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dialog.setContentView(R.layout.activity_logged_in_successfully_message);
                            ok = (Button) dialog.findViewById(R.id.btnOK);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Logger.getLogger("Test").warning("Button ok is clicked");
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }
                            });
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();

//                            NotificationDialog mDialog = new NotificationDialog();
//                            mDialog.show(getSupportFragmentManager(), null);
//                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                            Logger.getLogger("Test").warning("logged in successfully");
                        }else {
                            dialog.setContentView(R.layout.activity_logged_in_failed_message);
                            close = (Button) dialog.findViewById(R.id.btnClose);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();                        }

                    }
                });
            }
        });

//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}

