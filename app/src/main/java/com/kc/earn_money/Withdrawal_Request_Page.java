package com.kc.earn_money;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kc.earn_money.Model.WithDrawModel;
import com.kc.earn_money.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Withdrawal_Request_Page extends AppCompatActivity {
    ImageView backBack;
    TextView noData;
    RecyclerView Withdrawal;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<WithDrawModel> RequestData = new ArrayList<>();
    WorkAdapter adapter;
    FirebaseDatabase database;
    Uri uri;
    String status, Sender_Name, Sender_Upi_Id, Receiver_Upi_Id, Transaction_Id, Paid_Status, Receiver_Amount, key;
    SwipeRefreshLayout refresh;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_request);

        preferences = getSharedPreferences(Constants.MyPREFERENCES, 0);

        noData = findViewById(R.id.noData);
        backBack = findViewById(R.id.backBack);

        backBack.setOnClickListener(v -> onBackPressed());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Log.d("UserDataMoney", "UserDataMoney: " + user.getUid());
        database = FirebaseDatabase.getInstance();

        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> {
            Intent i = new Intent(Withdrawal_Request_Page.this, Withdrawal_Request_Page.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
        });

        getPaymentData();

        Withdrawal = findViewById(R.id.WithdrawalWork);
        Withdrawal.setLayoutManager(new LinearLayoutManager(this));
        Withdrawal.setHasFixedSize(true);

        adapter = new WorkAdapter();

        if (preferences.getString(Constants.Email, "").equals("arpit.vekariya123@gmail.com")) {
            getWithdrawalRequest();
        } else {
            getWithdrawalRequestForSingleUser();
        }

    }

    private void getWithdrawalRequestForSingleUser() {
        FirebaseDatabase.getInstance().getReference().child("WithDrawal").child(user.getUid()).child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("DaaSnapshot", "onDataChange: " + dataSnapshot.toString());
                    RequestData.add(new WithDrawModel(dataSnapshot.child("ID").getValue(String.class), dataSnapshot.child("Sender_Name").getValue(String.class), dataSnapshot.child("Sender_Upi_Id").getValue(String.class).toString(), dataSnapshot.child("Receiver_Upi_Id").getValue(String.class).toString(), dataSnapshot.child("Transaction_Id").getValue(String.class).toString(), dataSnapshot.child("Paid_Status").getValue(String.class).toString(), dataSnapshot.child("Receiver_Amount").getValue(String.class).toString()));
                }
                if (RequestData != null && RequestData.size() > 0) {
                    Withdrawal.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    Withdrawal.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                Withdrawal.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWithdrawalRequest() {
        FirebaseDatabase.getInstance().getReference().child("WithDrawal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot requestSnapshot : userSnapshot.child("Request").getChildren()) {

                        RequestData.add(new WithDrawModel(requestSnapshot.child("ID").getValue(String.class), requestSnapshot.child("Sender_Name").getValue(String.class), requestSnapshot.child("Sender_Upi_Id").getValue(String.class).toString(), requestSnapshot.child("Receiver_Upi_Id").getValue(String.class).toString(), requestSnapshot.child("Transaction_Id").getValue(String.class).toString(), requestSnapshot.child("Paid_Status").getValue(String.class).toString(), requestSnapshot.child("Receiver_Amount").getValue(String.class).toString()));
                    }
                }
                if (RequestData != null && RequestData.size() > 0) {
                    Withdrawal.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    Withdrawal.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                Withdrawal.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder> {

        @Override
        public int getItemCount() {
            return RequestData.size();
        }

        @NonNull
        @Override
        public WorkAdapter.WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkAdapter.WorkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false));
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull WorkAdapter.WorkViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.SenderName.setText("Money Sender Name :  " + getString(R.string.app_name));
            holder.ReceiverName.setText("Money Receiver Name :  " + RequestData.get(position).getSenderName());
            holder.SenderUpiId.setText("To : " + RequestData.get(position).getSenderUpiId());
            holder.TransactionId.setText("Transaction Id : " + RequestData.get(position).getTransactionId());
            holder.Status.setText(RequestData.get(position).getStatus());
            holder.ReqAmount.setText("₹ " + RequestData.get(position).getReqAmount());
            holder.id.setText("Id " + RequestData.get(position).getUserid());

            String StatusConvert = RequestData.get(position).getStatus().toString().toLowerCase();
            if (preferences.getString(Constants.Email, "").equals("arpit.vekariya123@gmail.com") && StatusConvert.contains("pending")) {
                holder.PaytoUser.setVisibility(View.VISIBLE);
            } else if (StatusConvert.contains("failed")) {
                holder.PaytoUser.setVisibility(View.VISIBLE);
            } else {
                holder.PaytoUser.setVisibility(View.GONE);
            }

            String StatusTrans = holder.Status.getText().toString().toLowerCase();
            if (StatusTrans.contains("success")) {
                holder.Status.setBackground(getResources().getDrawable(R.drawable.mme_bg_btn_success));
            } else {
                holder.Status.setBackground(getResources().getDrawable(R.drawable.mme_bg_btn_unsuccess));
            }

            String StatusShow = holder.Status.getText().toString().toLowerCase();
            if (StatusShow.contains("pending")) {
                holder.Status.setVisibility(View.GONE);
            } else {
                holder.Status.setBackground(getResources().getDrawable(R.drawable.mme_bg_btn_unsuccess));
            }

            holder.PaytoUser.setOnClickListener(v -> {
                String name = RequestData.get(holder.getAdapterPosition()).getSenderName().trim();
                String upiId = RequestData.get(holder.getAdapterPosition()).getSenderUpiId().trim();
                String amount = RequestData.get(holder.getAdapterPosition()).getReqAmount().trim();
                String transId = RequestData.get(holder.getAdapterPosition()).getTransactionId().trim();

                Toast.makeText(Withdrawal_Request_Page.this, "Value " + name + " \n" + upiId + " \n" + amount + " \n" + transId, Toast.LENGTH_SHORT).show();
                Log.d("d", "Value " + name + " \n" + upiId + " \n" + amount + " \n" + transId);
                uri = getUpiPaymentUri(name, upiId, transId, amount);
                payWithGPay();

            });
        }

        class WorkViewHolder extends RecyclerView.ViewHolder {

            CardView OnlineWithdraw;
            TextView SenderName, ReceiverName, SenderUpiId, TransactionId, Status, ReqAmount, PaytoUser, id;

            WorkViewHolder(View view) {
                super(view);

                OnlineWithdraw = view.findViewById(R.id.OnlineWithdraw);
                id = view.findViewById(R.id.Req_ReceiverUpi);
                SenderName = view.findViewById(R.id.Req_SenderName);
                ReceiverName = view.findViewById(R.id.Req_ReceiverName);
                SenderUpiId = view.findViewById(R.id.Rec_SenderUpiId);
                TransactionId = view.findViewById(R.id.Req_SenderTransId);
                Status = view.findViewById(R.id.Withdraw_Status);
                ReqAmount = view.findViewById(R.id.TotalWithdrawAmount);
                PaytoUser = view.findViewById(R.id.PayToUser);
            }
        }
    }

    public void getPaymentData() {
        FirebaseDatabase.getInstance().getReference().child("WithDrawal").child(user.getUid()).child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sender_Name = dataSnapshot.child("Sender_Name").getValue(String.class);
                Sender_Upi_Id = dataSnapshot.child("Sender_Upi_Id").getValue(String.class);
                Receiver_Upi_Id = dataSnapshot.child("Receiver_Upi_Id").getValue(String.class);
                Transaction_Id = dataSnapshot.child("Transaction_Id").getValue(String.class);
                Paid_Status = dataSnapshot.child("Paid_Status").getValue(String.class);
                Receiver_Amount = dataSnapshot.child("Receiver_Amount").getValue(String.class);
                key = dataSnapshot.child("ID").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static boolean isAppInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(GOOGLE_PAY_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Google", "isAppInstalled: " + e);
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String S_Name, String S_UPI, String S_Trans, String Amount) {
        return new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa", S_UPI).appendQueryParameter("pn", S_Name).appendQueryParameter("tn", "Withdrawal").appendQueryParameter("am", Amount).appendQueryParameter("cu", "INR").build();
    }

    private void payWithGPay() {
        if (isAppInstalled(this)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Please Install Google Pay", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        Map<String, Object> map = new HashMap<>();

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            map.put("Paid_Status", "success");
            database.getReference().child("WithDrawal").child(user.getUid()).child("Request").child(key).updateChildren(map);
            database.getReference().child("WithDrawal").child(user.getUid()).child("RequestMoney").setValue(false);

            Toast.makeText(this, "Transaction Sucessful", Toast.LENGTH_SHORT).show();
        } else {
            map.put("Paid_Status", "failed");
            database.getReference().child("WithDrawal").child(user.getUid()).child("Request").child(key).updateChildren(map);
            database.getReference().child("WithDrawal").child(user.getUid()).child("RequestMoney").setValue(true);

            Toast.makeText(this, "Transaction Failed \n Wait For More Time", Toast.LENGTH_SHORT).show();
        }
    }

}