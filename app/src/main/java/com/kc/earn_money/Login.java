package com.kc.earn_money;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kc.earn_money.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    ProgressDialog progressDialog;
    Button btnLogin;
    FirebaseAuth mAuth;
    TextView new_Account;
    SharedPreferences preferences;
    FirebaseDatabase database;
    LinearLayout llGoogle;
    FirebaseAuth auth;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        new_Account = findViewById(R.id.new_Account);
        new_Account.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        llGoogle = findViewById(R.id.llGoogle);
        llGoogle.setOnClickListener(v -> SignIn());

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constants.Email, etLoginEmail.getText().toString());
                    editor.putString(Constants.LoginData, "LoginWithEmailPassword");
                    editor.apply();

                    FirebaseDatabase.getInstance().getReference().child("Wallet_Data_Entries").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Email").getValue(String.class) != null) {
                                Toast.makeText(Login.this, "Your Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                                editor.putString(Constants.KEY_SPIN_20, dataSnapshot.child("20_Spin_Left").getValue(String.class));
                                editor.putString(Constants.KEY_SPIN_50, dataSnapshot.child("50_Spin_Left").getValue(String.class));
                                editor.putString(Constants.KEY_SPIN_100, dataSnapshot.child("100_Spin_Left").getValue(String.class));
                                editor.putString(Constants.KEY_SPIN_500, dataSnapshot.child("500_Spin_Left").getValue(String.class));
                                editor.putString(Constants.KEY_MONEY, dataSnapshot.child("total_amount").getValue(String.class));
                                editor.apply();
                            } else {
                                Map<String, String> map = new HashMap<>();
                                map.put("Email", preferences.getString("email", ""));
                                map.put("20_Spin_Left", "5");
                                map.put("50_Spin_Left", "0");
                                map.put("100_Spin_Left", "0");
                                map.put("500_Spin_Left", "0");
                                map.put("total_amount", "0");
                                database.getReference().child("Wallet_Data_Entries").child(user.getUid()).setValue(map);
                                editor.putString(Constants.KEY_SPIN_20, "5");
                                editor.putString(Constants.KEY_SPIN_50, "0");
                                editor.putString(Constants.KEY_SPIN_100, "0");
                                editor.putString(Constants.KEY_SPIN_500, "0");
                                editor.putString(Constants.KEY_MONEY, "0");
                                editor.apply();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("TAG", "onCancelled: " + error);
                        }
                    });

                    editor.putString(Constants.Email, etLoginEmail.getText().toString());
                    editor.apply();
                    Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, DashBoard.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "loginUser: " + task.getException().getMessage());
                }
            });
        }
    }

    int RC_SIGN_IN = 40;

    private void SignIn() {
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN/* && data != null && data.getData() != null*//* && requestCode == Activity.RESULT_OK*/) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                Log.d("TAG", "onActivityResult: " + e);
            }

        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.Email, user.getEmail());
                editor.putString(Constants.KEY_USERNAME, user.getDisplayName());
                editor.apply();

                FirebaseDatabase.getInstance().getReference().child("Wallet_Data_Entries").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Email").getValue(String.class) != null) {
                            Toast.makeText(Login.this, "Your Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                            editor.putString(Constants.KEY_SPIN_20, dataSnapshot.child("20_Spin_Left").getValue(String.class));
                            editor.putString(Constants.KEY_SPIN_50, dataSnapshot.child("50_Spin_Left").getValue(String.class));
                            editor.putString(Constants.KEY_SPIN_100, dataSnapshot.child("100_Spin_Left").getValue(String.class));
                            editor.putString(Constants.KEY_SPIN_500, dataSnapshot.child("500_Spin_Left").getValue(String.class));
                            editor.putString(Constants.KEY_MONEY, dataSnapshot.child("total_amount").getValue(String.class));
                            editor.apply();
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("Email", preferences.getString("email", ""));
                            map.put("20_Spin_Left", "5");
                            map.put("50_Spin_Left", "0");
                            map.put("100_Spin_Left", "0");
                            map.put("500_Spin_Left", "0");
                            map.put("total_amount", "0");
                            database.getReference().child("Wallet_Data_Entries").child(user.getUid()).setValue(map);
                            editor.putString(Constants.KEY_SPIN_20, "5");
                            editor.putString(Constants.KEY_SPIN_50, "0");
                            editor.putString(Constants.KEY_SPIN_100, "0");
                            editor.putString(Constants.KEY_SPIN_500, "0");
                            editor.putString(Constants.KEY_MONEY, "0");
                            editor.apply();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                startActivity(new Intent(Login.this, DashBoard.class));
                finish();
            } else {
                Toast.makeText(Login.this, "Error Detected", Toast.LENGTH_SHORT).show();
            }
        });
    }

}