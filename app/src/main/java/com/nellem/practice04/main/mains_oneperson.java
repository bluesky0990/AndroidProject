package com.nellem.practice04.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nellem.practice04.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class mains_oneperson extends AppCompatActivity {

    ListView list;
    mains_onepersonAdapter adapter;
    Handler handler;
    String[][] storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains_oneperson);

        handler = new Handler();
        list = (ListView)findViewById(R.id.lvOnePerson);
        adapter = new mains_onepersonAdapter();
        list.setAdapter(adapter);

        boardLoad();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mains_oneperson_LvItem item = (mains_oneperson_LvItem)adapterView.getItemAtPosition(i);

            }
        });
    }

    public void boardLoad() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://10.0.2.2/Travel/boardLoad.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append("name").append("=").append("");
//                    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
//                    osw.write(buffer.toString());
//                    osw.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                    }
                    String resultData = builder.toString();

                    final int firstArraySize = resultData.split("¿").length;
                    final int secondArraySize = 5; // The number of table property
                    storage = new String[firstArraySize][secondArraySize];
                    String[] oneArray = resultData.split("¿");
                    for(int i = 0; i < firstArraySize; i++) {
                        String[] tmps = oneArray[i].split("/");
                        for(int j = 0; j < secondArraySize; j++) {
                            storage[i][j] = tmps[j];
                        }
                    }

                    for(int i = 0; i < storage.length; i++) {
                        adapter.addItem(storage[i][0], storage[i][1], storage[i][2], storage[i][3], storage[i][4]);
                    }
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
