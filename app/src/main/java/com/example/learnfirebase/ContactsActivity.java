package com.example.learnfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactsActivity extends AppCompatActivity {

    ListView listContacts;
    ArrayAdapter<String> adapter;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listContacts = (ListView)findViewById(R.id.lvContacts);
        listContacts.setAdapter(adapter);

        // Get a firebase's object
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Connect an object named is contacts (This node is defined by us in the Firebase)
        DatabaseReference databaseReference = database.getReference("contacts");
        // Access and listen for data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                // loop to get data when there is a change on firebase
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                    // Get data key
                    String key = data.getKey();
                    // Get key value (content)
                    String value = data.getValue().toString();
                    adapter.add(key + "\n" + value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "localPost: onCancelled", databaseError.toException());
            }
        });

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=adapter.getItem(position);
                String key=data.split("\n")[0];
                Intent intent=new Intent(ContactsActivity.this,EditContactActivity.class);
                intent.putExtra("KEY",key);
                startActivity(intent);
            }
        });

    }
}
