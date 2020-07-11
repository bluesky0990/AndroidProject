package com.nellem.practice04.mains_oneperson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nellem.practice04.R;
import com.nellem.practice04.login.Login;
import com.nellem.practice04.main.Mains_Oneperson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OnepersonPostModify extends AppCompatActivity {
    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    EditText etTitle, etContent;
    Button btnModify;
    Handler handler;

    Intent intent;
    int no = 0;
    String region = null,
            title = null,
            writer = null,
            date = null,
            content = null;
    String str = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneperson_post_modify);

        intent = getIntent();
        no = intent.getExtras().getInt("no");
        region = intent.getExtras().getString("region");
        title = intent.getExtras().getString("title");
        writer = intent.getExtras().getString("writer");
        date = intent.getExtras().getString("date");
        content = intent.getExtras().getString("content");
        Log.e("error", no+region+title+writer+date+content);

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

        spinner = (Spinner)findViewById(R.id.spinner_one_post_modi);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        btnModify = (Button)findViewById(R.id.btnModify);
        spinner.setAdapter(arrayAdapter);

        setSpinText(spinner, region);
        etTitle.setText(title);
        etContent.setText(content);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUpdate();
            }
        });
    }

    public void setSpinText(Spinner spin, String text) {
        for(int i= 0; i < spin.getAdapter().getCount(); i++) {
            if(spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }
    public void dataUpdate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    region = spinner.getSelectedItem().toString();
                    title = etTitle.getText().toString();
                    content = etContent.getText().toString();
                    URL url = new URL("Http://goodmin.dothome.co.kr/php/uploadUpdate.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(region).append("/").append(title).append("/").append(content).append("/").append(no);
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
                            Log.e("error", region+title+content);
                            Intent intentTo = new Intent(getApplicationContext(), Mains_Oneperson.class);
                            startActivity(intentTo);
                        }
                    });
                } catch (Exception e){
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}