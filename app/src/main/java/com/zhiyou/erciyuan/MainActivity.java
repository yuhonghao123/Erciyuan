package com.zhiyou.erciyuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView userNameView;
    private TextView passwordView;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameView = findViewById(R.id.text_userName);
        passwordView = findViewById(R.id.text_password);
        button_login = findViewById(R.id.loginButton);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence userName = userNameView.getText();
                CharSequence password = passwordView.getText();
                send(userName, password);
            }
        });
    }

    private void send(final CharSequence username, final CharSequence password) {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> login = new HashMap<>();
                login.put("username", username.toString());
                login.put("password", password.toString());
                Result<Map> loginResult = OKHttpUtils.post("/login", null, login, Map.class);
                show(loginResult);
            }
        }).start();
    }

    private void show(final Result<Map> loginResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loginResult.isSuccess()) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("以成功登陆")
                            .show();
                    Intent users=new Intent(MainActivity.this,ListActivity.class);
                    Map user = loginResult.getData();

                    String userIdStr = String.valueOf(user.get("userId")); //1.0
                    Double userIdDouble = Double.parseDouble(userIdStr);
                    int userIdInt = userIdDouble.intValue();

                    users.putExtra("userId", userIdInt);
                    //users.putExtra("username", String.valueOf(user.get("username")));
                    startActivity(users);
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("用户名或密码错误，请重新输入")
                            .show();
                }
            }
        });
    }


}
