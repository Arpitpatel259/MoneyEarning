package com.kc.earn_money;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

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

public class SpinWheel extends AppCompatActivity {
    int lastSpinValueIndex1, lastSpinValueIndex2, lastSpinValueIndex3, lastSpinValueIndex4;
    int randomSectorIndex1 = 0, randomSectorIndex2 = 0, randomSectorIndex3 = 0, randomSectorIndex4 = 0;
    private static final int[] sectors = {1, 2, 3, 4, 5, 0};
    private static final int[] sectors50 = {1, 2, 3, 4, 5, 0};
    private static final int[] sectors100 = {1, 2, 3, 4, 5, 0};
    private static final int[] sectors500 = {1, 2, 3, 4, 5, 0};
    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 1;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    private static final int[] sectorDegrees = new int[sectors.length];
    private static final int[] sectorDegrees50 = new int[sectors50.length];
    private static final int[] sectorDegrees100 = new int[sectors100.length];
    private static final int[] sectorDegrees500 = new int[sectors500.length];
    private boolean is100Select = false, is20Select = false, is500Select = false, is50Select = false, isSpinning = false;
    private int GlobalValue = 0;
    int SpinCounter100, SpinCounter20, SpinCounter50, SpinCounter500, earningRecord, lastValue;
    public static String P_MSG = "Spin Purchase";
    public static String P_Name = "Vimarsh Raiyani";
    String status;
    public static String P_Upi_ID = Constants.MERCHANT_ID;
    double Discount = 0.1d;
    ImageView Wheel;
    FirebaseAuth auth;
    AppCompatButton btnPlayNow;
    FirebaseDatabase database;
    Dialog dialog;
    ImageView imgViewBack4;
    String Transaction_id;
    LinearLayout ll100App, ll20App, ll500App, ll50App, llWithdrawMoney;
    TextView tvFiftySpinLeft, tvFiveHundredSpinLeft, tvHundredSpinLeft, tvTwentySpinLeft, tvWithdrawMoney;
    SharedPreferences preferences;
    FirebaseUser user;
    MediaPlayer mediaPlayer;
    Uri uri;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);  // Shared Preferences

        SpinCounter20 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_20, "5"));
        SpinCounter50 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_50, "0"));
        SpinCounter100 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_100, "0"));
        SpinCounter500 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_500, "0"));

        lastSpinValueIndex1 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_LAST_20, "0"));
        lastSpinValueIndex2 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_LAST_50, "0"));
        lastSpinValueIndex3 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_LAST_100, "0"));
        lastSpinValueIndex4 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_LAST_500, "0"));

        getDgreeForSector();
        getDgreeForSector50();
        getDgreeForSector100();
        getDgreeForSector500();

        dialog = new Dialog(this);

        earningRecord = Integer.parseInt(preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));

        tvWithdrawMoney = findViewById(R.id.tvWithdrawMoney);
        tvWithdrawMoney.setText("₹ " + preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));

        Wheel = findViewById(R.id.imgWheel);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvTwentySpinLeft = findViewById(R.id.tvTwoSpinLeft);
        tvFiftySpinLeft = findViewById(R.id.tvFiveSpinLeft);
        tvHundredSpinLeft = findViewById(R.id.tvOneSpinLeft);
        tvFiveHundredSpinLeft = findViewById(R.id.tvFiveZeroSpinLeft);

        tvTwentySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_20, Constants.KEY_MONEY) + " Spin Left");
        tvFiftySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_50, Constants.KEY_MONEY) + " Spin Left");
        tvHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_100, Constants.KEY_MONEY) + " Spin Left");
        tvFiveHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_500, Constants.KEY_MONEY) + " Spin Left");

        llWithdrawMoney = findViewById(R.id.llWithdrawMoney);
        llWithdrawMoney.setOnClickListener(v -> {
            startActivity(new Intent(SpinWheel.this, Withdraw.class));
            finish();
        });

        ll20App = findViewById(R.id.ll20App);
        ll20App.setOnClickListener(v -> {
            if (SpinCounter20 == 0) {
                dialog.setContentView(R.layout.activity_slot_add);
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(true);

                LinearLayout b = dialog.findViewById(R.id.tv20Payment);
                b.setOnClickListener(v1 -> {
                    String P_Amount20 = String.valueOf(100 - (Discount * 100));
                    // uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount20);
                    //payWithGPay();
                    initiateUpiPayment(P_Name, P_Upi_ID, P_MSG, P_Amount20);
                    GlobalValue = 20;
                });

                dialog.show();
            }
            ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is50Select = false;
            ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is100Select = false;
            ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is500Select = false;

            ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_selected, null));
            is20Select = true;
        });

        ll50App = findViewById(R.id.ll50App);
        ll50App.setOnClickListener(v -> {
            if (SpinCounter50 == 0) {
                dialog.setContentView(R.layout.activity_slot_add);
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(true);

                LinearLayout b = dialog.findViewById(R.id.tv50Payment);
                b.setOnClickListener(v13 -> {
                    String P_Amount50 = String.valueOf(250 - (Discount * 250));
                    uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount50);
                    payWithGPay();
                    GlobalValue = 50;
                });
                dialog.show();
            }
            ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is20Select = false;
            ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is100Select = false;
            ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is500Select = false;

            ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_selected, null));
            is50Select = true;
        });

        ll100App = findViewById(R.id.ll100App);
        ll100App.setOnClickListener(v -> {
            if (SpinCounter100 == 0) {
                dialog.setContentView(R.layout.activity_slot_add);
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(true);

                LinearLayout b = dialog.findViewById(R.id.tv100Payment);
                b.setOnClickListener(v14 -> {
                    String P_Amount100 = String.valueOf(500 - (Discount * 500));
                    uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount100);
                    payWithGPay();
                    GlobalValue = 100;
                });
                dialog.show();
            }
            ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is50Select = false;
            ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is20Select = false;
            ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is500Select = false;

            ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_selected, null));
            is100Select = true;
        });

        ll500App = findViewById(R.id.ll500App);
        ll500App.setOnClickListener(v -> {
            if (SpinCounter500 == 0) {
                dialog.setContentView(R.layout.activity_slot_add);
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(true);

                LinearLayout b = dialog.findViewById(R.id.tv500Payment);
                b.setOnClickListener(v12 -> {

                    String P_Amount500 = String.valueOf(2500 - (Discount * 2500));
                    uri = getUpiPaymentUri(P_Name, P_Upi_ID, P_MSG, P_Amount500);
                    payWithGPay();
                    GlobalValue = 500;
                });
                dialog.show();
            }
            ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is50Select = false;
            ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is100Select = false;
            ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
            is20Select = false;

            ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_selected, null));
            is500Select = true;
        });

        imgViewBack4 = findViewById(R.id.imgViewBack4);
        imgViewBack4.setOnClickListener(v -> finish());

        btnPlayNow = findViewById(R.id.btnPlayNow);
        btnPlayNow.setOnClickListener(v -> {
            if (!is20Select && !is50Select && !is100Select && !is500Select) {
                ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                Toast.makeText(SpinWheel.this, "Please Select Amount", Toast.LENGTH_SHORT).show();
            } else if (is20Select && !isSpinning) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ui_sound1);
                mediaPlayer.start();
                spinWheel(20);
                isSpinning = true;
            } else if (is50Select && !isSpinning) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ui_sound);
                mediaPlayer.start();
                spinWheel(50);
                isSpinning = true;
            } else if (is100Select && !isSpinning) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ui_sound1);
                mediaPlayer.start();
                spinWheel(100);
                isSpinning = true;
            } else if (is500Select && !isSpinning) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ui_sound);
                mediaPlayer.start();
                spinWheel(500);
                isSpinning = true;
            }
        });
    }

    private void spinWheel(int CashValue) {
        if (is20Select && SpinCounter20 > 0) {
            SpinCounter20--;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.KEY_SPIN_20, String.valueOf(SpinCounter20));
            editor.apply();

            UpdateSpinData20(CashValue);
        } else if (is50Select && SpinCounter50 > 0) {
            SpinCounter50--;

            SharedPreferences.Editor editor2 = preferences.edit();
            editor2.putString(Constants.KEY_SPIN_50, String.valueOf(SpinCounter50));
            editor2.apply();

            UpdateSpinData50(CashValue);
        } else if (is100Select && SpinCounter100 > 0) {
            SpinCounter100--;

            SharedPreferences.Editor editor3 = preferences.edit();
            editor3.putString(Constants.KEY_SPIN_100, String.valueOf(SpinCounter100));
            editor3.apply();

            UpdateSpinData100(CashValue);
        } else if (is500Select && SpinCounter500 > 0) {
            SpinCounter500--;

            SharedPreferences.Editor editor4 = preferences.edit();
            editor4.putString(Constants.KEY_SPIN_500, String.valueOf(SpinCounter500));
            editor4.apply();

            UpdateSpinData500(CashValue);
        } else {
            Toast.makeText(this, "You Have Not Any Spin", Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveEarning(int cashValue, int spinValue) {
        earningRecord += cashValue * spinValue;
        lastValue = cashValue * spinValue;
    }

    private int generateRandomDegree20() {
        return (sectors.length * 360) + sectorDegrees[randomSectorIndex1];
    }

    private int generateRandomDegree50() {
        return (sectors50.length * 360) + sectorDegrees50[5 - (Constants.Spin_set_50.get(lastSpinValueIndex2))];
    }

    private int generateRandomDegree100() {
        return (sectors100.length * 360) + sectorDegrees100[5 - (Constants.Spin_set_100.get(lastSpinValueIndex3))];
    }

    private int generateRandomDegree500() {
        return (sectors500.length * 360) + sectorDegrees500[5 - (Constants.Spin_set_500.get(lastSpinValueIndex4))];
    }

    private void getDgreeForSector() {
        int sectorDegree = 360 / sectors.length;
        for (int i = 0; i < sectors.length; i++) {
            sectorDegrees[i] = (i + 1) * sectorDegree;
        }
    }

    private void getDgreeForSector50() {
        int sectorDegree1 = 360 / sectors50.length;
        for (int i = 0; i < sectors50.length; i++) {
            sectorDegrees50[i] = (i + 1) * sectorDegree1;
        }
    }

    private void getDgreeForSector100() {
        int sectorDegree2 = 360 / sectors100.length;
        for (int i = 0; i < sectors100.length; i++) {
            sectorDegrees100[i] = (i + 1) * sectorDegree2;
        }
    }

    private void getDgreeForSector500() {
        int sectorDegree3 = 360 / sectors500.length;
        for (int i = 0; i < sectors500.length; i++) {
            sectorDegrees500[i] = (i + 1) * sectorDegree3;
        }
    }

    @SuppressLint("SetTextI18n")
    public void UpdateData(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_MONEY, String.valueOf(earningRecord));
        editor.apply();

        FirebaseUser user = auth.getCurrentUser();
        Map<String, String> map = new HashMap<>();

        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);

        map.put("Email_id", preferences.getString("email", ""));

        if (is20Select && SpinCounter20 > 0) {
            map.put("Spin_Left_20", Constants.KEY_SPIN_20);
        } else if (is50Select && SpinCounter50 > 0) {
            map.put("Spin_Left_50", Constants.KEY_SPIN_50);
        } else if (is100Select && SpinCounter100 > 0) {
            map.put("Spin_Left_100", Constants.KEY_SPIN_100);
        } else if (is500Select && SpinCounter500 > 0) {
            map.put("Spin_Left_500", Constants.KEY_SPIN_500);
        }

        map.put("Spin_Left", preferences.getString(Constants.KEY_SPIN_20, ""));
        map.put("Win_Amount", String.valueOf(lastValue));
        map.put("Total_Amount", preferences.getString(Constants.KEY_MONEY, ""));
        map.put("TimeStamp", ts);

        database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Win_Amount").child(ts).setValue(map);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("20_Spin_Left", preferences.getString(Constants.KEY_SPIN_20, "0"));
        updateMap.put("50_Spin_Left", preferences.getString(Constants.KEY_SPIN_50, "0"));
        updateMap.put("100_Spin_Left", preferences.getString(Constants.KEY_SPIN_100, "0"));
        updateMap.put("500_Spin_Left", preferences.getString(Constants.KEY_SPIN_500, "0"));
        updateMap.put("total_amount", preferences.getString(Constants.KEY_MONEY, "0"));
        database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);

        tvWithdrawMoney.setText("₹ " + preferences.getString(Constants.KEY_MONEY, ""));
        if (is20Select) {
            tvTwentySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_20, "") + " Spin Left");
        } else if (is50Select) {
            tvFiftySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_50, "") + " Spin Left");
        } else if (is100Select) {
            tvHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_100, "") + " Spin Left");
        } else if (is500Select) {
            tvFiveHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_500, "") + " Spin Left");
        }
        is20Select = false;
        is50Select = false;
        is100Select = false;
        is500Select = false;
        dialog.dismiss();
    }

    @SuppressLint("SetTextI18n")
    public void CancleData(View view) {

        if (is20Select) {
            tvTwentySpinLeft.setText(SpinCounter20 + " Spin Left");
        } else if (is50Select && !isSpinning) {
            tvFiftySpinLeft.setText(SpinCounter50 + " Spin Left");
        } else if (is100Select && !isSpinning) {
            tvHundredSpinLeft.setText(SpinCounter100 + " Spin Left");
        } else if (is500Select && !isSpinning) {
            tvFiveHundredSpinLeft.setText(SpinCounter500 + " Spin Left");
        }
        is20Select = false;
        is50Select = false;
        is100Select = false;
        is500Select = false;
        isSpinning = false;
        dialog.dismiss();
    }

    //20   53421 = 60+80+40+20+100 = 300
    //50   34210 = 50+100+200+150 = 500
    //100  30210 = 100+200+300 = 600
    //500  00210 = 500+1000 = 1500

    private void UpdateSpinData20(final int CashValue) {
        Constants.Spin_set_20.add(1);
        Constants.Spin_set_20.add(3);
        Constants.Spin_set_20.add(2);
        Constants.Spin_set_20.add(4);
        Constants.Spin_set_20.add(5);
        randomSectorIndex1 = Constants.Spin_set_20.get(lastSpinValueIndex1);//random.nextInt(sectors.length);

        int degrees = generateRandomDegree20();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Wheel, View.ROTATION, 0.0f, degrees);
        animator.setDuration(3600);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("SetTextI18n")
            public void onAnimationEnd(Animator animation) {

                int winValue = sectors[sectors.length - (randomSectorIndex1 + 1)];
                lastSpinValueIndex1 = lastSpinValueIndex1 + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.KEY_SPIN_LAST_20, String.valueOf(lastSpinValueIndex1));
                editor.apply();
                if (winValue == 0) {
                    dialog.setContentView(R.layout.lose_value);
                    mediaPlayer.stop();
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    createNotificationChannel();
                    showNotification((winValue * CashValue));

                } else {
                    mediaPlayer.stop();
                    SaveEarning(CashValue, winValue);
                    dialog.setContentView(R.layout.win_value);
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    showNotification((winValue * CashValue));
                    createNotificationChannel();

                }
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(false);
                dialog.show();

                isSpinning = false;
            }
        });
        isSpinning = false;
        animator.start();
    }

    private void UpdateSpinData50(final int CashValue) {
        Constants.Spin_set_50.add(2);//3
        Constants.Spin_set_50.add(3);//4
        Constants.Spin_set_50.add(1);//2
        Constants.Spin_set_50.add(0);//1
        Constants.Spin_set_50.add(5);//0
        randomSectorIndex2 = Constants.Spin_set_50.get(lastSpinValueIndex2);//random.nextInt(sectors.length);

        int degrees = generateRandomDegree50();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Wheel, View.ROTATION, 0.0f, degrees);
        animator.setDuration(3600);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("SetTextI18n")
            public void onAnimationEnd(Animator animation) {

                int winValue = sectors50[Constants.Spin_set_50.get(lastSpinValueIndex2)];
                lastSpinValueIndex2 = (lastSpinValueIndex2 + 1) % Constants.Spin_set_50.size();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.KEY_SPIN_LAST_50, String.valueOf(lastSpinValueIndex2));
                editor.apply();
                if (winValue == 0) {
                    dialog.setContentView(R.layout.lose_value);
                    mediaPlayer.stop();
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    createNotificationChannel();
                    showNotification((winValue * CashValue));
                } else {
                    mediaPlayer.stop();
                    SaveEarning(CashValue, winValue);
                    dialog.setContentView(R.layout.win_value);
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    showNotification((winValue * CashValue));
                    createNotificationChannel();

                }
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(false);
                dialog.show();

                isSpinning = false;
            }
        });
        isSpinning = false;
        animator.start();
    }

    private void UpdateSpinData100(final int CashValue) {
        Constants.Spin_set_100.add(0);//1
        Constants.Spin_set_100.add(2);//3
        Constants.Spin_set_100.add(1);//2
        Constants.Spin_set_100.add(5);//0
        Constants.Spin_set_100.add(5);//0
        randomSectorIndex3 = Constants.Spin_set_100.get(lastSpinValueIndex3);//random.nextInt(sectors.length);

        int degrees = generateRandomDegree100();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Wheel, View.ROTATION, 0.0f, degrees);
        animator.setDuration(3600);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("SetTextI18n")
            public void onAnimationEnd(Animator animation) {

                int winValue = sectors100[Constants.Spin_set_100.get(lastSpinValueIndex3)];
                lastSpinValueIndex3 = (lastSpinValueIndex3 + 1) % Constants.Spin_set_100.size();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.KEY_SPIN_LAST_100, String.valueOf(lastSpinValueIndex3));
                editor.apply();
                if (winValue == 0) {
                    dialog.setContentView(R.layout.lose_value);
                    mediaPlayer.stop();
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    createNotificationChannel();
                    showNotification((winValue * CashValue));
                } else {
                    mediaPlayer.stop();
                    SaveEarning(CashValue, winValue);
                    dialog.setContentView(R.layout.win_value);
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    showNotification((winValue * CashValue));
                    createNotificationChannel();

                }
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(false);
                dialog.show();

                isSpinning = false;
            }
        });
        isSpinning = false;
        animator.start();
    }

    private void UpdateSpinData500(final int CashValue) {
        Constants.Spin_set_500.clear();
        Constants.Spin_set_500.add(0);//1
        Constants.Spin_set_500.add(5);//0
        Constants.Spin_set_500.add(1);//2
        Constants.Spin_set_500.add(5);//0
        Constants.Spin_set_500.add(5);//0
        randomSectorIndex4 = Constants.Spin_set_500.get(lastSpinValueIndex4);//random.nextInt(sectors.length);

        int degrees = generateRandomDegree500();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Wheel, View.ROTATION, 0.0f, degrees);
        animator.setDuration(3600);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("SetTextI18n")
            public void onAnimationEnd(Animator animation) {

                int winValue = sectors500[Constants.Spin_set_500.get(lastSpinValueIndex4)];
                lastSpinValueIndex4 = (lastSpinValueIndex4 + 1) % Constants.Spin_set_500.size();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.KEY_SPIN_LAST_20, String.valueOf(lastSpinValueIndex4));
                editor.apply();
                if (winValue == 0) {
                    dialog.setContentView(R.layout.lose_value);
                    mediaPlayer.stop();
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    createNotificationChannel();
                    showNotification((winValue * CashValue));

                } else {
                    mediaPlayer.stop();
                    SaveEarning(CashValue, winValue);
                    dialog.setContentView(R.layout.win_value);
                    TextView tvAmountWinning = dialog.findViewById(R.id.tvAmountWinning);
                    tvAmountWinning.setText("RS. " + (winValue * CashValue));

                    showNotification((winValue * CashValue));
                    createNotificationChannel();

                }
                dialog.getWindow().setLayout(-2, -2);
                dialog.setCancelable(false);
                dialog.show();

                isSpinning = false;
            }
        });
        isSpinning = false;
        animator.start();
    }

    private void showNotification(int money) {
        // Create an intent to open the app when the notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_notification).setContentTitle("Earn Money").setContentText("You are win Rs. " + money).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);

        // Display the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "My Channel";
        String description = "My Channel Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Google Pay
     */
    private static boolean isAppInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(SpinWheel.GOOGLE_PAY_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Google", "isAppInstalled: " + e);
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", upiId).appendQueryParameter("pn", name).appendQueryParameter("tn", transactionNote).appendQueryParameter("am", amount).appendQueryParameter("cu", "INR").build();
    }

    private void payWithGPay() {
        if (isAppInstalled(this)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(SpinWheel.this, "Please Install Google Pay", Toast.LENGTH_SHORT).show();
        }
    }

    /* Paytm Payment Integration*/
    public void initiateUpiPayment(String name, String upiId, String transactionNote, String amount) {
        String currencyCode = "INR";

        // Create a UPI URI
        String upiUri = "upi://pay?pa=" + upiId + "&pn=" + name + "&tn=" + transactionNote + "&am=" + amount + "&cu=" + currencyCode;

        // Create an Intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upiUri));

        // Check if an app to handle the Intent is available
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }
        Map<String, String> map = new HashMap<>();
        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);

        if (resultCode == 0) {
            if (resultCode == RESULT_OK || resultCode == 11 && status.equals("success")) {
                // Payment successful
                if (GlobalValue == 20) {
                    map.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map.put("amount", String.valueOf(100 - (Discount * 100)));

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("20_Spin_Left", "5");
                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);
                } else if (GlobalValue == 50) {
                    map.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map.put("amount", String.valueOf(250 - (Discount * 250)));

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("50_Spin_Left", "5");
                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);
                } else if (GlobalValue == 100) {
                    map.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map.put("amount", String.valueOf(500 - (Discount * 500)));

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("100_Spin_Left", "5");
                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);
                } else if (GlobalValue == 500) {
                    map.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map.put("amount", String.valueOf(2500 - (Discount * 2500)));

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("500_Spin_Left", "5");
                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).updateChildren(updateMap);
                }

                Transaction_id = coreHelper.generateTransactionId();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

                map.put("Sender_Name", account.getDisplayName());
                map.put("Sender_Email", account.getEmail());
                map.put("Receiver_Name", "Earn Money App");
                map.put("Receiver_Upi_Id", P_Upi_ID);
                map.put("Transaction_Status", status);
                map.put("Transaction_Id", Transaction_id);
                map.put("TimeStamp", ts);

                database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").child(ts).setValue(map);
                getPaymentDataSpin();
                Toast.makeText(this, "Transaction Sucessful", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, String> map1 = new HashMap<>();

                String Transaction_id_Failed = coreHelper.generateTransactionId();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

                map1.put("Receiver_Name", "Earn Money App");
                map1.put("Receiver_Upi_Id", P_Upi_ID);
                map1.put("Sender_Email", account.getEmail());
                map1.put("Sender_Name", account.getDisplayName());
                map1.put("Transaction_Status", "failed");
                map1.put("Transaction_Id", Transaction_id_Failed);
                map1.put("TimeStamp", ts);

                if (GlobalValue == 20) {
                    map1.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map1.put("amount", String.valueOf(100 - (Discount * 100)));

                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").child(ts).setValue(map1);
                } else if (GlobalValue == 50) {
                    map1.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map1.put("amount", String.valueOf(250 - (Discount * 250)));

                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").child(ts).setValue(map1);
                } else if (GlobalValue == 100) {
                    map1.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map1.put("amount", String.valueOf(500 - (Discount * 500)));

                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").child(ts).setValue(map1);
                } else if (GlobalValue == 500) {
                    map1.put("Spin_Purchase", "5 Spin Of " + GlobalValue);
                    map1.put("amount", String.valueOf(2500 - (Discount * 2500)));

                    database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").child(ts).setValue(map1);
                }

                getPaymentDataSpin();
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Payment was canceled by user
            Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Payment failed or unknown response
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getPaymentDataSpin() {
        FirebaseUser user = auth.getCurrentUser();

        SharedPreferences.Editor editor = preferences.edit();
        FirebaseDatabase.getInstance().getReference().child("Wallet_Data_Entries").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editor.putString(Constants.KEY_SPIN_20, dataSnapshot.child("20_Spin_Left").getValue(String.class));
                editor.putString(Constants.KEY_SPIN_50, dataSnapshot.child("50_Spin_Left").getValue(String.class));
                editor.putString(Constants.KEY_SPIN_100, dataSnapshot.child("100_Spin_Left").getValue(String.class));
                editor.putString(Constants.KEY_SPIN_500, dataSnapshot.child("500_Spin_Left").getValue(String.class));
                editor.putString(Constants.KEY_MONEY, dataSnapshot.child("total_amount").getValue(String.class));
                editor.apply();
                startActivity(new Intent(SpinWheel.this, SpinWheel.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
