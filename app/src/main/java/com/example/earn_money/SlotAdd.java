package com.example.earn_money;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SlotAdd extends AppCompatActivity {
    public static String P_MSG = "Transfer";
    public static String P_Name = "Harsh Babaria";
    public static String P_Status;
    public static String P_Upi_ID = "harshbabaria4@okicici";
    double Discount = 0.1d;
    LinearLayout tv100Payment;
    TextView tv100spinPurchase;
    LinearLayout tv20Payment;
    TextView tv20spinPurchase;
    LinearLayout tv500Payment;
    TextView tv500spinPurchase;
    LinearLayout tv50Payment;
    TextView tv50spinPurchase;
    Uri uri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_add);

        tv20spinPurchase = findViewById(R.id.tv20spinPurchase);
        tv50spinPurchase = findViewById(R.id.tv50spinPurchase);
        tv100spinPurchase = findViewById(R.id.tv100spinPurchase);
        tv500spinPurchase = findViewById(R.id.tv500spinPurchase);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        P_Name = account.getDisplayName();

        tv20Payment = findViewById(R.id.tv20Payment);
        tv20Payment.setOnClickListener(v -> {

            int P_Amount20 = (int) (100 - (Discount * 100));
            uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, String.valueOf(P_Amount20));
            payWithGpay(String.valueOf(P_Amount20));
        });
/*
        tv50Payment = findViewById(R.id.tv50Payment);
        tv50Payment.setOnClickListener(v -> {
            String P_Amount50 = String.valueOf(Discount * 250);
            uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount50);
            payWithGpay(Integer.parseInt(P_Amount50));
        });

        tv100Payment = findViewById(R.id.tv100Payment);
        tv100Payment.setOnClickListener(v -> {
            String P_Amount100 = String.valueOf(Discount * 500);
            uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount100);
            payWithGpay(Integer.parseInt(P_Amount100));
        });

        tv500Payment = findViewById(R.id.tv500Payment);
        tv500Payment.setOnClickListener(v -> {
            String P_Amount500 = String.valueOf((Discount * 2500));
            uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount500);
            payWithGpay(Integer.parseInt(P_Amount500));
        });*/
    }

    private static Uri getUpiPaymentUri(String name, String upi_id, String transaction, String amount) {
        return new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", upi_id).appendQueryParameter("pn", name).appendQueryParameter("tn", transaction).appendQueryParameter("am", amount).appendQueryParameter("cu", "INR").build();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void payWithGpay(String UPI_PAYMENT) {
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, Integer.parseInt(UPI_PAYMENT));
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            P_Status = data.getStringExtra("Status").toLowerCase();
        }
        if (RESULT_OK == resultCode || P_Status.equals("SUCCESS")) {
            Toast.makeText(this, "Transaction Sucessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
