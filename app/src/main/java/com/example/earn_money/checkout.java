package com.example.earn_money;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.earn_money.Payment.PaymentsUtil;
import com.example.earn_money.Utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class checkout extends AppCompatActivity {

    PaymentsClient paymentsClient;
    Button googlePayButton;
    boolean available = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        googlePayButton = findViewById(R.id.googlePayBtn);

        //Configuration Of Google Pay
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
        paymentsClient = Wallet.getPaymentsClient(this, walletOptions);

        googlePayButton.setOnClickListener(v -> {
            Toast.makeText(checkout.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            requestPayment();
        });
    }

    //isReadytoPay()
    private void possiblyShowGooglePayButton() {

        final JSONObject isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (isReadyToPayJson == null) {
            return;
        }

        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                task1 -> {
                    if (task1.isSuccessful()) {
                        if (available) {
                            googlePayButton.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(checkout.this, "Google Pay Not", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.w("isReadyToPay failed", task1.getException());
                    }
                });
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    checkout.this, billingName,
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    public void requestPayment() {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        double price = 100;

        JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest((long) price);
        Log.d("MAster", "paymentDataRequestJson : " + paymentDataRequestJson.toString());
        if (paymentDataRequestJson == null) {
            return;
        }
        Log.d("MAster", "paymentDataRequestJson : " + paymentDataRequestJson);
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        Log.d("MAster", "paymentDataRequestJson : " + request.toJson());
        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this, 6000);

        }
        googlePayButton.setClickable(true);

    }

}