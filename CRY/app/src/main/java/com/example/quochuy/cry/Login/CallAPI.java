package com.example.quochuy.cry.Login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Quoc Huy on 4/17/2018.
 */

public class CallAPI {
    public CallAPI(){

    }

    public String POST(){
        String response="";
        try{
            URL url=new URL("http://cryws.herokuapp.com/api/accounts/");
            HttpsURLConnection urlConnection=(HttpsURLConnection) url.openConnection();
            String parameter="username=ronaldo&password=123";
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("User-Agent","RestFUL");
            urlConnection.setRequestProperty("ACCEPT_LANGUAGE","UTF-8");
            urlConnection.setDoOutput(true);
            DataOutputStream dataOutputStream=new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.writeBytes(parameter);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode=urlConnection.getResponseCode();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line="";
            StringBuilder responseOutput=new StringBuilder();

            while((line=bufferedReader.readLine())!= null){
                responseOutput.append(line);
            }
            response=responseOutput.toString();
            bufferedReader.close();

        }catch (Exception e)
        {

        }
        return response;
    }


}
