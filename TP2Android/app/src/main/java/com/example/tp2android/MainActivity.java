package com.example.tp2android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Result{
    public String mAuthenticated;
    public String mUser;

    public String getAuthenticated() {
        return mAuthenticated;
    }

    public void setAuthenticated(String authenticated) {
        mAuthenticated = authenticated;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }
}



public class MainActivity extends AppCompatActivity {

    private EditText mIdEditText;
    private EditText mPwEditText;
    private Button mAuthButton;
    private TextView mAuthTextView;
    private Result mRes;

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.set

    private static String readStream(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("JFL", "IOException", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("JFL", "IOException", e);
            }
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIdEditText=findViewById(R.id.main_edittext_id);
        mPwEditText=findViewById(R.id.main_edittext_pw);
        mAuthButton=findViewById(R.id.main_button_auth);
        mAuthTextView=findViewById(R.id.main_textview_auth);
        Result res = mRes;


        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                try {

                    url = new URL("https://httpbin.org/basic-auth/bob/sympa");

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String basicAuth = "Basic " + Base64.encodeToString((mIdEditText.getText().toString() + ":"+mPwEditText.getText().toString()).getBytes(), Base64.NO_WRAP);
                    urlConnection.setRequestProperty("Authorization", basicAuth);
                    try {

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String s = readStream(in);
                        Log.i("JFL", s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String json = s;
                                try{
                                    JSONObject obj = new JSONObject(json);
                                    Log.d("JFL", obj.toString());

                                } catch (Throwable t){
                                    Log.e("JFL", "Erreur sur le format du JSON");
                                }
                            }
                        });

                    } finally { urlConnection.disconnect();

                    }
                } catch (MalformedURLException e) {

                    e.printStackTrace(); } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });



    }


}