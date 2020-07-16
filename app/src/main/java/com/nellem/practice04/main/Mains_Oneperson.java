package com.nellem.practice04.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nellem.practice04.R;
import com.nellem.practice04.mains_oneperson.OnepersonAdapter;
import com.nellem.practice04.mains_oneperson.OnepersonWrite;
import com.nellem.practice04.mains_oneperson.Oneperson_LvItem;
import com.nellem.practice04.mains_oneperson.Oneperson_Post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Mains_Oneperson extends AppCompatActivity {
    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    ListView list;
    Button btnSearch, btnWrite, btnReturn;
    EditText etSearch;
    OnepersonAdapter adapter;
    Handler handler;
    String[][] storage;

    String region = null;

    public static int selectPostNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains_oneperson);

        actionBar();

        arrayList = new ArrayList<>();
        arrayList.add("모두");
        arrayList.add("강원");
        arrayList.add("경기");
        arrayList.add("경북");
        arrayList.add("경남");
        arrayList.add("전북");
        arrayList.add("전남");
        arrayList.add("충북");
        arrayList.add("충남");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        handler = new Handler();
        spinner = (Spinner)findViewById(R.id.spinner_oneperson);
        list = (ListView)findViewById(R.id.lvOnePerson);
        etSearch = (EditText)findViewById(R.id.etSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        btnReturn = (Button)findViewById(R.id.btnReturn);
        adapter = new OnepersonAdapter();
        list.setAdapter(adapter);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnepersonAdapter.itemlist.clear();
                region = adapterView.getSelectedItem().toString();
                boardLoad(region, "null");
                // 선택한 콤보박스 출력
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        boardLoad("모두", "null");

        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //OnepersonAdapter.itemlist.clear();
                Oneperson_LvItem item = (Oneperson_LvItem)adapterView.getItemAtPosition(i);
                selectPostNo = Integer.parseInt(item.getNo());
                Intent intent = new Intent(getApplicationContext(), Oneperson_Post.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnepersonAdapter.itemlist.clear();
                String key = etSearch.getText().toString();
                boardLoad(region, key);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OnepersonWrite.class);
                startActivity(intent);
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

    public void boardLoad(final String regionFilter, final String search) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //OnepersonAdapter.itemlist.clear();
                    URL url = new URL("Http://goodmin.dothome.co.kr/php/boardLoad.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("search").append("=").append(regionFilter).append("/").append(search);
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
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch(Exception e) {
                    Log.e("Error", "실행도중 문제가 발생했습니다. 확인 후 수정바랍니다.", e);
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
