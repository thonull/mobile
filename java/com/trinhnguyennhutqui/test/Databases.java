package com.trinhnguyennhutqui.test;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Databases extends SQLiteOpenHelper {
    public static  final String DB_NAME = "food.sqlite";
    public static  final int DB_VERSION = 1;
    public static  final String TBL_NAME = "Car";
    public static  final String COL_NAME = "CarName";
    public static  final String COL_CODE = "CarCode";
    public static  final String COL_PRICE = "CarPrice";
    public static  final String COL_DES = "CarDes";
    public static  final String COL_IMAGE = "CarImage";
    public static  final String COL_CATE = "CarCate";

    public Databases(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public Databases(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public Databases(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " (" +COL_CODE + " INTEGER  PRIMARY KEY AUTOINCREMENT, " +COL_NAME + " VARCHAR(100), " + COL_PRICE + " REAL, "+ COL_DES + " VARCHAR(100), "+ COL_IMAGE + " BLOB, "+ COL_CATE + " VARCHAR(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }


    //SELECT
    public Cursor queryData(String sql){
        SQLiteDatabase db =getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    //INSERT, UPDATE , DELETE
    public void execSql(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }


    public boolean insertData(String name ,  double price , String des, byte[] photo,String type ) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO " + TBL_NAME + " VALUES(null,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.clearBindings();
            statement.bindString(1, name);
            statement.bindString(2, String.valueOf(price));
            statement.bindBlob(3, photo);
            statement.bindString(4, des);
            statement.bindString(5, type);
            statement.executeInsert();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public int getNumbOfRow(){
        Cursor cursor = queryData("SELECT * FROM " + TBL_NAME);
        int numRow = cursor.getCount();
        cursor.close();
        return numRow;
    }

    public boolean codeExists(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(*) FROM " + TBL_NAME + " WHERE " + COL_CODE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { code });
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            db.close();
            return count > 0;
        }
        return false;
    }

    public void CreateSampleData(){
        if (getNumbOfRow() == 0){
            try {
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Toyota',2500000,null,'TpHCM','4 chỗ')");
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Ford',1900000,null,'Hà Nội','7 chỗ')");
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Kia',3000000,null,'Đà Nẵng','16 chỗ')");
                execSql("INSERT INTO " + TBL_NAME + " VALUES(null, 'Suzuki',2900000,null,'Mỹ Tho','4 chỗ')");

            }catch(Exception e){
                Log.e("error: ",e.getMessage().toString());
            }
        }
    }
}
