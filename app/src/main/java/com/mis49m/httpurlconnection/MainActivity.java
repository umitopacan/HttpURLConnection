package com.mis49m.httpurlconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvContent;
    EditText etUrl;
    ImageView imageView;
    Button btnImage, btnUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tvContent = (TextView) findViewById(R.id.tv_content);
        etUrl = (EditText) findViewById(R.id.et_url);
        imageView = (ImageView) findViewById(R.id.image);
        btnImage = (Button) findViewById(R.id.btn_image);
        btnUrl = (Button) findViewById(R.id.btn_url);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUrl();
            }
        });

    }

    public void getImage() {
        String urlStr = etUrl.getText().toString();
        URL url;
        HttpURLConnection conn=null;
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();

            bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn!=null) conn.disconnect();
        }
    }


    public void getUrl() {
        String urlStr = etUrl.getText().toString();
        URL url;
        HttpURLConnection conn=null;
        InputStream inputStream = null;
        InputStreamReader reader = null;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); //maximum time to wait for an input stream read
            conn.setConnectTimeout(15000); //maximum time to wait while connecting
            conn.setRequestMethod("GET");
            conn.setDoInput(true); //whether this URLConnection allows receiving data
            conn.connect();

            inputStream = conn.getInputStream();
            reader = new InputStreamReader(inputStream);

            StringBuilder response = new StringBuilder();
            int data = reader.read();
            while(data!=-1){
                response.append( (char)data );
                data = reader.read();
            }

            tvContent.setText(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            try {
                if(reader!=null) reader.close();
                if(inputStream!=null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn!=null) conn.disconnect();

        }
    }



}
