package com.nellem.practice04.mains_oneperson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.login.Login;
import com.nellem.practice04.login.LoginRegister;
import com.nellem.practice04.main.Mains_Oneperson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class Oneperson_Post extends AppCompatActivity {

    Handler handler;

    TextView tvRegion, tvTitle, tvWriter, tvDate, tvContent;
    int no = 0;
    String region = null,
            title = null,
            writer = null,
            date = null,
            content = null;
    Button btnModify, btnDelete, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneperson_post);

        actionBar();

        handler = new Handler();
        tvRegion = (TextView)findViewById(R.id.tvRegion);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvWriter = (TextView)findViewById(R.id.tvNo);
        tvDate = (TextView)findViewById(R.id.tvData);
        tvContent = (TextView)findViewById(R.id.tvContent);

        btnModify = (Button)findViewById(R.id.btnModify);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnReturn = (Button)findViewById(R.id.btnReturn);

        no = Mains_Oneperson.selectPostNo;

        postLoad();

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Login.dbId.equalsIgnoreCase(writer)) {
                    Intent intent = new Intent(getApplicationContext(), OnepersonPostModify.class);
                    intent.putExtra("no", no);
                    intent.putExtra("region", region);
                    intent.putExtra("title", title);
                    intent.putExtra("writer", writer);
                    intent.putExtra("date", date);
                    intent.putExtra("content", content);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "본인이 작성한 게시글만 수정이 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDelete();
                finish();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void actionBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.d(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.d(TAG, "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
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
                    buffer.append("no").append("=").append("" + no);
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
                    final String[] sResult = resultData.split("/");
                    region = sResult[1];
                    title = sResult[2];
                    writer = sResult[3];
                    date = sResult[4];
                    content = sResult[5];

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvRegion.setText(region);
                            tvTitle.setText(title);
                            tvWriter.setText(writer);
                            tvDate.setText(date);
                            tvContent.setText(content);
                        }
                    });
                } catch(Exception e) {
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }

    public void postDelete() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://goodmin.dothome.co.kr/php/uploadDelete.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("no").append("=").append("" + no);
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
                    final String[] sResult = resultData.split("/");
                } catch(Exception e) {
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                }
            }
        }.start();
    }
}