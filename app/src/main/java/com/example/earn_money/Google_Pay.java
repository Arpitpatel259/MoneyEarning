package com.example.earn_money;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Google_Pay extends AppCompatActivity {
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String status;
    Uri uri;

    private static boolean IsAppInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(Google_Pay.GOOGLE_PAY_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upi_id, String transaction, String amount) {
        return new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", upi_id).appendQueryParameter("pn", name).appendQueryParameter("tn", transaction).appendQueryParameter("an", amount).appendQueryParameter("cn", "INR").build();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payWithGpay();
    }

    private void payWithGpay() {
        if (IsAppInstalled(this)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(this.uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, this.GOOGLE_PAY_REQUEST_CODE);
            return;
        }
        Toast.makeText(this, "Please Install Google Pay First", Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            this.status = data.getStringExtra("Status").toLowerCase();
        }
        if (-1 != resultCode || !this.status.equals(FirebaseAnalytics.Param.SUCCESS)) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction Sucessful", Toast.LENGTH_SHORT).show();
        }
    }
}
