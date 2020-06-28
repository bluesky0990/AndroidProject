package com.nellem.practice04.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.main.Mains;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    Handler handler;

    EditText etId, etPw;
    TextView tvRegister;
    Button btnLogin;

    String id;
    String pw;

    String dbId = null;
    String dbPw = null;

    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new Handler();

        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);

        tvRegister = (TextView)findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginRegister.class);
                startActivity(intent);
            }
        });

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = etId.getText().toString();
                pw = etPw.getText().toString();
                dataSelect();
            }
        });
    }

    public void dataSelect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://10.0.2.2/Travel/memberSelect.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id).append("/").append(pw);
                    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    osw.write(buffer.toString());
                    osw.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                    }
                    final String resultData = builder.toString();
                    final String[] sResult = resultData.split("/");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        if(resultData != "") {
                            dbId = sResult[0];
                            dbPw = sResult[1];
                        }

                        if(id.equalsIgnoreCase(dbId) && pw.equalsIgnoreCase(dbPw)) {
                            Intent intent = new Intent(getApplicationContext(), Mains.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해 주십시오.", Toast.LENGTH_SHORT).show();
                            etPw.setText("");
                        }
                        }
                    });
                } catch (Exception e){
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}
