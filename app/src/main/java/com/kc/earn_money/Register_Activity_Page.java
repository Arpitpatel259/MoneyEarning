package com.kc.earn_money;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kc.earn_money.Utils.Constants;

public class Register_Activity_Page extends AppCompatActivity {

    LinearLayout llGoogle;
    FirebaseAuth auth, mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ImageView imageView;
    SharedPreferences preferences;
    EditText etRegEmail, etRegPassword, etRegName;
    Button btnRegister, btnloginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

        imageView = findViewById(R.id.imgViewBack3);
        imageView.setOnClickListener(v -> {
            startActivity(new Intent(Register_Activity_Page.this, Login_Activity_Page.class));
            finish();
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        etRegEmail = findViewById(R.id.etRegEmail);
        etRegName = findViewById(R.id.etRegName);
        etRegPassword = findViewById(R.id.etRegPass);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> createUser());
    }

    private void createUser() {
        mAuth = FirebaseAuth.getInstance();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String name = etRegName.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            etRegPassword.setError("Name cannot be empty");
            etRegPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constants.Email, etRegEmail.getText().toString());
                    editor.putString(Constants.NAME, etRegName.getText().toString());
                    editor.apply();

                    Toast.makeText(Register_Activity_Page.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register_Activity_Page.this, Login_Activity_Page.class));
                    finish();
                } else {
                    Toast.makeText(Register_Activity_Page.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}