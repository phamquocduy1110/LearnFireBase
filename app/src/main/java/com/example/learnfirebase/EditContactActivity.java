package com.example.learnfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class EditContactActivity extends AppCompatActivity {

    EditText id, name, address, gender, email, officePhone, homePhone, mobilePhone;
    Button cancel, update, delete;
    String key;

    void Matching() {
        id = (EditText) findViewById(R.id.txtAddID);
        name = (EditText) findViewById(R.id.txtFullName);
        address = (EditText) findViewById(R.id.txtAddress);
        gender = (EditText) findViewById(R.id.txtGender);
        email = (EditText) findViewById(R.id.txtEmail);
        officePhone = (EditText) findViewById(R.id.txtOfficePhone);
        homePhone = (EditText) findViewById(R.id.txtHomePhone);
        mobilePhone = (EditText) findViewById(R.id.txtMobilePhone);

        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update = (Button) findViewById(R.id.btnUpdateContact);
        delete = (Button) findViewById(R.id.btnDeleteContact);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Matching();
        getContactDetail();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là contacts
                // kết nối bản contacts
                DatabaseReference myRef = database.getReference("contacts");
                // lấy dữ liệu do người dùng nhập vào
                String contactId = key;
                String sname =name.getText().toString();
                String saddress =address.getText().toString();
                String sgender=gender.getText().toString();
                String semail =email.getText().toString();
                String soffice = officePhone.getText().toString();
                String shome = homePhone.getText().toString();
                String smobile = mobilePhone.getText().toString();
                // chèn các dữ liệu vào các cột cho phù hợp
                myRef.child(contactId).child("address").setValue(saddress);
                myRef.child(contactId).child("email").setValue(semail);
                myRef.child(contactId).child("name").setValue(sname);
                myRef.child(contactId).child("id").setValue(contactId);
                myRef.child(contactId).child("gender").setValue(sgender);
                myRef.child(contactId).child("phone").child("home").setValue(shome);
                myRef.child(contactId).child("phone").child("mobile").setValue(smobile);
                myRef.child(contactId).child("phone").child("office").setValue(soffice);
                finish(); // close activity
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
                DatabaseReference myRef = database.getReference("contacts");
                myRef.child(key).removeValue();
                finish();

            }
        });
    }

    private void getContactDetail() {
        // lấy key được truyền từ MainActivity
        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");
        // lấy dữ liệu từ firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // kết nối tới từng table trong db
        DatabaseReference myReference = database.getReference("contacts");
        //truy suất và lắng nghe sự thay đổi dữ liệu
        //chỉ truy suất node được chọn trên ListView myRef.child(key)
        //addListenerForSingleValueEvent để lấy dữ liệu đơn
        myReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    //trả về 1 DataSnapShot, mà giá trị đơn nên gọi getValue trả về 1 HashMap
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) dataSnapshot.getValue();
                    //HashMap này sẽ có kích  thước bằng số Node con bên trong node truy vấn
                    //mỗi phần tử có key là name được định nghĩa trong cấu trúc Json của Firebase
                    id.setText(key);
                    name.setText(hashMap.get("name").toString());
                    email.setText(hashMap.get("email").toString());
                    address.setText(hashMap.get("address").toString());
                    address.setText(hashMap.get("address").toString());
                    gender.setText(hashMap.get("gender").toString());
                    // lấy dữ liệu phần số điện thoại
                    HashMap phone = (HashMap) hashMap.get("phone");
                    homePhone.setText(phone.get("home").toString());
                    officePhone.setText(phone.get("office").toString());
                    mobilePhone.setText(phone.get("mobile").toString());

                } catch (Exception e) {
                    Log.d("LOI_JSON",e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
