package com.kc.earn_money;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kc.earn_money.Utils.Constants;

public class MainActivity extends AppCompatActivity {
    Animation topAnimation, bottomAnimation;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Merchantid").getValue(String.class) != null) {
                    Constants.MERCHANT_ID = dataSnapshot.child("Merchantid").getValue(String.class);
                }
                if (dataSnapshot.child("isAppUnderMaintainance").getValue(boolean.class)) {
                    startActivity(new Intent(MainActivity.this, Maintainance.class));
                    finish();
                } else {
                    topAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                    bottomAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_up);
                    final TextView text1 = findViewById(R.id.app_title);
                    final TextView text2 = findViewById(R.id.app_desc);
                    text1.setAnimation(topAnimation);
                    text2.setAnimation(bottomAnimation);
                    new Handler().postDelayed(() -> {
                        Intent mainIntent = new Intent(MainActivity.this, DashBoard.class);
                        startActivity(mainIntent);
                        finish();
                    }, 1200);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}