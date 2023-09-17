package com.kc.earn_money.Model;

public class WithDrawModel {

    String PyId, SenderName, SenderUpiId, ReceiverUpiId, TransactionId, Status, ReqAmount,UserId;

    public WithDrawModel() {
    }


    public WithDrawModel(String pyId, String senderName, String senderUpiId, String receiverUpiId, String transactionId, String status, String reqAmount, String userId) {
        PyId = pyId;
        SenderName = senderName;
        SenderUpiId = senderUpiId;
        ReceiverUpiId = receiverUpiId;
        TransactionId = transactionId;
        Status = status;
        ReqAmount = reqAmount;
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPyId() {
        return PyId;
    }

    public void setPyId(String pyId) {
        PyId = pyId;
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