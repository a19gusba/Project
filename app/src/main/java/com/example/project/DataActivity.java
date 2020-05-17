package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LongSummaryStatistics;

public class DataActivity extends AppCompatActivity {

    ProgressDialog pd;
    TextView txtJson;

    final ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;

    final ArrayList<String> messageInfo = new ArrayList<>();
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, items);
        adapter2 = new ArrayAdapter<String>(this, R.layout.listview_item, messageInfo);

        ListView view = findViewById(R.id.list_view);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DataActivity.this, messageInfo.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        Button button = findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            Log.d("TAGasd", "before");
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("TAGasd", "after");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("TAGasd", json);
        }
    }
}
