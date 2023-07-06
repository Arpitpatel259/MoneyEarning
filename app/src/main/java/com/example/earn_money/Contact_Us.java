package com.example.earn_money;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Contact_Us extends AppCompatActivity {
    ImageView ivBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ImageView imageView = findViewById(R.id.imgViewBack1);
        this.ivBack = imageView;
        imageView.setOnClickListener(v -> Contact_Us.this.startActivity(new Intent(Contact_Us.this, DashBoard.class)));
    }
}
