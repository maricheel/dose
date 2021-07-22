package com.example.if_dose.Models;

public class CatMsg {
    private String msg;
    private  String date,mode;
    private long date1=1 ,date2=2;
    private  int idserv;

    public long getDate1() {
        return date1;
    }

    public void setDate1(long date1) {
        this.date1 = date1;
    }

    public long getDate2() {
        return date2;
    }

    public void setDate2(long date2) {
        this.date2 = date2;
    }


    public CatMsg(String msg, String date, String mode ,int idserv) {
        this.msg = msg;
        this.date = date;
        this.mode = mode;
        this.idserv=idserv;
    }

    public CatMsg(String msg, String date, String mode, long date1, long date2,int idserv) {
        this.msg = msg;
        this.date = date;
        this.mode = mode;
        this.date1 = date1;
        this.date2 = date2;
        this.idserv=idserv;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getIdserv() {
        return idserv;
    }

    public void setIdserv(int idserv) {
        this.idserv = idserv;
    }
}
