package com.kc.earn_money.Model;

public class WithDrawModel {

    String SenderName, SenderUpiId, ReceiverUpiId, TransactionId, Status, ReqAmount;

    public WithDrawModel(String senderName, String senderUpiId, String receiverUpiId, String transactionId, String status, String reqAmount) {
        SenderName = senderName;
        SenderUpiId = senderUpiId;
        ReceiverUpiId = receiverUpiId;
        TransactionId = transactionId;
        Status = status;
        ReqAmount = reqAmount;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderUpiId() {
        return SenderUpiId;
    }

    public void setSenderUpiId(String senderUpiId) {
        SenderUpiId = senderUpiId;
    }

    public String getReceiverUpiId() {
        return ReceiverUpiId;
    }

    public void setReceiverUpiId(String receiverUpiId) {
        ReceiverUpiId = receiverUpiId;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReqAmount() {
        return ReqAmount;
    }

    public void setReqAmount(String reqAmount) {
        ReqAmount = reqAmount;
    }
}