package com.kc.earn_money;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kc.earn_money.Utils.Constants;
import com.kc.earn_money.helper.coreHelper;

import java.util.HashMap;
import java.util.Map;

public class Withdraw_Money_Page extends AppCompatActivity {
    EditText UpiID, editText;
    AppCompatButton WithDraw;
    LinearLayout gPay, phonePay, Paytm;
    ImageView ivBack;
    SharedPreferences preferences;
    TextView tvWallet;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);  // Shared Preferences

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvWallet = findViewById(R.id.tvWallet);
        tvWallet.setText("₹ " + preferences.getString(Constants.KEY_MONEY, ""));

        editText = findViewById(R.id.edtAmount);
        editText.setText(tvWallet.getText());

        UpiID = findViewById(R.id.edtUpiID);

        phonePay = findViewById(R.id.llPhonePe);
        phonePay.setOnClickListener(v -> {
            UpiID.setHint("Enter Phone Pay UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        gPay = findViewById(R.id.llGPay);
        gPay.setOnClickListener(v -> {
            UpiID.setHint("Enter Google Pay UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        Paytm = findViewById(R.id.llPaytm);
        Paytm.setOnClickListener(v -> {
            UpiID.setHint("Enter Paytm UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        ivBack = findViewById(R.id.imgViewBack);
        ivBack.setOnClickListener(v -> {
            //startActivity(new Intent(Withdraw_Money_Page.this, DashBoard_Main_Page.class));
            finish();
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        WithDraw = findViewById(R.id.btnWithDraw);
        WithDraw.setOnClickListener(v -> {

            String upiId = UpiID.getText().toString();
            FirebaseDatabase.getInstance().getReference().child("WithDrawal").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("RequestMoney").getValue(boolean.class) != null && dataSnapshot.child("RequestMoney").getValue(boolean.class)) {
                        Toast.makeText(Withdraw_Money_Page.this, "Your Request Is Already Created Wait For Transaction.", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(upiId)) {
                        UpiID.setError("UPI ID cannot be empty");
                        UpiID.requestFocus();
                    } else {
                        long tsLong = System.currentTimeMillis() / 1000;
                        String ts = Long.toString(tsLong);

                        if (Integer.parseInt(preferences.getString(Constants.KEY_MONEY, "")) < 2500) {
                            editText.setError("Money Must Be Greater Than 2500");
                            Toast.makeText(Withdraw_Money_Page.this, "Money Must Be Greater Than 2500", Toast.LENGTH_SHORT).show();
                        } else {
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();

                            Map<String, String> map = new HashMap<>();
                            String key = database.getReference().child("WithDrawal").child(user.getUid()).child("Request").push().getKey();
                            String Transaction_id = coreHelper.generateTransactionId();

                            map.put("ID", key);
                            map.put("Sender_Name", account.getDisplayName());
                            map.put("Sender_Upi_Id", UpiID.getText().toString());
                            map.put("Receiver_Name", "Earn Money App");
                            map.put("Receiver_Upi_Id", Constants.MERCHANT_ID);
                            map.put("Transaction_Id", Transaction_id);
                            map.put("Paid_Status", "pending");

                            int Value = Integer.parseInt(preferences.getString(Constants.KEY_MONEY, ""));
                            int UpdateAmount = Value - 2500;
                            editor.putString(Constants.KEY_MONEY, String.valueOf(UpdateAmount));
                            editor.apply();

                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("total_amount", String.valueOf(UpdateAmount));
                            database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);

                            map.put("Receiver_Amount", String.valueOf(2500));
                            database.getReference().child("WithDrawal").child(user.getUid()).child("Request").child(key).setValue(map);

                            Map<String, Object> updateMap1 = new HashMap<>();
                            updateMap1.put("RequestMoney", true);
                            database.getReference().child("WithDrawal").child(user.getUid()).updateChildren(updateMap1);

                            Toast.makeText(Withdraw_Money_Page.this, "Your Request is created, Please Wait For Complete.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });
    }
}
