package com.nellem.practice04.mains_oneperson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.main.Mains_Oneperson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Oneperson_Post extends AppCompatActivity {

    Handler handler;

    TextView region, title, writer, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneperson_post);
        handler = new Handler();
        region = (TextView)findViewById(R.id.tvRegion);
        title = (TextView)findViewById(R.id.tvTitle);
        writer = (TextView)findViewById(R.id.tvWriter);
        date = (TextView)findViewById(R.id.tvData);
        content = (TextView)findViewById(R.id.tvContent);

        postLoad();
    }

    public void postLoad() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://goodmin.dothome.co.kr/php/postLoad.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("no").append("=").append("" + Mains_Oneperson.selectPostNo);
                    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    osw.write(buffer.toString());
                    osw.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                    }
                    final String resultData = builder.toString();
                    Log.e("Msg", resultData);
                    final String[] sResult = resultData.split("/");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            region.setText(sResult[1]);
                            title.setText(sResult[2]);
                            writer.setText(sResult[3]);
                            date.setText(sResult[4]);
                            content.setText(sResult[5]);
                        }
                    });
                } catch(Exception e) {
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}