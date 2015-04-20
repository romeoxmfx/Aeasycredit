package com.aeasycredit.order.database;

import com.aeasycredit.order.models.RequestBody;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper{
    //用来保存UserID、Access Token、Access Secret的表名
    public static final String TB_NAME= "requestBody";
    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                TB_NAME+ "("+
                RequestBody.ID+ " integer primary key autoincrement,"+
                RequestBody.TASKID+ " varchar,"+
                RequestBody.INVESTIGATETYPE+ " varchar,"+
                RequestBody.INVESTIGATEENDTIME+ " varchar,"+
                RequestBody.INVESTIGATEADDR+ " varchar,"+
                RequestBody.CONTACTNAME+ " varchar,"+
                RequestBody.CONTACTPHONE+ " varchar,"+
                RequestBody.CONTACTPOST+ " varchar,"+
                RequestBody.COMPANYNAME+ " varchar,"+
                RequestBody.COMPANYNATURE+ " varchar,"+
                RequestBody.STAFFNUMBER+ " varchar,"+
                RequestBody.BUSINESSAREA+ " varchar,"+
                RequestBody.ISHAVECOMPANYBOARD+ " int,"+
                RequestBody.ISHAVECOMPANYNAMEATOFFICEAREA+ " int,"+
                RequestBody.SERVICECONTENT+ " varchar,"+
                RequestBody.COMPANYSCALE+ " varchar,"+
                RequestBody.PRODUCTIONAPPARATUS+ " varchar,"+
                RequestBody.INVENTORY+ " varchar,"+
                RequestBody.INTERVIEWCONTENT+ " varchar,"+
                RequestBody.SUMMARY+ " varchar,"+
                RequestBody.OTHER+ " varchar,"+
                RequestBody.FILES+ " varchar"+
                ")"
                );
        Log. e("Database" ,"onCreate" );
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
        onCreate(db);
        Log. e("Database" ,"onUpgrade" );
    }
    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
//        try{
//            db.execSQL( "ALTER TABLE " +
//                    TB_NAME + " CHANGE " +
//                    oldColumn + " "+ newColumn +
//                    " " + typeColumn
//            );
//        } catch(Exception e){
//            e.printStackTrace();
//        }
    }
}