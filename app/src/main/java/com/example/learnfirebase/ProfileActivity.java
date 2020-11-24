package com.example.learnfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.logging.Logger;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    EditText Profile_fullname, Profile_dateofbirth, Profile_phone, Profile_email, Profile_currentammount, Profile_address, Profile_nationalId, Profile_description;
    Button Profile_save, Profile_cancel;
    ImageView Profile_imageAvatar;

    String userId;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Profile_fullname = (EditText)findViewById(R.id.txtProfileName);
        Profile_dateofbirth = (EditText)findViewById(R.id.txtProfileBirthdate);
        Profile_phone = (EditText)findViewById(R.id.txtProfilePhone);
        Profile_email = (EditText)findViewById(R.id.txtProfileEmail);
        Profile_currentammount = (EditText)findViewById(R.id.txtProfileAmount);
        Profile_address = (EditText)findViewById(R.id.txtProfileAddress);
        Profile_nationalId = (EditText)findViewById(R.id.txtProfileNationalID);
        Profile_description = (EditText)findViewById(R.id.txtProfileDescription);

        Profile_imageAvatar = (ImageView)findViewById(R.id.ivProfileAvatar);
        Profile_save = (Button)findViewById(R.id.btnProfileSave);
        Profile_cancel = (Button)findViewById(R.id.btnProfileCancelButton);

//        Intent data = getIntent();
//        final String fullName = data.getStringExtra("Name");
//        String dateofBirth = data.getStringExtra("DateofBirth");
//        String phone = data.getStringExtra("Phone");
//        String Email = data.getStringExtra("Email");
//        String currentAmount = data.getStringExtra("CurrentAmount");
//        String address = data.getStringExtra("Address");
//        String nationalid = data.getStringExtra("NationalID");
//        String description = data.getStringExtra("Description");

        // Connect to  Firebase's Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        // Connect to  Firebase's FireStore
        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+ firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Profile_imageAvatar);
            }
        });

        // Load data from Firebase back to system
        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Profile_fullname.setText(documentSnapshot.getString("fName"));
                    Logger.getLogger("Test birthdate").warning(documentSnapshot.getString("birthdate"));
                    Profile_dateofbirth.setText(documentSnapshot.getString("birthdate"));
                    Profile_phone.setText(documentSnapshot.getString("phone"));
                    Profile_email.setText(documentSnapshot.getString("email"));
                    Profile_currentammount.setText(documentSnapshot.getString("ammount"));
                    Profile_address.setText(documentSnapshot.getString("address"));
                    Profile_nationalId.setText(documentSnapshot.getString("nationalId"));
                    Profile_description.setText(documentSnapshot.getString("description"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        // Edit account's information
        Profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Profile_fullname.getText().toString().isEmpty() || Profile_email.getText().toString().isEmpty() || Profile_phone.getText().toString().isEmpty()){
                    Toast.makeText(ProfileActivity.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String Email = Profile_email.getText().toString();
                firebaseUser.updateEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = firestore.collection("users").document(firebaseUser.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("fName",Profile_fullname.getText().toString());
                        edited.put("birthdate",Profile_dateofbirth.getText().toString());
                        edited.put("phone",Profile_phone.getText().toString());
                        edited.put("email",Email);
                        edited.put("ammount", Profile_currentammount.getText().toString());
                        edited.put("address",Profile_address.getText().toString());
                        edited.put("nationalId", Profile_nationalId.getText().toString());
                        edited.put("description", Profile_description.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}