package com.nellem.practice04.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nellem.practice04.MainActivity;
import com.nellem.practice04.R;
import com.nellem.practice04.main.mains_profile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginRegister extends AppCompatActivity {

    EditText etName, etId, etPw, etEmail;
    Button btnRegister;

    String name;
    String id;
    String pw;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        etName = (EditText) findViewById(R.id.etName);
        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataInsert();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }

    private void dataInsert() {
        new Thread() {
            @Override
            public void run() {
                try {
                    name = etName.getText().toString();
                    id = etId.getText().toString();
                    pw = etPw.getText().toString();
                    email = etEmail.getText().toString();

                    URL url = new URL("http://10.0.2.2/Travel/insert.php/");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("member").append("=").append(name).append("/").
                            append(id).append("/").append(pw).append("/").append(email);

                    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                    osw.write(buffer.toString());
                    osw.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    while (reader.readLine() != null) {
                        System.out.println(reader.readLine());
                    }
                } catch (Exception e) {
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}