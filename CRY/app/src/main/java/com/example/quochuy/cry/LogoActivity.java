package com.example.quochuy.cry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.quochuy.cry.Login.LoginActivity;

import static java.lang.Thread.sleep;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread nextPage=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(5000);
                }catch (Exception e)
                {

                }finally {
                    Intent intent=new Intent(LogoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        nextPage.start();
    }
}
