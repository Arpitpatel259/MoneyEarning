package com.example.earn_money;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.earn_money.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SpinWheel extends AppCompatActivity {

    private static final int[] sectors = {1, 2, 3, 4, 5, 0};
    private static final int[] sectorDegrees = new int[sectors.length];
    private static final Random random = new Random();
    int SpinCounter100;
    int SpinCounter20;
    int SpinCounter50;
    int SpinCounter500;
    ImageView Wheel;
    FirebaseAuth auth;
    AppCompatButton btnPlayNow;
    FirebaseDatabase database;
    Dialog dialog;
    int earningRecord;
    ImageView imgViewBack4;
    private boolean is100Select = false;
    private boolean is20Select = false;
    private boolean is500Select = false;
    private boolean is50Select = false;
    /* access modifiers changed from: private */
    public boolean isSpinning = false;
    LinearLayout ll100App;
    LinearLayout ll20App;
    LinearLayout ll500App;
    LinearLayout ll50App;
    LinearLayout llWithdrawMoney;
    SharedPreferences preferences;
    int randomSectorIndex = 0;
    TextView tvFiftySpinLeft;
    TextView tvFiveHundredSpinLeft;
    TextView tvHundredSpinLeft;
    TextView tvTwentySpinLeft;
    TextView tvWithdrawMoney;
    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);  // Shared Preferences

        SpinCounter20 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_20, "5"));
        SpinCounter50 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_50, "5"));
        SpinCounter100 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_100, "5"));
        SpinCounter500 = Integer.parseInt(preferences.getString(Constants.KEY_SPIN_500, "5"));

        getDgreeForSector();

        dialog = new Dialog(this);

        earningRecord = Integer.parseInt(preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));

        tvWithdrawMoney = findViewById(R.id.tvWithdrawMoney);
        tvWithdrawMoney.setText("₹ " + preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));

        Wheel = findViewById(R.id.imgWheel);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        tvTwentySpinLeft = findViewById(R.id.tvTwoSpinLeft);
        tvFiftySpinLeft = findViewById(R.id.tvFiveSpinLeft);
        tvHundredSpinLeft = findViewById(R.id.tvOneSpinLeft);
        tvFiveHundredSpinLeft = findViewById(R.id.tvFiveZeroSpinLeft);

        tvTwentySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_20, Constants.KEY_MONEY) + " Spin Left");
        tvFiftySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_50, Constants.KEY_MONEY) + " Spin Left");
        tvHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_100, Constants.KEY_MONEY) + " Spin Left");
        tvFiveHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_500, Constants.KEY_MONEY) + " Spin Left");

        llWithdrawMoney = findViewById(R.id.llWithdrawMoney);
        llWithdrawMoney.setOnClickListener(v -> startActivity(new Intent(SpinWheel.this, Withdraw.class)));

        ll20App = findViewById(R.id.ll20App);
        ll20App.setOnClickListener(v -> {
            if (SpinCounter20 == 0) {
                startActivity(new Intent(SpinWheel.this, SlotAdd.class));
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
                startActivity(new Intent(SpinWheel.this, SlotAdd.class));
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
                startActivity(new Intent(SpinWheel.this, SlotAdd.class));
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
                startActivity(new Intent(SpinWheel.this, SlotAdd.class));
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
        imgViewBack4.setOnClickListener(v -> {
            startActivity(new Intent(SpinWheel.this, DashBoard.class));
            finish();
        });

        btnPlayNow = findViewById(R.id.btnPlayNow);
        btnPlayNow.setOnClickListener(v -> {
            if (!is20Select && !is50Select && !is100Select && !is500Select) {
                ll20App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll50App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll100App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                ll500App.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mme_bg_app_unselected, null));
                Toast.makeText(SpinWheel.this, "Please Select Amount", Toast.LENGTH_SHORT).show();
            } else if (is20Select && !isSpinning) {
                spinWheel(20);
                isSpinning = true;
            } else if (is50Select && !isSpinning) {
                spinWheel(50);
                isSpinning = true;
            } else if (is100Select && !isSpinning) {
                spinWheel(100);
                isSpinning = true;
            } else if (is500Select && !isSpinning) {
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

            UpdateSpinData(CashValue);
        } else if (is50Select && SpinCounter50 > 0) {
            SpinCounter50--;

            SharedPreferences.Editor editor2 = preferences.edit();
            editor2.putString(Constants.KEY_SPIN_50, String.valueOf(SpinCounter50));
            editor2.apply();

            UpdateSpinData(CashValue);
        } else if (is100Select && SpinCounter100 > 0) {
            SpinCounter100--;

            SharedPreferences.Editor editor3 = preferences.edit();
            editor3.putString(Constants.KEY_SPIN_100, String.valueOf(SpinCounter100));
            editor3.apply();

            UpdateSpinData(CashValue);
        } else if (is500Select && SpinCounter500 > 0) {
            SpinCounter500--;

            SharedPreferences.Editor editor4 = preferences.edit();
            editor4.putString(Constants.KEY_SPIN_500, String.valueOf(SpinCounter500));
            editor4.apply();

            UpdateSpinData(CashValue);
        } else {
            is20Select = false;
            is50Select = false;
            is100Select = false;
            is500Select = false;
            isSpinning = false;
            Toast.makeText(this, "You Have Not Any Spin", Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: private */
    public void SaveEarning(int cashValue, int spinValue) {
        earningRecord += cashValue * spinValue;
    }

    private int generateRandomDegree() {
        return (sectors.length * 360) + sectorDegrees[randomSectorIndex];
    }

    private void getDgreeForSector() {
        int sectorDegree = 360 / sectors.length;
        for (int i = 0; i < sectors.length; i++) {
            sectorDegrees[i] = (i + 1) * sectorDegree;
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
        map.put("amount", preferences.getString(Constants.KEY_MONEY, ""));
        map.put("TimeStamp", ts);

        database.getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Transaction").child(ts).setValue(map);

        tvWithdrawMoney.setText("₹ " + preferences.getString(Constants.KEY_MONEY, Constants.KEY_MONEY));
        if (is20Select) {
            tvTwentySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_20, Constants.KEY_MONEY) + " Spin Left");
        } else if (is50Select) {
            tvFiftySpinLeft.setText(preferences.getString(Constants.KEY_SPIN_50, Constants.KEY_MONEY) + " Spin Left");
        } else if (is100Select) {
            tvHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_100, Constants.KEY_MONEY) + " Spin Left");
        } else if (is500Select) {
            tvFiveHundredSpinLeft.setText(preferences.getString(Constants.KEY_SPIN_500, Constants.KEY_MONEY) + " Spin Left");
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

    private void UpdateSpinData(final int CashValue) {
        randomSectorIndex = random.nextInt(sectors.length);
        int degrees = generateRandomDegree();
        ObjectAnimator animator = ObjectAnimator.ofFloat(Wheel, View.ROTATION, 0.0f, degrees);
        animator.setDuration(3600);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                int winValue = sectors[sectors.length - (randomSectorIndex + 1)];
                if (winValue == 0) {
                    dialog.setContentView(R.layout.lose_value);
                    Toast.makeText(SpinWheel.this, "You Got This " + winValue + " " + CashValue, Toast.LENGTH_SHORT).show();
                } else {
                    SaveEarning(CashValue, winValue);
                    dialog.setContentView(R.layout.win_lose);
                    Toast.makeText(SpinWheel.this, "You Got This" + winValue + " " + CashValue, Toast.LENGTH_SHORT).show();
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
}
