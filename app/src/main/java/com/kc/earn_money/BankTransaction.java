package com.kc.earn_money;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.kc.earn_money.Model.PayAmountRecordModel;

import java.util.ArrayList;

public class BankTransaction extends AppCompatActivity {
    ImageView imgViewBack8;
    TextView noData1;
    RecyclerView circular_Transaction, Circular_Bank;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<PayAmountRecordModel> BankList = new ArrayList<>();
    WorkAdapter adapter1;
    SwipeRefreshLayout refresh;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transaction);

        noData1 = findViewById(R.id.noData1);

        refresh = findViewById(R.id.swipe_refresh1);
        refresh.setOnRefreshListener(() -> {
            Intent i = new Intent(BankTransaction.this, BankTransaction.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
        });

        imgViewBack8 = findViewById(R.id.backButton);
        imgViewBack8.setOnClickListener(v -> onBackPressed());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        circular_Transaction = findViewById(R.id.BankData);
        circular_Transaction.setLayoutManager(new LinearLayoutManager(this));
        circular_Transaction.setHasFixedSize(true);
        adapter1 = new WorkAdapter();
        getBankTransactionData();
    }

    private void getBankTransactionData() {
        FirebaseDatabase.getInstance().getReference().child("Wallet_Data_Entries").child(user.getUid()).child("Bank_Transaction").child("Spin_Purchase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BankList.add(new PayAmountRecordModel(
                            dataSnapshot.child("Sender_Name").getValue(String.class).toString(),
                            dataSnapshot.child("Sender_Email").getValue(String.class).toString(),
                            dataSnapshot.child("Transaction_Id").getValue(String.class).toString(),
                            dataSnapshot.child("Receiver_Upi_Id").getValue(String.class).toString(),
                            dataSnapshot.child("Spin_Purchase").getValue(String.class).toString(),
                            dataSnapshot.child("Transaction_Status").getValue(String.class).toString(),
                            dataSnapshot.child("amount").getValue(String.class).toString()));
                }
                if (BankList != null && BankList.size() > 0) {
                    circular_Transaction.setVisibility(View.VISIBLE);
                    noData1.setVisibility(View.GONE);
                } else {
                    circular_Transaction.setVisibility(View.GONE);
                    noData1.setVisibility(View.VISIBLE);
                }
                circular_Transaction.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder> {

        @Override
        public int getItemCount() {
            return BankList.size();
        }

        @NonNull
        @Override
        public WorkAdapter.WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WorkAdapter.WorkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_transaction, parent, false));
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull WorkAdapter.WorkViewHolder holder, int position) {
            holder.PayerName.setText(BankList.get(position).getPayerName());
            holder.PayerEmail.setText(BankList.get(position).getPayerEmail());
            holder.PayerTransId.setText("Transaction Id : " + BankList.get(position).getPayerTransId());
            holder.PayerSpin.setText("For Spin : " + BankList.get(position).getPayerSpin());
            holder.Receiverupi.setText("App Upi Id : " + BankList.get(position).getReceiverUpi());

            if (BankList.get(position).getPayerStatus() == "success") {
                holder.PayerStatus.setText(BankList.get(position).getPayerStatus());
                holder.PayerStatus.setBackground(getResources().getDrawable(R.drawable.mme_bg_btn_success));
            } else {
                holder.PayerStatus.setText(BankList.get(position).getPayerStatus());
                holder.PayerStatus.setBackground(getResources().getDrawable(R.drawable.mme_bg_btn_unsuccess));
            }
            holder.tvBankAmount.setText("₹ " + BankList.get(position).getTvBankAmount());
        }

        class WorkViewHolder extends RecyclerView.ViewHolder {

            CardView BankCard;
            TextView PayerName, PayerEmail, PayerTransId, PayerSpin, Receiverupi, PayerStatus, tvBankAmount;

            WorkViewHolder(View view) {
                super(view);

                BankCard = view.findViewById(R.id.TransRecBank);
                PayerName = view.findViewById(R.id.PayerName); //Name
                PayerEmail = view.findViewById(R.id.PayerEmail); //PayerEmail
                PayerTransId = view.findViewById(R.id.PayerTransId); //PayerTransId
                PayerSpin = view.findViewById(R.id.PayerSpin);//PayerSpin
                Receiverupi = view.findViewById(R.id.PayerTs); //PayerUpiId
                PayerStatus = view.findViewById(R.id.PayerStatus); //STatus
                tvBankAmount = view.findViewById(R.id.tvBankAmount); //amount

            }
        }
    }
}
