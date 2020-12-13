package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "ACT";
    String text;
    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while((line = reader.readLine())!= null){
                    result.append(line);
                }
            } catch (Exception e){
                e.printStackTrace();
                return null;
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String info = jsonObject.getString("weather");
//                Log.i(TAG,info);
                JSONArray arr = new JSONArray(info);
                for(int i = 0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    text += jsonPart.getString("main");
                    text += jsonPart.getString("description");
                }
                Log.i(TAG,text);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText cityName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button getWeatherInfo = (Button) findViewById(R.id.button);
        TextView weatherText = (TextView) findViewById(R.id.textView2);
        DownloadTask task = new DownloadTask();

        getWeatherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityName.getText().toString();
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=785844eae4c24e45222c639a948fd626";
                Log.i(TAG,url);
                String response = null;
                task.execute("https://api.openweathermap.org/data/2.5/weather?q=Durgapur&appid=785844eae4c24e45222c639a948fd626");

            }
        });

    }
}