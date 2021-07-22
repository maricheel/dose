package com.example.if_dose;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.if_dose.Models.CatMsg;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String col1 = "message";
    private static final String col2 = "date";
    private static final String col3 = "type";
    private static final String col4 = "date1";
    private static final String col5 = "date2";
    private static final String col6 = "idserv";



    private static final String table_name = "messageSS";
    private static final String idmsg = "message_id";

    //public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        //super(context, name, factory, version);
    public DatabaseHelper(@Nullable Context context) {
        super(context,table_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+table_name+"("+idmsg+" INTEGER PRIMARY KEY ,"+
                col1+" TEXT ,"+col2+" TEXT ,"+ col3+" TEXT ,"+col4+" NUMERIC ,"+col5+" NUMERIC ,"+col6+" NUMERIC "+")";


        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Drop older table if existeddb.execSQL("DROP TABLE IF EXISTS " + table_name);
        db.execSQL("DROP TABLE " + table_name);
        // Create tables again
        onCreate(db);
    }

    public void addData(CatMsg msg){

           SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(col1,msg.getMsg());
             values.put(col2,msg.getDate());
             values.put(col3,msg.getMode());
            values.put(col4,msg.getDate1());
            values.put(col5,msg.getDate2());
            values.put(col6,msg.getIdserv());

            // Inserting Row
            db.insert(table_name, null, values);

            // Closing database connection
            db.close();

    }

    public CatMsg getMsg(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_name, new String[] {idmsg,
                       col1,col2,col3}, idmsg+ "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CatMsg data = new CatMsg(cursor.getString(1),cursor.getString(2),cursor.getString(3),
                cursor.getLong(4),cursor.getLong(5),cursor.getInt(6) );

        return data ;
    }


    public ArrayList<CatMsg> getAllMsg() {

        ArrayList<CatMsg> msgList = new ArrayList<CatMsg>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CatMsg data = new CatMsg(cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        cursor.getLong(4),cursor.getLong(5),cursor.getInt(6));

                // Adding note to list
               msgList.add(data);
            } while (cursor.moveToNext());
        }

        // return  list
        return msgList;
    }

    public int getMsgCount() {

        String countQuery = "SELECT  * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
    public int getMsgIdserv(String Data) {

        SQLiteDatabase db = this.getReadableDatabase();
        //String selectQuery = "SELECT * FROM messages where idserv = (SELECT MAX(idserv) FROM messages ) AND type ='"+Data+"'";
        String selectQuery = "SELECT * FROM messageSS where idserv = (SELECT MAX(idserv) FROM messageSS where type ='"+Data+"')";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

                return cursor.getInt(6);}
       else
           return 1;

    }




}
