package com.example.learnfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fullName, dateOfBirth, phoneNumber, email, inputPassword, re_enterPassword, currentAmount, address, nationalID;
    Button register, cancel;
    RadioButton male, female;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = (EditText)findViewById(R.id.txtFullName);
        dateOfBirth = (EditText)findViewById(R.id.txtDateOfBirth);
        phoneNumber = (EditText)findViewById(R.id.txtPhoneNumber);
        email = (EditText)findViewById(R.id.txtEmail);
        inputPassword = (EditText)findViewById(R.id.txtInputPassword);
        re_enterPassword = (EditText)findViewById(R.id.txtRe_enterPassword);
        currentAmount = (EditText)findViewById(R.id.txtCurrentAmount);
        address = (EditText)findViewById(R.id.txtAddress);
        nationalID = (EditText)findViewById(R.id.txtNationalID);

        register = (Button)findViewById(R.id.btnRegister);
        cancel = (Button)findViewById(R.id.btnCancel);

        male = (RadioButton)findViewById(R.id.rdMale);
        female = (RadioButton)findViewById(R.id.rdFemale);

        // Connect to Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Check if already have a account logged in
        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        // Click to register a account
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = fullName.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();
                String Re_password = re_enterPassword.getText().toString().trim();
                String Ammount = currentAmount.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Nationalid = nationalID.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();

                if (Name.isEmpty()) {
                    fullName.setError("Full name is required! Please input your name");
                    fullName.requestFocus();
                }

                else if (Email.isEmpty()) {
                    email.setError("Email is required! Please input your email address");
                    email.requestFocus();
                }

                else if (Password.isEmpty()) {
                    inputPassword.setError("Password is required! Please input your password");
                    inputPassword.requestFocus();
                }

                else if (Password.length() < 6) {
                    inputPassword.setError("Password must be >= 6 characters! Please input your password again");
                    inputPassword.requestFocus();
                }

                else  if (Re_password.isEmpty()) {
                    re_enterPassword.setError("This Re_password is required! Please input this again");
                    re_enterPassword.requestFocus();
                }

                else if (Re_password.length() != Password.length()) {
                    re_enterPassword.setError("This field must be same as password! Please input agian");
                    re_enterPassword.requestFocus();
                }
                else if (Ammount.isEmpty()) {
                    currentAmount.setError("Ammount is required! Please input an ammount you want");
                    currentAmount.requestFocus();
                }

                else {
                    Toast.makeText(RegisterActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

                // Create user account in CloudStore
                firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User account has been created.", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();

                            //Create database in CloudStore
                            DocumentReference documentReference = firestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName", Name);
                            user.put("email", Email);
                            user.put("password", Password);
                            user.put("re_password", "Re_password");
                            user.put("phone", phone);
                            user.put("ammount", Ammount);
                            user.put("address", Address);
                            user.put("nationalId", Nationalid);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Failed to create new user Profile" + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
