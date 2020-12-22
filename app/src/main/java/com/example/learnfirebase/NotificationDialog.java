package com.example.learnfirebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.logging.Logger;
import java.util.zip.Inflater;

public class NotificationDialog extends AppCompatActivity {
//
    Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_successfully_message);

        ok = (Button)findViewById(R.id.btnOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDialog.this, HomeActivity.class);
                Logger.getLogger("Test").warning("Move to Homepage ");
                startActivity(intent);
            }
        });
    }
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_logged_in_successfully_message, container, false);
//        view.findViewById(R.id.btnOK).setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), HomeActivity.class));
//        });
//        return view;
//    }
}
