package com.example.quochuy.cry.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quochuy.cry.Coin.LastValues;
import com.example.quochuy.cry.Coin.Max7Days_Values;
import com.example.quochuy.cry.CoinAPI;
import com.example.quochuy.cry.DBManager;
import com.example.quochuy.cry.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    String TokenKey="tokenSecurity";
    TextView tvName,tvChange,tvPrice;
    Button back,add;
    private CombinedChart mChart;
    private int MaxChart=9;
    String TOKEN;
    String SYMBOL_CHOOSE;
    DBManager dbManager;

    protected void setID(){
        back=findViewById(R.id.id_btn_back);
        tvName=findViewById(R.id.id_tv_name);
        tvChange=findViewById(R.id.id_tv_change);
        tvPrice=findViewById(R.id.id_tv_price);
        add=findViewById(R.id.id_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(add.getText().toString().equals("add"))
                    {
                        addFavorite(SYMBOL_CHOOSE);
                        add.setText("remove");
                    }
                    else {
                        deleteFavorite(SYMBOL_CHOOSE);
                        add.setText("add");
                    }
                } catch (InterruptedException e) {
                    Log.e("Error","Lỗi");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent=getIntent();
//        Coin coin= (Coin) intent.getSerializableExtra("coin");
        tvName.setText(intent.getStringExtra("name"));
        tvPrice.setText(intent.getStringExtra("price"));
        tvChange.setText(intent.getStringExtra("change"));

    }

    private static String day_chart7days[]=new String[]{"prev7","prev6","prev5","prev4","prev3","prev2",
    "prev1","prev0"};

   protected Max7Days_Values getChart7Days(final String symbol) throws InterruptedException {
//       final float chart7day[]=new float[10];
       final Max7Days_Values max7Days_values=new Max7Days_Values();
       Thread getJSON=new Thread(new Runnable() {
           @Override
           public void run() {
               try{
                   CoinAPI coinAPI=new CoinAPI();
                   URL url=new URL(coinAPI.getCoinDetail_7day(symbol));
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
//                       Log.e("Response",stringBuilder.toString());
//                       jsonString=stringBuilder.toString();
//                             JSONObject jsonObject=new JSONObject(stringBuilder.toString());

                       JSONArray jsonArray=new JSONArray (stringBuilder.toString());
                       JSONObject jsonObject_Child=jsonArray.getJSONObject(0);
//                       JSONArray jsonArray_chart7day=jsonObject_Child.getJSONObject("max7days_values");
//                       Log.e("Response",jsonObject_Child.optString("name").toString());
//                       Log.e("Response",jsonObject_lastvalues.optString("price").toString());
                       JSONObject jsonObject_chart=jsonObject_Child.getJSONObject("max7days_values");
                       LastValues lastValues_7day[]=new LastValues[12];
                       for(int i=0;i<day_chart7days.length;i++)
                       {
                           JSONObject jsonObject_day=(JSONObject) jsonObject_chart.getJSONObject(day_chart7days[i]);
                           lastValues_7day[i]=new LastValues(
                                   jsonObject_day.optString("timeStamp"),
                                   jsonObject_day.optString("price"),
                                   jsonObject_day.optString("marketcap"),
                                   jsonObject_day.optString("volume24"),
                                   jsonObject_day.optString("change_24h"));

                       }

                       max7Days_values.setLastValues(lastValues_7day);
//                       for(int i=0;i<day_chart7days.length;i++)
//                       {
//                           Log.e("day:"+i,lastValues_7day[i].getPrice());
//                       }
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
       return max7Days_values;
//       return chart7day;
   }

   static Max7Days_Values max7Days_values=new Max7Days_Values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        dbManager=new DBManager(getApplicationContext());
        setID();

        Intent intent=getIntent();
        String symbolCoin=intent.getStringExtra("symbol");
        SYMBOL_CHOOSE=intent.getStringExtra("symbol");
        SharedPreferences sharedPreferences=getSharedPreferences(TokenKey,MODE_PRIVATE);
        TOKEN=sharedPreferences.getString("TOKEN","");
        if(dbManager.checkExist(symbolCoin))
        {
            add.setText("remove");
        }
        try {
            max7Days_values = getChart7Days(symbolCoin);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mChart=findViewById(R.id.idCombinedChart);

        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.parseColor("#080051"));
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        YAxis rightAxis=mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(max7Days_values.getMinPrice());
        rightAxis.setTextColor(Color.WHITE);

        YAxis leftAxis=mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(max7Days_values.getMinPrice());
        leftAxis.setTextColor(Color.WHITE);

        final List<String> xLabel=new ArrayList<>();
        for(int i=0;i<day_chart7days.length;i++)
        {
            xLabel.add(day_chart7days[i]);
        }
//        xLabel.add("Jan");
//        xLabel.add("Feb");
//        xLabel.add("Mar");
//        xLabel.add("Apr");
//        xLabel.add("May");
//        xLabel.add("Jun");
//        xLabel.add("Jul");
//        xLabel.add("Aug");
//        xLabel.add("Sep");
//        xLabel.add("Oct");
//        xLabel.add("Nov");
//        xLabel.add("Dec");

        XAxis xAxis=mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Float mang[]=max7Days_values.getArray();

        Log.e("min",max7Days_values.getMinPrice()+"");

        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        CombinedData data=new CombinedData();
        LineData lineData=new LineData();
        lineData.addDataSet((ILineDataSet) dataChart());
//        lineData.setValueTextColor(Color.BLUE);

        data.setData(lineData);

        xAxis.setAxisMaximum(data.getXMax()+0.25f);
        xAxis.setGridColor(Color.RED);
        xAxis.setTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getApplicationContext(),"Value "+
                e.getY()+
                ", index: "+
                h.getX()+
                ", dataset index: "+
                h.getDataSetIndex(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private static DataSet dataChart(){
        LineData d=new LineData();
//        int[] data=new int[]{1,2,3,1,1,1,2,1,1,2,1,9};
        Float[] data= new Float[12];
        data=max7Days_values.getArray();
        ArrayList <Entry> entries=new ArrayList<>();
        for(int index=0;index<day_chart7days.length;index++)
        {
            entries.add(new Entry(index,data[index]));
        }

        LineDataSet set=new LineDataSet(entries,"Request");

        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }

    protected void addFavorite(final String symbol) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response="";
                try{
                    URL url =new URL("http://cryws.herokuapp.com/api/accounts/favorites/");
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();

                    String urlParameter= "favorites[]="+symbol;
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("USER-AGENT","RESTFUL");
                    connection.setRequestProperty("Authorization",TOKEN);
                    Log.e("Token",TOKEN);
                    connection.setDoOutput(true);
                    DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());

                    dataOutputStream.writeBytes(urlParameter);
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line="";
                    StringBuilder responseOutput=new StringBuilder();

                    while((line=bufferedReader.readLine())!=null)
                    {
                        responseOutput.append(line);
                    }
                    bufferedReader.close();
                    Log.e("Response",responseOutput.toString());
                }catch (Exception e)
                {
                    Log.e("Error JSON",e.toString());
                }

            }
        });
        thread.start();
        thread.join();
    }

    protected void deleteFavorite(final String symbol) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response="";
                try{
                    URL url =new URL("http://cryws.herokuapp.com/api/accounts/favorites/"+symbol);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();

//                    String urlParameter= "favorites[]="+symbol;
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("USER-AGENT","RESTFUL");
                    connection.setRequestProperty("Authorization",TOKEN);
                    Log.e("Token",TOKEN);

//                    connection.setDoOutput(true);
//                    DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());
//
//                    dataOutputStream.writeBytes(urlParameter);
//                    dataOutputStream.flush();
//                    dataOutputStream.close();

                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line="";
                    StringBuilder responseOutput=new StringBuilder();

                    while((line=bufferedReader.readLine())!=null)
                    {
                        responseOutput.append(line);
                    }
                    bufferedReader.close();
                    Log.e("Response",responseOutput.toString());
                }catch (Exception e)
                {
                    Log.e("Error JSON",e.toString());
                }

            }
        });
        thread.start();
        thread.join();
        dbManager.deleteCoin(symbol);
        Log.e("Count",dbManager.countLanguages()+"");
    }
}
