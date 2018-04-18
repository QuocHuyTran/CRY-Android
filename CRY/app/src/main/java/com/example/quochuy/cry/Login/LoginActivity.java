package com.example.quochuy.cry.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quochuy.cry.MainActivity.MainActivity;
import com.example.quochuy.cry.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {


    String Token="tokenSecurity";
    EditText edtUsername,edtPassword;
    Button btnLogin,btnExit;
    TextView tvCreateAccount;
    TextView tvWrongAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setID();

    }

    protected void setID(){
        edtUsername=findViewById(R.id.id_edt_username);
        edtPassword=findViewById(R.id.id_edt_pass);
        btnLogin=findViewById(R.id.id_btn_login);
        btnExit=findViewById(R.id.id_btn_exit);
        tvCreateAccount=findViewById(R.id.id_tv_create);
        tvWrongAccount=findViewById(R.id.id_tv_wrong);
        setControl();
    }

    protected void setControl(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkAccont(edtUsername.getText().toString(),edtPassword.getText().toString());
                    if(token!="")
                    {
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("token",token);
                        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                        SharedPreferences sharedPreferences=getSharedPreferences(Token,MODE_PRIVATE);
                        SharedPreferences.Editor editor =sharedPreferences.edit();
                        editor.putString("TOKEN",token);
                        editor.commit();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    String token="";
    protected void checkAccont(final String username, final String password) throws InterruptedException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response="";
                try{
                    URL url =new URL("http://cryws.herokuapp.com/api/accounts/login");
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
                    token=jsonObject.optString("token");
                    Log.e("Result",jsonObject.optBoolean("success")+"");
                }catch(Exception ex)
                {
                    token="";
                    Log.e("Error",ex.toString());
                }

            }
        });
        thread.start();
        thread.join();
    }



}
