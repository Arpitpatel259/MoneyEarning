package com.kc.earn_money;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Contact_Us_Page extends AppCompatActivity {
    ImageView ivBack;
    EditText name, email, msg;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton btnSend;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ImageView imageView = findViewById(R.id.imgViewBack1);
        this.ivBack = imageView;
        imageView.setOnClickListener(v -> finish());

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        msg = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {

            String Cname = name.getText().toString();
            String Cemail = email.getText().toString();
            String Cmsg = msg.getText().toString();

            if (name.getText().toString().isEmpty() && email.getText().toString().isEmpty() && msg.getText().toString().isEmpty()) {
                Toast.makeText(Contact_Us_Page.this, "Please Fill Up The Above Details", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(Cname)) {
                name.setError("Please Enter Name");
                name.requestFocus();
            }
            if (TextUtils.isEmpty(Cemail)) {
                email.setError("Please Enter Email");
                email.requestFocus();
            }
            if (TextUtils.isEmpty(Cmsg)) {
                msg.setError("Please Type Your Content");
                msg.requestFocus();
            } else {
                long tsLong = System.currentTimeMillis() / 1000;
                String ts = Long.toString(tsLong);

                Map<String, String> map = new HashMap<>();
                map.put("Customer Name", Cname);
                map.put("Customer Email", Cemail);
                map.put("Customer Query", Cmsg);
                database.getReference().child("Contact Us Queries").child(user.getUid()).child(ts).setValue(map);

                name.setText("");
                email.setText("");
                msg.setText("");
                Toast.makeText(Contact_Us_Page.this, "Thank you for contacting us. We've received your query and our team is looking into it. We do our best to respond to every email within 24 hours, so you should be hearing back from one of our customer service team members soon.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
