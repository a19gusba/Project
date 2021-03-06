package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    final ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;

    final ArrayList<String> messageInfo = new ArrayList<>();
    ArrayAdapter<String> adapter2;

    ImageView mountainImage;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?login=a19gusba");

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, items);
        adapter2 = new ArrayAdapter<String>(this, R.layout.listview_item, messageInfo);

        mountainImage = (ImageView)findViewById(R.id.mountain_img);

        ListView view = findViewById(R.id.list_view);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DataActivity.this, messageInfo.get(position), Toast.LENGTH_SHORT).show();
                loadImage(imageUrl);
            }

            private void loadImage(String imageUrl) {

            }
        });

        // Close activity
        Button close = findViewById(R.id.close_data_activity);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                items.clear();
                messageInfo.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final String name = jsonObject.getString("name");
                    final String location = jsonObject.getString("location");
                    final String company = jsonObject.getString("company");
                    final String cost = jsonObject.getString("cost");
                    final String size = jsonObject.getString("size");
                    imageUrl = jsonObject.getString("auxdata");
                    final String message = "Name: " + name + ", Company: " + company + ", Cost: $" + cost + ", Store: " + location + ",  Size: " + size + "mm";

                    items.add(name);
                    messageInfo.add(message);
                }
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
