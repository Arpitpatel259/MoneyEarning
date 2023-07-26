package com.kc.earn_money;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Register extends AppCompatActivity {

    LinearLayout llGoogle;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    ImageView imageView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        llGoogle = findViewById(R.id.llGoogle);
        imageView = findViewById(R.id.imgViewBack3);
        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

        imageView.setOnClickListener(v -> finish());

        llGoogle.setOnClickListener(v -> SignIn());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);
    }

    int RC_SIGN_IN = 40;

    private void SignIn() {
        progressDialog = new ProgressDialog(Register.this);
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
                Log.d("TAG", "onActivityResult: " + e.toString());
//                throw new RuntimeException(e);
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
                            Toast.makeText(Register.this, "Your Data Updated Successfully.", Toast.LENGTH_SHORT).show();
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

                startActivity(new Intent(Register.this, DashBoard.class));
                finish();
            } else {
                Toast.makeText(Register.this, "Error Detected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}