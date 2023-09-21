package com.kc.earn_money;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kc.earn_money.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DashBoard_Main_Page extends AppCompatActivity {
    LinearLayout Contact;
    TextView Privacy_Text;
    LinearLayout Privay_Policy, llTransaction;
    TextView Rate_Text;
    LinearLayout llBank;
    LinearLayout Wallet;
    FirebaseAuth auth;
    TextView getLocation;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    ImageView ivMenu;
    LinearLayout logout;
    SharedPreferences preferences;
    TextView tvUserEmail;
    TextView tvUserName;
    ImageView tvUserProfile;
    RecyclerView recyclerView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, 0);

        getLocation = findViewById(R.id.btnGetLoaction);
        Privacy_Text = findViewById(R.id.btnGetPrivacyPolicy);
        Rate_Text = findViewById(R.id.btnRateUs);
        ivMenu = findViewById(R.id.ivMenu);

        Wallet = findViewById(R.id.llWallet);
        Contact = findViewById(R.id.llContact);
        Privay_Policy = findViewById(R.id.llPrivacy);
        logout = findViewById(R.id.logout);
        llTransaction = findViewById(R.id.llTransaction);
        llBank = findViewById(R.id.llBank);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserProfile = findViewById(R.id.ivProfile);
        tvUserName = findViewById(R.id.tvUserName);

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions build = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gso = build;
        gsc = GoogleSignIn.getClient(this, build);

        if (preferences.getString(Constants.Email, "").isEmpty()) {
            logout.setVisibility(View.GONE);
            Wallet.setVisibility(View.GONE);
            llTransaction.setVisibility(View.GONE);
            llBank.setVisibility(View.GONE);
        } else {
            logout.setVisibility(View.VISIBLE);
            Wallet.setVisibility(View.VISIBLE);
            llTransaction.setVisibility(View.VISIBLE);
            llBank.setVisibility(View.VISIBLE);
        }

        getLocation.setOnClickListener(v -> {
            String isLoggedIn = preferences.getString(Constants.Email, " ");
            Intent intent;
            if (!isLoggedIn.equals(" ")) {
                intent = new Intent(DashBoard_Main_Page.this, SpinWheel_Game_Page.class);
            } else {
                intent = new Intent(DashBoard_Main_Page.this, Terms_Condition_Page.class);
            }
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashBoard_Main_Page.this);
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Are you sure you want to logout?");
            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                gsc.signOut();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(DashBoard_Main_Page.this, "You have been Logged out", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DashBoard_Main_Page.this, DashBoard_Main_Page.class);
                startActivity(intent);
                finish();
            });
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        Privacy_Text.setOnClickListener(v -> startActivity(new Intent(DashBoard_Main_Page.this, Privacy_Policy_Page.class)));

        Rate_Text.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=com.kc.earn_money";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        Wallet.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard_Main_Page.this, Withdraw_Money_Page.class);
            startActivity(i);
        });

        llBank.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard_Main_Page.this, Bank_Transaction_Page.class);
            startActivity(i);
        });

        Contact.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard_Main_Page.this, Contact_Us_Page.class);
            startActivity(i);
        });

        Privay_Policy.setOnClickListener(v -> startActivity(new Intent(DashBoard_Main_Page.this, Privacy_Policy_Page.class)));

        ivMenu.setOnClickListener(v -> {
            DrawerLayout dl = findViewById(R.id.drawer_layout);
            dl.openDrawer(GravityCompat.START);
        });

        llTransaction.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard_Main_Page.this, Withdrawal_Request_Page.class);
            startActivity(i);
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (preferences.getString(Constants.LoginData, "").contains("LoginWithEmailPassword")) {
            String Uname = preferences.getString(Constants.NAME, "");
            String Umail = preferences.getString(Constants.Email, "");
            tvUserName.setText(Uname);
            tvUserEmail.setText(Umail);
        } else if (account != null) {
            String Uname = account.getDisplayName();
            String Umail = preferences.getString(Constants.Email, "");
            Picasso.with(getApplicationContext()).load(Objects.requireNonNull(account.getPhotoUrl()).toString()).into(tvUserProfile);
            tvUserName.setText(Uname);
            tvUserEmail.setText(Umail);
        }
    }
}
