package com.example.caffeine;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UserDatabaseClass extends SQLiteOpenHelper {


    private static final String database_name = "user.db";
    private static final String table_user = "user_table";
    private static final String col_user_1 = "ID";
    private static final String col_user_2 = "Name";
    private static final String col_user_3 = "Email";
    private static final String col_user_4 = "Password";
    private static final String col_user_5 = "Phone";


    public UserDatabaseClass(@Nullable Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_employee_table = "CREATE TABLE " + table_user + "("
                + col_user_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_user_2 + " TEXT,"
                + col_user_3 + " TEXT," + col_user_4 + " TEXT," + col_user_5 + " TEXT" +")";
        db.execSQL(create_employee_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_employee_table = "DROP TABLE IF EXISTS " + table_user;
        db.execSQL(drop_employee_table);
        onCreate(db);
    }

    boolean insertData(String name, String email, String password,String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_user_2,name);
        contentValues.put(col_user_3,email);
        contentValues.put(col_user_4,password);
        contentValues.put(col_user_5,phone);
        long result = db.insert(table_user,null ,contentValues);
        return result != -1;
    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+ table_user,null);
    }

    boolean checkUser(String email, String password) {

        String[] columns = {
                col_user_1
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = col_user_3 + " = ?" + " AND " + col_user_4 + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(table_user, columns, selection, selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    boolean checkUserEmail(String email) {

        String[] columns = {
                col_user_1
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = col_user_3 + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(table_user, columns, selection, selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    UserClass getUser(String Email, Activity context) {

        UserClass user = new UserClass();

        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + table_user
                    + " WHERE " + col_user_3 + " = " +"'"+Email+"'";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                //user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(col_user_1))));
                user.setName(cursor.getString(cursor.getColumnIndex(col_user_2)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(col_user_4)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(col_user_3)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(col_user_5)));
            }
            cursor.close();
            db.close();

        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return user;
    }

    String UserUpdate(int Id, String name, String email, String password,String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values =new ContentValues();
        values.put(col_user_2,name);
        values.put(col_user_3,email);
        values.put(col_user_4,password);
        values.put(col_user_5,phone);

        String whereCalue = col_user_1 + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(Id)};

        long result =  db.update(table_user,values,whereCalue,whereArgs);
        String msg = "Not UPDATED";
        if (result>0)
        {
            msg = "UPDATED Successfully";
        }
        return msg;
    }

    public String UserDelete(int Id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereCalue = col_user_1 + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(Id)};

        long result =  db.delete(table_user,whereCalue,whereArgs);
        String msg = "Not DELETED";
        if (result>0)
        {
            msg = "DELETED Successfully";
        }
        return msg;
    }
}

