package com.example.quochuy.cry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quochuy.cry.Coin.Coin;

import java.util.ArrayList;

/**
 * Created by Quoc Huy on 4/17/2018.
 */

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="COIN_DATABASE";
    private static final String TABLE_NAME="COIN";
    private static final String SYMBOL="SYMBOL";
    private static final String NAME="NAME";
    private static final String PRICE="PRICE";
    private static final String CHANGE="CHANGE";

    private Context context;

    public DBManager (Context context){
        super(context,DATABASE_NAME,null,1);
        Log.d("DBManager","DAManager:");
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery="CREATE TABLE "+TABLE_NAME+"("+
                SYMBOL+" TEXT PRIMARY KEY, "+
                NAME+" TEXT,"+
                PRICE+" TEXT,"+
                CHANGE+" TEXT)";
        db.execSQL(sqlQuery);
        Log.e("Status","Success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void addCoin(Coin coin)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(SYMBOL,coin.getSymbol().toString());
        contentValues.put(NAME,coin.getNameCoin().toString());
        contentValues.put(PRICE,coin.getLastValues().getPrice().toString());
        contentValues.put(CHANGE,coin.getLastValues().getChange_24h());

        database.insert(TABLE_NAME,null,contentValues);
        database.close();
    }

    public void deleteCoin(String symbol)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.delete(TABLE_NAME,SYMBOL+" = ?",new String[]{String.valueOf(symbol)});
        database.close();
    }

    public ArrayList<String> getAllCoin(){
        ArrayList<String> arrayList=new ArrayList<>();
        String selectQuery="SELECT * FROM "+TABLE_NAME;

        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst())
        {
            do{
                arrayList.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return arrayList;
    }

    public int countLanguages(){
        ArrayList arrayList=this.getAllCoin();
        return arrayList.size();
    }

    public Boolean checkExist(String symbol)
    {
        Boolean isExists=false;
        ArrayList arrayList=this.getAllCoin();
        for(int i=0;i<arrayList.size();i++)
        {
            if(arrayList.get(i).equals(symbol))
            {
                isExists=true;
                break;
            }
        }
        return isExists;
    }
}
