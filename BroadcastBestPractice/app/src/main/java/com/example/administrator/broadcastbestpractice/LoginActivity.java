package com.example.administrator.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private  SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox)findViewById(R.id.remember_pass);
        final boolean isRemember = prefs.getBoolean("remember_password",false);
        if(isRemember){
            String account = prefs.getString("account","");
            String password = prefs.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login = (Button)findViewById(R.id.login);
        Log.i(TAG, "onCreate: show something");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account =  accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Log.i(TAG, "onClick: "+account+"|"+ password);
                if(account.equals("admin") &&  password.equals("123456")){
                    editor = prefs.edit();
                    if(rememberPass.isChecked()){
                        editor.putString("account",account);
                        editor.putString("password",password);
                        editor.putBoolean("remember_password",true);
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"account or password is wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
