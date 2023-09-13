package com.kc.earn_money.Model;

public class MoneyRequestModel {

    String id;
    String paid_status;
    String rec_amount;
    String rec_name;
    String rec_upi_id;
    String send_name;
    String send_upi_id;
    String trans_id;

    public MoneyRequestModel(String id, String paid_status, String rec_amount, String rec_name, String rec_upi_id, String send_name, String send_upi_id, String trans_id) {
        this.id = id;
        this.paid_status = paid_status;
        this.rec_amount = rec_amount;
        this.rec_name = rec_name;
        this.rec_upi_id = rec_upi_id;
        this.send_name = send_name;
        this.send_upi_id = send_upi_id;
        this.trans_id = trans_id;
    }

    public MoneyRequestModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getRec_amount() {
        return rec_amount;
    }

    public void setRec_amount(String rec_amount) {
        this.rec_amount = rec_amount;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_upi_id() {
        return rec_upi_id;
    }

    public void setRec_upi_id(String rec_upi_id) {
        this.rec_upi_id = rec_upi_id;
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getSend_upi_id() {
        return send_upi_id;
    }

    public void setSend_upi_id(String send_upi_id) {
        this.send_upi_id = send_upi_id;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }
}
