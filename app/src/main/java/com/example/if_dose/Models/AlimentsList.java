package com.example.if_dose.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AlimentsList extends ArrayList<Aliment> implements Parcelable {

    public AlimentsList(){

    }

    protected AlimentsList(Parcel in) {
        this.clear();
        int size = in.readInt();
        for(int i = 0; i < size; i++)
        {
            Aliment pers = new Aliment();
            pers.setName(in.readString());
            pers.setType_aliment(in.readString())  ;
            pers.setType_quantite(in.readString());
            if (in.readByte() == 0) {
                pers.setGlucide(null);
            } else {
                pers.setGlucide(in.readDouble());
            }
            if (in.readByte() == 0) {
                pers.setQuantite(null);
            } else {
                pers.setQuantite(in.readDouble());
            }
            pers.setQuantiteA(in.readString()) ;
            pers.setQuantiteB(in.readDouble()) ;
            pers.setWith_livret(in.readBoolean()) ;
            this.add(pers);
        }
    }

    public static final Creator<AlimentsList> CREATOR = new Creator<AlimentsList>() {
        @Override
        public AlimentsList createFromParcel(Parcel in) {
            return new AlimentsList(in);
        }

        @Override
        public AlimentsList[] newArray(int size) {
            return new AlimentsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Taille de la liste
        int size = this.size();
        dest.writeInt(size);
        for(int i=0; i < size; i++)
        {
            Aliment pers = this.get(i); //On vient lire chaque objet personne
            dest.writeString(pers.getName());
            dest.writeString(pers.getType_aliment());
            dest.writeString(pers.getType_quantite());
            if (pers.getGlucide() == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(pers.getGlucide());
            }
            if (pers.getQuantite() == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(pers.getQuantite());
            }
            dest.writeString(pers.getQuantiteA());
            dest.writeDouble(pers.getQuantiteB());
            dest.writeByte((byte) (pers.isWith_livret() ? 1 : 0));
        }
    }
}
