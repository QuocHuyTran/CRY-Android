package com.example.quochuy.cry.MainActivity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.quochuy.cry.Coin.Coin;
import com.example.quochuy.cry.Coin.CoinAdapter;
import com.example.quochuy.cry.Coin.LastValues;
import com.example.quochuy.cry.CoinAPI;
import com.example.quochuy.cry.MainActivity.Main3Activity;
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

public class AllCoin extends Fragment {
    View rootView;
    Button btnBack,btnForward;
    ListView lvAllCoin;

    private int EndCoin=39;// end position
    private int StartCoin=20;// start position
    private int limitCoin=150;//Max Coin
    private int numberCoinCall=20;//the number of coin at every call
    private ArrayList<Coin> arrayList_coin=new ArrayList<>();
    private CoinAdapter coinAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_allbitcoin,container,false);
        setID();
        try {
            setListJSON(StartCoin,EndCoin);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coinAdapter=new CoinAdapter(getContext(),R.layout.layout_custom_coin,arrayList_coin);
        lvAllCoin.setAdapter(coinAdapter);
        setListView(jsonString);
        return rootView;
    }

    protected void setID(){
        btnBack=rootView.findViewById(R.id.id_btn_back);
        btnForward=rootView.findViewById(R.id.id_btn_forward);
        lvAllCoin=rootView.findViewById(R.id.id_lv_allcoin);
        lvAllCoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), Main3Activity.class);
                intent.putExtra("symbol",arrayList_coin.get(position).getSymbol());
                intent.putExtra("name",arrayList_coin.get(position).getNameCoin());
                intent.putExtra("price",arrayList_coin.get(position).getLastValues().getPrice().toString());
                intent.putExtra("change",arrayList_coin.get(position).getLastValues().getPrice().toString());
//                intent.putExtra("coin",arrayList_coin.get(position));
                startActivity(intent);
            }
        });
        setControlButton();
    }

    protected void setControlButton(){
     btnForward.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             StartCoin+=numberCoinCall;
             EndCoin+=numberCoinCall;
             btnBack.setEnabled(true);btnForward.setEnabled(true);
             if(EndCoin>limitCoin)
             {
                 btnForward.setEnabled(false);
             }
             try {
                 setListJSON(StartCoin,EndCoin);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             setListView(jsonString);
//             Toast.makeText(getContext(), jsonString, Toast.LENGTH_SHORT).show();
         }
     });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartCoin-=numberCoinCall;
                EndCoin-=numberCoinCall;
                btnBack.setEnabled(true);btnForward.setEnabled(true);
                if(StartCoin  < numberCoinCall)
                {
                    btnBack.setEnabled(false);
                }
                try {
                    setListJSON(StartCoin,EndCoin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setListView(jsonString);
            }
        });
    }
    String jsonString;
    protected void setListJSON(final int start, final int end) throws InterruptedException {
        Thread getJSON=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    CoinAPI coinAPI=new CoinAPI();
                    URL url=new URL(coinAPI.getCoinLimit(start,end));
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
//                        Log.e("Response",stringBuilder.toString());
                        jsonString=stringBuilder.toString();
//                             JSONObject jsonObject=new JSONObject(stringBuilder.toString());

                        JSONArray jsonArray=new JSONArray (stringBuilder.toString());
                        JSONObject jsonObject_Child=jsonArray.getJSONObject(0);
                        JSONObject jsonObject_lastvalues=jsonObject_Child.getJSONObject("last_values");
//                        Log.e("Response",jsonObject_Child.optString("name").toString());
//                        Log.e("Response",jsonObject_lastvalues.optString("price").toString());
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
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    CoinAPI coinAPI=new CoinAPI();
//                    URL url=new URL(coinAPI.getCoinLimit(start,end));
//                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("User-Agent","RestFUL");
//                    if(connection.getResponseCode()==200)
//                    {
//                        InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
//                        BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
//                        StringBuilder stringBuilder=new StringBuilder();
//                        String line=null;
//                        while ((line =bufferedReader.readLine())!=null)
//                        {
//                            stringBuilder.append(line);
//                        }
//                             Log.e("Response",stringBuilder.toString());
//                        jsonString=stringBuilder.toString();
////                             JSONObject jsonObject=new JSONObject(stringBuilder.toString());
//
//                        JSONArray jsonArray=new JSONArray (stringBuilder.toString());
//                        JSONObject jsonObject_Child=jsonArray.getJSONObject(0);
//                        JSONObject jsonObject_lastvalues=jsonObject_Child.getJSONObject("last_values");
//                        Log.e("Response",jsonObject_Child.optString("name").toString());
//                        Log.e("Response",jsonObject_lastvalues.optString("price").toString());
//                    }
//                    else {
//
//                    }
//                    connection.disconnect();
//                }catch (Exception e)
//                {
//                    Log.e("Error JSON",e.toString());
//                }
//            }
//        });
    }

    protected void setListView(String json)
    {
        arrayList_coin.clear();
        try{
            JSONArray jsonArray=new JSONArray (json.toString());
            for(int i=0;i<numberCoinCall;i++)
            {
                JSONObject jsonObject_Child=jsonArray.getJSONObject(i);
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
//                Log.e("Index",coin.getLastValues().getChange_24h()+"");
                arrayList_coin.add(coin);
                coinAdapter.notifyDataSetChanged();
            }
//            coinAdapter=new CoinAdapter(getContext(),R.layout.layout_custom_coin,arrayList_coin);
        }catch (Exception e)
        {
            Log.e("Error ListView",e.toString());
        }
    }

}
