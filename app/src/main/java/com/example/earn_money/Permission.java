package com.example.earn_money;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Permission extends AppCompatActivity {

    TextView btnAgree;
    ImageView ivBack;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        btnAgree = findViewById(R.id.btnAgree);
        checkBox = findViewById(R.id.simpleCheckBox);

        btnAgree.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                startActivity(new Intent(Permission.this, Register.class));
            } else {
                Toast.makeText(this, "Agree The Privacy Policy in check box in", Toast.LENGTH_SHORT).show();
            }
        });

        ivBack = findViewById(R.id.imgViewBack2);
        ivBack.setOnClickListener(v -> startActivity(new Intent(Permission.this, DashBoard.class)));

    }
}