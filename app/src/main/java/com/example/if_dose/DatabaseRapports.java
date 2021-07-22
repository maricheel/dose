package com.example.if_dose;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.if_dose.Models.Rapport;

import java.util.ArrayList;


public class DatabaseRapports extends SQLiteOpenHelper {

    private static final String col1 ="date";
    private static final String col2= "r1";
    private static final String col3 = "r2";
    private static final String col4= "r3";
    private static final String col5 = "r4";
    private static final String col6 = "r5";
    private static final String col7= "r6";
    private static final String col8= "r7";
    private static final String col9= "r8";
    private static final String col10= "r9";
    private static final String col11 = "r10";
    private static final String col12 = "r11";
    private static final String col13 = "r12";
    private static final String col14 = "r13";
    private static final String col15 = " r14";
    private static final String col16 = "r15";
    private static final String col17 = " r16";
    private static final String col18 = " rd";
    private static final String col19 = " rp";
    private static final String col20 = "rc";
    private static final String col21 = "rdi";
    private static final String col22 = "iss";
    private static final String col23 = "obj";
    private static final String col24 = "gluco0";
    private static final String col25 = "gluco1";
    private static final String col26 = "gluco2";
    private static final String col27 = "gluco3";
    private static final String col28 = "c1";
    private static final String col29 = "c2";
    private static final String col30 = "c3";
    private static final String col31 = "c4";
    private static final String col32 = " alimentsPDej";
    private static final String col33= "alimentsDej";
    private static final String col34 = " alimentsCol";
    private static final String col35 = "alimentsDin";
    private static final String col36 = "longdate";



    private static final String table_name = "rapports1";
    private static final String idrapport = "rapport_id";

