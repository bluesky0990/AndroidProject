package com.nellem.practice04.mains_oneperson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.login.Login;
import com.nellem.practice04.main.Mains;
import com.nellem.practice04.main.Mains_Oneperson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OnepersonWrite extends AppCompatActivity {
    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    EditText etTitle, etContent;
    Button btnWrite;
    String str;
    Handler handler;

    String region = null;
    String title = null;
    String content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneperson_write);
        handler = new Handler();
        arrayList = new ArrayList<>();
        arrayList.add("강원");
        arrayList.add("경기");
        arrayList.add("경북");
        arrayList.add("경남");
        arrayList.add("충북");
        arrayList.add("충남");
        arrayList.add("전북");
        arrayList.add("전남");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner)findViewById(R.id.spinner);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region = spinner.getSelectedItem().toString();
                title = etTitle.getText().toString();
                content = etContent.getText().toString();
                dataInsert();
                Intent intent = new Intent(getApplicationContext(), Mains_Oneperson.class);
                startActivity(intent);
            }
        });
    }

    public void dataInsert() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://goodmin.dothome.co.kr/php/uploadWrite.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("insert").append("=").append(region).append("/").append(title).append("/").append(Login.dbId).append("/").append(content);
                    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    osw.write(buffer.toString());
                    osw.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                    }
//                    final String resultData = builder.toString();
//                    final String[] sResult = resultData.split("/");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e){
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}