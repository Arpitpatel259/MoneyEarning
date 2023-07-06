package com.example.earn_money;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.earn_money.Utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Withdraw extends AppCompatActivity {
    EditText Amount;
    String P_Amounts;
    String P_Name = "Kuldip";
    String P_Note = "Trasfer";
    String P_UpiID = "kuldipvekariya12@okhdfcak";
    String P_status;
    LinearLayout Paytm;
    EditText UpiID;
    AppCompatButton WithDraw;
    LinearLayout gPay;
    ImageView ivBack;
    LinearLayout phonePay;
    SharedPreferences preferences;
    TextView tvWallet;
    Uri uri;

    private static Uri getUpiPaymentUri(String name, String upi_id, String transaction, String amount) {
        return new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", upi_id).appendQueryParameter("pn", name).appendQueryParameter("tn", transaction).appendQueryParameter("an", amount).appendQueryParameter("cu", "INR").build();
    }

    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);  // Shared Preferences

        tvWallet = findViewById(R.id.tvWallet);
        tvWallet.setText("₹ " + preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));

        EditText editText = findViewById(R.id.edtAmount);
        Amount = editText;
        editText.setText(tvWallet.getText());

        UpiID = findViewById(R.id.edtUpiID);


        phonePay = findViewById(R.id.llPhonePe);
        phonePay.setOnClickListener(v -> {
            UpiID.setText("Enter Phone Pay Number/UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        gPay = findViewById(R.id.llGPay);
        gPay.setOnClickListener(v -> {
            UpiID.setText("Enter Google Pay Number/UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        Paytm = findViewById(R.id.llPaytm);
        Paytm.setOnClickListener(v -> {
            UpiID.setText("Enter Paytm Number/UPI ID");
            UpiID.setHintTextColor(getColor(R.color.red));
        });

        ivBack = findViewById(R.id.imgViewBack);
        ivBack.setOnClickListener(v -> {
            startActivity(new Intent(Withdraw.this, DashBoard.class));
            finish();
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        WithDraw = findViewById(R.id.btnWithDraw);
        WithDraw.setOnClickListener(v -> {
            P_Amounts = Amount.getText().toString();
            P_Name = account.getDisplayName();
            P_UpiID = UpiID.getText().toString();
            P_Note = "Transfer";
            if (!P_Amounts.isEmpty()) {
                uri = getUpiPaymentUri(P_Name, P_UpiID, P_Note, P_Amounts);
                payWithGpay();
                return;
            }
            Toast.makeText(Withdraw.this, "Payment Failed", Toast.LENGTH_SHORT).show();
        });
    }


    @SuppressLint("QueryPermissionsNeeded")
    private void payWithGpay() {
        Intent upiPayIntent = new Intent("android.intent.action.VIEW");
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            P_status = data.getStringExtra("Status").toLowerCase();
        }
        if (-1 != resultCode || !P_status.equals(FirebaseAnalytics.Param.SUCCESS)) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction Sucessful", Toast.LENGTH_SHORT).show();
        }
    }
}