    //public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    //super(context, name, factory, version);
    public DatabaseRapports(@Nullable Context context) {
        super(context,table_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+table_name+"("+idrapport+" INTEGER PRIMARY KEY ,"+
                col1+" TEXT ,"+ col2+" FLOAT ,"+
                col3+" FLOAT ,"+ col4+" FLOAT ,"+
                col5+" FLOAT ,"+ col6+" FLOAT ,"+
                col7+" FLOAT ,"+ col8+" FLOAT ,"+
                col9+" FLOAT ,"+ col10+" FLOAT ,"+
                col11+" FLOAT ,"+ col12+" FLOAT ,"+
                col13+" FLOAT ,"+ col14+" FLOAT ,"+
                col15+" FLOAT ,"+ col16+" FLOAT ,"+
                col17+" FLOAT ,"+ col18+" FLOAT ,"+
                col19+" FLOAT ,"+ col20+" FLOAT ,"+
                col21+" FLOAT ,"+ col22+" FLOAT ,"+
                col23+" FLOAT ,"+ col24+" FLOAT ,"+
                col25+" FLOAT ,"+ col26+" FLOAT ,"+
                col27+" FLOAT ,"+ col28+" TEXT ,"+
                col29+" TEXT ,"+ col30+" TEXT ,"+
                col31+" TEXT ,"+ col32+" TEXT ,"+
                col33+" TEXT ,"+ col34+" TEXT ,"+
                col35+" TEXT ,"+ col36+" NUMERIC "+")";


        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Drop older table if existeddb.execSQL("DROP TABLE IF EXISTS " + table_name);
        db.execSQL("DROP TABLE " + table_name);
        // Create tables again
        onCreate(db);
    }

    public void addData(Rapport rapport){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(col1,rapport.getDate());
        values.put(col2,rapport.getR1());
        values.put(col3,rapport.getR2());
        values.put(col4,rapport.getR3());
        values.put(col5,rapport.getR4());
        values.put(col6,rapport.getR5());
        values.put(col7,rapport.getR6());
        values.put(col8,rapport.getR7());
        values.put(col9,rapport.getR8());
        values.put(col10,rapport.getR9());
        values.put(col11,rapport.getR10());
        values.put(col12,rapport.getR11());
        values.put(col13,rapport.getR12());
        values.put(col14,rapport.getR13());
        values.put(col15,rapport.getR14());
        values.put(col16,rapport.getR15());
        values.put(col17,rapport.getR16());
        values.put(col18,rapport.getRd());
        values.put(col19,rapport.getRp());
        values.put(col20,rapport.getRc());
        values.put(col21,rapport.getRdi());
        values.put(col22,rapport.getIs());
        values.put(col23,rapport.getObj());
        values.put(col24,rapport.getGluco0());
        values.put(col25,rapport.getGluco1());
        values.put(col26,rapport.getGluco2());
        values.put(col27,rapport.getGluco3());
        values.put(col28,rapport.getC1());
        values.put(col29,rapport.getC2());
        values.put(col30,rapport.getC3());
        values.put(col31,rapport.getC4());
        values.put(col32,rapport.getAlimentsPDej());
        values.put(col33,rapport.getAlimentsDej());
        values.put(col34,rapport.getAlimentsCol());
        values.put(col35,rapport.getAlimentsDin());
        values.put(col36,rapport.getL());

        // Inserting Row
        db.insert(table_name, null, values);

        // Closing database connection
        db.close();

    }

    public Rapport getRapport(long Date) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM rapports1 where longdate='"+Date+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();
        Rapport data = new Rapport(cursor.getLong(36),cursor.getFloat(2),cursor.getFloat(3),cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6),cursor.getFloat(7),
                cursor.getFloat(8),cursor.getFloat(9),cursor.getFloat(10),cursor.getFloat(11),cursor.getFloat(12),cursor.getFloat(13),cursor.getFloat(14),
                cursor.getFloat(15),cursor.getFloat(16),cursor.getFloat(17),cursor.getFloat(18),cursor.getFloat(19),cursor.getFloat(20),
                cursor.getFloat(21),cursor.getFloat(22),cursor.getFloat(23),cursor.getFloat(24),cursor.getFloat(25),cursor.getFloat(26),
                cursor.getFloat(27),cursor.getString(28),cursor.getString(29),cursor.getString(30),cursor.getString(31),cursor.getString(32),
                cursor.getString(33),cursor.getString(34),cursor.getString(35),cursor.getString(1));
        return data ;


    }


    public ArrayList<Rapport> getAllRapport(long start ,long end) {

        ArrayList<Rapport> rap =new  ArrayList<Rapport>();
        SQLiteDatabase db = this.getReadableDatabase();
       // String selectQuery = "SELECT * FROM rapports1 where substr(date,7,4)||substr(date,4,2)||substr(date,1,2) BETWEEN '"+11111110+"' AND '"+11111112+"'";
        String selectQuery = "SELECT * FROM rapports1 where longdate BETWEEN '"+start+"' AND '"+end+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            do {
                System.out.println("=====size======================="+rap.size()+"==============================");
                Rapport data = new Rapport(cursor.getLong(36),cursor.getFloat(2),cursor.getFloat(3),cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6),cursor.getFloat(7),
                        cursor.getFloat(8),cursor.getFloat(9),cursor.getFloat(10),cursor.getFloat(11),cursor.getFloat(12),cursor.getFloat(13),cursor.getFloat(14),
                        cursor.getFloat(15),cursor.getFloat(16),cursor.getFloat(17),cursor.getFloat(18),cursor.getFloat(19),cursor.getFloat(20),
                        cursor.getFloat(21),cursor.getFloat(22),cursor.getFloat(23),cursor.getFloat(24),cursor.getFloat(25),cursor.getFloat(26),
                        cursor.getFloat(27),cursor.getString(28),cursor.getString(29),cursor.getString(30),cursor.getString(31),cursor.getString(32),
                        cursor.getString(33),cursor.getString(34),cursor.getString(35),cursor.getString(1));

                // Adding to list
                rap.add(data);

            } while (cursor.moveToNext());

            return rap;
    }

    public int getCountall(long start ,long end) {

        String countQuery = "SELECT * FROM rapports1 where longdate BETWEEN '"+start+"' AND '"+end+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public void deleterapp(long data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, col36 + " = ?",
                new String[] { String.valueOf(data) });

        db.close();
    }

    public int getCount(long Date) {

        String countQuery ="SELECT * FROM rapports1 where longdate='"+Date+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    public void updatAliments1(long l,Rapport rapport){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(col32,rapport.getAlimentsPDej());
        values.put(col19,rapport.getRp());
        values.put(col24,rapport.getGluco0());
        values.put(col28,rapport.getC1());
        values.put(col2,rapport.getR1());
        values.put(col3,rapport.getR2());
        values.put(col4,rapport.getR3());
        values.put(col5,rapport.getR4());

        db.update(table_name,values,col36 + " = ?",
                new String[] { String.valueOf(l) });

        // Closing database connection
        db.close();

    }


    public void updatAliments2(long l,Rapport rapport){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(col6,rapport.getR5());
        values.put(col7,rapport.getR6());
        values.put(col8,rapport.getR7());
        values.put(col9,rapport.getR8());
        values.put(col33,rapport.getAlimentsDej());
        values.put(col19,rapport.getRd());
        values.put(col25,rapport.getGluco1());
        values.put(col29,rapport.getC2());

        db.update(table_name,values,col36 + " = ?",
                new String[] { String.valueOf(l) });

        // Closing database connection
        db.close();

    }


    public void updatAliments3(long l,Rapport rapport){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(col10,rapport.getR9());
        values.put(col11,rapport.getR10());
        values.put(col12,rapport.getR11());
        values.put(col13,rapport.getR12());
        values.put(col34,rapport.getAlimentsCol());
        values.put(col20,rapport.getRc());
        values.put(col26,rapport.getGluco2());
        values.put(col30,rapport.getC3());

        db.update(table_name,values,col36 + " = ?",
                new String[] { String.valueOf(l) });
        // Closing database connection
        db.close();

    }



    public void updatAliments4(long l,Rapport rapport){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(col14,rapport.getR13());
        values.put(col15,rapport.getR14());
        values.put(col16,rapport.getR15());
        values.put(col17,rapport.getR16());
        values.put(col35,rapport.getAlimentsDin());
        values.put(col21,rapport.getRdi());
        values.put(col27,rapport.getGluco3());
        values.put(col31,rapport.getC4());

        db.update(table_name,values,col36 + " = ?",
                new String[] { String.valueOf(l) });
        // Closing database connection
        db.close();

    }


}
