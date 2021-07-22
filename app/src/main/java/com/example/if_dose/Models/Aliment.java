package com.example.if_dose.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Aliment implements Parcelable{
    private String name;
    private String type_aliment;
    private String type_quantite;
    private Double glucide;
    private Double quantite;
    private String quantiteA ;
    private double quantiteB ;
    private boolean with_livret;
    public Aliment(){};
    public Aliment(boolean with_livret, String name, Double glucide, Double quantite, String type_aliment,String type_quantite,String quantiteA,double quantiteB) {
        this.with_livret = with_livret;
        this.name = name;
        this.glucide = glucide;
        this.quantite = quantite;
        this.type_aliment = type_aliment;
        this.type_quantite = type_quantite;
        this.quantiteA =quantiteA;
        this.quantiteB =quantiteB;
    }


    protected Aliment(Parcel in) {
        name = in.readString();
        type_aliment = in.readString();
        type_quantite = in.readString();
        if (in.readByte() == 0) {
            glucide = null;
        } else {
            glucide = in.readDouble();
        }
        if (in.readByte() == 0) {
            quantite = null;
        } else {
            quantite = in.readDouble();
        }
        quantiteA = in.readString();
        quantiteB = in.readDouble();
        with_livret = in.readByte() != 0;
    }

    public static final Creator<Aliment> CREATOR = new Creator<Aliment>() {
        @Override
        public Aliment createFromParcel(Parcel in) {
            return new Aliment(in);
        }

        @Override
        public Aliment[] newArray(int size) {
            return new Aliment[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_aliment() {
        return type_aliment;
    }

    public void setType_aliment(String type_aliment) {
        this.type_aliment = type_aliment;
    }

    public String getType_quantite() {
        return type_quantite;
    }

    public void setType_quantite(String type_quantite) {
        this.type_quantite = type_quantite;
    }

    public Double getGlucide() {
        return glucide;
    }

    public void setGlucide(Double glucide) {
        this.glucide = glucide;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public boolean isWith_livret() {
        return with_livret;
    }

    public void setWith_livret(boolean with_livret) {
        this.with_livret = with_livret;
    }

    public String getQuantiteA() {
        return quantiteA;
    }

    public void setQuantiteA(String quantiteA) {
        this.quantiteA = quantiteA;
    }

    public double getQuantiteB() {
        return quantiteB;
    }

    public void setQuantiteB(double quantiteB) {
        this.quantiteB = quantiteB;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type_aliment);
        dest.writeString(type_quantite);
        if (glucide == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(glucide);
        }
        if (quantite == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(quantite);
        }
        dest.writeString(quantiteA);
        dest.writeDouble(quantiteB);
        dest.writeByte((byte) (with_livret ? 1 : 0));
    }
}
