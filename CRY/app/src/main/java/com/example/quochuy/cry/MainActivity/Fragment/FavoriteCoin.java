package com.example.quochuy.cry.MainActivity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.quochuy.cry.Coin.Coin;
import com.example.quochuy.cry.Coin.CoinAdapter;
import com.example.quochuy.cry.Coin.LastValues;
import com.example.quochuy.cry.CoinAPI;
import com.example.quochuy.cry.DBManager;
import com.example.quochuy.cry.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Quoc Huy on 4/3/2018.
 */

public class FavoriteCoin extends Fragment {
    Button btnBack,btnForward;
    String Token="tokenSecurity";
    ListView lvFavoriteCoin;
    ArrayList<Coin> arrayListCoin=new ArrayList<>();
    CoinAdapter arrayAdapterCoin;
    View rootView;
    DBManager dbManager;
    int maxCoin=10;
    String token="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_favorite,container,false);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(Token, Context.MODE_PRIVATE);
        dbManager=new DBManager(getContext());
        token=sharedPreferences.getString("TOKEN","");
        Log.e("Token",token);
        arrayAdapterCoin=new CoinAdapter(getContext(),R.layout.layout_custom_coin,arrayListCoin);
        setID();
        lvFavoriteCoin.setAdapter(arrayAdapterCoin);
        return rootView;
    }

    protected void setID(){
        btnBack=rootView.findViewById(R.id.id_btn_back);
        btnForward=rootView.findViewById(R.id.id_btn_forward);
        lvFavoriteCoin=rootView.findViewById(R.id.id_lv_favoritecoin);
        setControlButton();
        try {
            setListJSON();
            Log.e("Active","Activated");
        } catch (InterruptedException e) {
            Log.e("Error",e.toString());
        }
    }

    protected void setControlButton(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    String jsonString;
    protected void setListJSON() throws InterruptedException {
        Thread getJSON=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    CoinAPI coinAPI=new CoinAPI();
                    URL url=new URL("http://cryws.herokuapp.com/api/accounts/favorites/");
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization",token);
                    connection.setRequestProperty("User-Agent","RestFUL");
                    Log.e("Code",connection.getResponseCode()+"");
                    if(connection.getResponseCode()==200)
                    {
                        InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                        BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder=new StringBuilder();
                        String line=null;
                        while ((line =bufferedReader.readLine())!=null)
                        {
                            stringBuilder.append(line);
                        }
                        Log.e("Response",stringBuilder.toString());
                        jsonString=stringBuilder.toString();
                    }
                    else {

                    }
                    connection.disconnect();
                }catch (Exception e)
                {
                    Log.e("Error JSON",e.toString());
                }
            }
        });

        getJSON.start();
        getJSON.join();
        setListView(jsonString);
    }

    protected void setListView(String jsonString)
    {
        arrayListCoin.clear();
        try{
            JSONObject jsonObject=new JSONObject(jsonString);
            JSONArray list=jsonObject.optJSONArray("favorites");
            Log.e("list",list.length()+"");
            for(int i=0;i<list.length();i++)
            {
                addListFavorite(list.getString(i));
            }
//            ArrayList<String> coinList=new ArrayList();
        }catch (Exception ex)
        {
            Log.e("Error",ex.toString());
        }
    }

    protected void addListFavorite(final String coin) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    CoinAPI coinAPI=new CoinAPI();
                    URL url=new URL(coinAPI.getCoinDetail(coin));
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-Agent","RestFUL");
                    if(connection.getResponseCode()==200)
                    {
                        InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                        BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder=new StringBuilder();
                        String line=null;
                        while ((line =bufferedReader.readLine())!=null)
                        {
                            stringBuilder.append(line);
                        }
                        Log.e("Response",stringBuilder.toString());
                        jsonString=stringBuilder.toString();
//                         JSONObject jsonObject=new JSONObject(stringBuilder.toString());

                        JSONArray jsonArray=new JSONArray (stringBuilder.toString());
                        JSONObject jsonObject_Child=jsonArray.getJSONObject(0);
                        Coin coin=new Coin(
                                jsonObject_Child.optString("name").toString(),
                                jsonObject_Child.optString("symbol").toString(),
                                jsonObject_Child.optString("available_supply").toString());
                        JSONObject jsonObject_lastvalues=jsonObject_Child.getJSONObject("last_values");
                        LastValues lastValues=new LastValues(
                                jsonObject_lastvalues.optString("timeStamp").toString(),
                                jsonObject_lastvalues.optString("price").toString(),
                                jsonObject_lastvalues.optString("marketcap").toString(),
                                jsonObject_lastvalues.optString("volume24").toString(),
                                jsonObject_lastvalues.optString("change_1h"),
                                jsonObject_lastvalues.optString("change_24h"));
                        coin.setLastValues(lastValues);
                        arrayListCoin.add(coin);
                        arrayAdapterCoin.notifyDataSetChanged();
//                        dbManager.addCoin(coin);
                        Log.e("Count",dbManager.countLanguages()+"");
//                        Log.e("Response",jsonObject_lastvalues.optString("price").toString());
                    }
                }catch (Exception e)
                {
                    Log.e("Error",e.toString());
                }
            }
        });
        thread.start();
        thread.join();

    }

}
