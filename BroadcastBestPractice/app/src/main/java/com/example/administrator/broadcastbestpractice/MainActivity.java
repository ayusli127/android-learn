package com.example.administrator.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.force_offline);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.administrator.broadcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
        Button saveData = (Button) findViewById(R.id.save_data);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data",0).edit();
                editor.putString("name","Tom");
                editor.putBoolean("married",false);
                editor.apply();
            }
        });
        Button restore_data =(Button) findViewById(R.id.restore_data);
        restore_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref =  getSharedPreferences("data",0);
                String name = pref.getString("name","");
                Boolean married = pref.getBoolean("married",false);
                Log.d(TAG, "name is :" + name);
                Log.d(TAG, "married is: " + married);
            }
        });
    }
}
