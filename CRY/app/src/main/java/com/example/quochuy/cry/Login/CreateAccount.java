package com.example.quochuy.cry.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quochuy.cry.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccount extends AppCompatActivity {

    EditText edtUsername,edtPassword;
    Button btnCreate,btnBack;
    TextView tvWrongAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setID();
    }

    protected void setID(){
        edtUsername=findViewById(R.id.id_edt_username);
        edtPassword=findViewById(R.id.id_edt_pass);
        btnCreate=findViewById(R.id.id_btn_create);
        btnBack=findViewById(R.id.id_btn_back);
        tvWrongAlert=findViewById(R.id.id_tv_wrong);
        setControl();
    }

    protected void setControl(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccont(edtUsername.getText().toString(),edtPassword.getText().toString());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateAccount.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void createAccont(final String username, final String password)
    {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response="";
                try{
                    URL url =new URL("http://cryws.herokuapp.com/api/accounts/");
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();

                    String urlParameter="username="+username+"&password="+password;
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("USER-AGENT","RESTFUL");

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
                    JSONObject jsonObject=new JSONObject(responseOutput.toString());
                    Log.e("Result",jsonObject.optBoolean("success")+"");
                    if(jsonObject.optBoolean("success"))
                    {
                        Intent intent=new Intent(CreateAccount.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                    }
                }catch(Exception ex)
                {
                    Log.e("Error",ex.toString());
                }

            }
        });
        thread.start();
    }
}
