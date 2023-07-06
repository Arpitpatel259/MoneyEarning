package com.example.earn_money;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.earn_money.Utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DashBoard extends AppCompatActivity {
    LinearLayout Contact;
    TextView Privacy_Text;
    LinearLayout Privay_Policy;
    TextView Rate_Text;
    LinearLayout Terms;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getLocation = findViewById(R.id.btnGetLoaction);
        Privacy_Text = findViewById(R.id.btnGetPrivacyPolicy);
        Rate_Text = findViewById(R.id.btnRateUs);

        ivMenu = findViewById(R.id.ivMenu);

        Wallet = findViewById(R.id.llWallet);
        Contact = findViewById(R.id.llContact);
        Privay_Policy = findViewById(R.id.llPrivacy);
        Terms = findViewById(R.id.Terms);
        logout = findViewById(R.id.logout);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserProfile = findViewById(R.id.ivProfile);
        tvUserName = findViewById(R.id.tvUserName);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, 0);
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions build = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gso = build;
        gsc = GoogleSignIn.getClient(this, build);

        getLocation.setOnClickListener(v -> {
            String isLoggedIn = preferences.getString(Constants.Email, " ");
            if (!isLoggedIn.equals(" ")) {
                Intent intent = new Intent(DashBoard.this, SpinWheel.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DashBoard.this, Permission.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashBoard.this);
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Are you sure you want to logout?");
            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                gsc.signOut();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(DashBoard.this, "You have been Logged out", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DashBoard.this, DashBoard.class);
                startActivity(intent);
                finish();
            });
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        Privacy_Text.setOnClickListener(v -> Toast.makeText(DashBoard.this, "Privacy Policy Button Pressed", Toast.LENGTH_SHORT).show());

        Rate_Text.setOnClickListener(v -> Toast.makeText(DashBoard.this, "Rate Us Button Pressed", Toast.LENGTH_SHORT).show());

        Wallet.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard.this, Withdraw.class);
            startActivity(i);
        });

        Contact.setOnClickListener(v -> {
            Intent i = new Intent(DashBoard.this, Contact_Us.class);
            startActivity(i);

        });

        Privay_Policy.setOnClickListener(v -> Toast.makeText(DashBoard.this, "Privacy P Button Pressed", Toast.LENGTH_SHORT).show());

        Terms.setOnClickListener(v -> Toast.makeText(DashBoard.this, "Terms & Condition Button Pressed", Toast.LENGTH_SHORT).show());

        ivMenu.setOnClickListener(v -> {
            DrawerLayout dl = findViewById(R.id.drawer_layout);
            dl.openDrawer(GravityCompat.START);
        });

        GoogleSignInOptions build2 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gso = build2;
        gsc = GoogleSignIn.getClient(this, build2);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String Uname = account.getDisplayName();
            String Umail = account.getEmail();
            Picasso.with(getApplicationContext()).load(Objects.requireNonNull(account.getPhotoUrl()).toString()).into(tvUserProfile);
            tvUserName.setText(Uname);
            tvUserEmail.setText(Umail);
        }
    }
}
