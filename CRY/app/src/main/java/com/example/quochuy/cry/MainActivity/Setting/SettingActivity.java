package com.example.quochuy.cry.MainActivity.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quochuy.cry.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingActivity extends AppCompatActivity {
    String TokenKey="tokenSecurity";
    String TOKEN;
    Button btnSave,btnBack;
    EditText edtUsername,edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setID();
    }

    protected void setID(){
        btnBack=findViewById(R.id.id_btn_back);
        btnSave=findViewById(R.id.id_btn_save);
        edtUsername=findViewById(R.id.id_edt_username);
        edtPassword=findViewById(R.id.id_edt_pass);
        setControlButton();
        SharedPreferences sharedPreferences=getSharedPreferences(TokenKey,MODE_PRIVATE);
        TOKEN=sharedPreferences.getString("TOKEN","");
    }

    protected void setControlButton(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().equals(edtPassword.getText().toString()))
                {
                    try {
//                        Log.e("pass",edtUsername.getText().toString());
                        editPass(edtPassword.getText().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(SettingActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void editPass(final String pass) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response="";
                try{
                    URL url =new URL("http://cryws.herokuapp.com/api/accounts/password/"+pass);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();

//                    String urlParameter= "favorites[]="+symbol;
                    connection.setRequestMethod("PUT");
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
    }
}
