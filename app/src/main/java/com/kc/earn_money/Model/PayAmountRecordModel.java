package com.kc.earn_money.Model;

public class PayAmountRecordModel {

    String PayerName, PayerEmail, PayerTransId, ReceiverUpi, PayerSpin, PayerStatus, tvBankAmount;
    public PayAmountRecordModel(String payerName, String payerEmail, String payerTransId, String receiverUpi, String payerSpin, String payerStatus, String tvBankAmount) {
        PayerName = payerName;
        PayerEmail = payerEmail;
        PayerTransId = payerTransId;
        ReceiverUpi = receiverUpi;
        PayerSpin = payerSpin;
        PayerStatus = payerStatus;
        this.tvBankAmount = tvBankAmount;
    }

    public String getPayerName() {
        return PayerName;
    }

    public void setPayerName(String payerName) {
        PayerName = payerName;
    }

    public String getPayerEmail() {
        return PayerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        PayerEmail = payerEmail;
    }

    public String getPayerTransId() {
        return PayerTransId;
    }

    public void setPayerTransId(String payerTransId) {
        PayerTransId = payerTransId;
    }

    public String getReceiverUpi() {
        return ReceiverUpi;
    }

    public void setReceiverUpi(String receiverUpi) {
        ReceiverUpi = receiverUpi;
    }

    public String getPayerSpin() {
        return PayerSpin;
    }

    public void setPayerSpin(String payerSpin) {
        PayerSpin = payerSpin;
    }

    public String getPayerStatus() {
        return PayerStatus;
    }

    public void setPayerStatus(String payerStatus) {
        PayerStatus = payerStatus;
    }

    public String getTvBankAmount() {
        return tvBankAmount;
    }

    public void setTvBankAmount(String tvBankAmount) {
        this.tvBankAmount = tvBankAmount;
    }
}
