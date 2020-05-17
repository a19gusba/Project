package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GOTO data activity
        Button btn_data = findViewById(R.id.btn_data_activity);
        btn_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_data = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent_data);
            }
        });

        // GOTO about activity
        Button btn_about = findViewById(R.id.btn_about_activity);
        btn_about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_about);
            }
        });
    }
}
