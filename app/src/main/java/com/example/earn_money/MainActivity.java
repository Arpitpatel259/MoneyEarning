package com.example.earn_money;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        topAnimation = AnimationUtils.loadAnimation ( this, R.anim.slide_down );
        bottomAnimation = AnimationUtils.loadAnimation ( this, R.anim.bottom_up );
        final TextView text1 = findViewById ( R.id.app_title );
        final TextView text2 = findViewById ( R.id.app_desc );
        text1.setAnimation ( topAnimation );
        text2.setAnimation ( bottomAnimation );
        new Handler( ).postDelayed ( () -> {
            Intent mainIntent = new Intent ( MainActivity.this, DashBoard.class );
            startActivity ( mainIntent );
            finish ( );
        }, 1200 );
    }
}