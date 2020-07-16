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
import android.widget.Spinner;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.login.Login;
import com.nellem.practice04.login.LoginRegister;
import com.nellem.practice04.realtime.RealtimeChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Mains_Realtime extends AppCompatActivity {

    private Spinner spinnerRegion, spinnerSex;
    ArrayList<String> arrayListReg, arrayListSex;
    ArrayAdapter<String> arrayAdapter1, arrayAdapter2;
    Button btnStart, btnReturn;
    Handler handler;

    String region, sex;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains_realtime);

        handler = new Handler();
        arrayListReg = new ArrayList<>();
        arrayListReg.add("무관");
        arrayListReg.add("강원");
        arrayListReg.add("경기");
        arrayListReg.add("경북");
        arrayListReg.add("경남");
        arrayListReg.add("전북");
        arrayListReg.add("전남");
        arrayListReg.add("충북");
        arrayListReg.add("충남");
        arrayListSex = new ArrayList<>();
        arrayListSex.add("무관");
        arrayListSex.add("남자");
        arrayListSex.add("여자");

        arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayListReg);
        arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayListSex);

        spinnerRegion = (Spinner)findViewById(R.id.spiRegion);
        spinnerSex = (Spinner)findViewById(R.id.spiSex);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnReturn = (Button)findViewById(R.id.btnReturn);
        spinnerRegion.setAdapter(arrayAdapter1);
        spinnerSex.setAdapter(arrayAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        actionBar();

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sex = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region = spinnerRegion.getSelectedItem().toString();
                sex = spinnerSex.getSelectedItem().toString();


                Intent intent = new Intent(getApplicationContext(), RealtimeChat.class);
                startActivity(intent);

//                if(btnStart.getText().toString().equals("매칭 종료")) {
//                    stopClient();
//                    btnStart.setText("매칭 시작");
//                } else if(btnStart.getText().toString().equals("매칭 시작")) {
//                    startClient();
//                    btnStart.setText("매칭 종료");
//                } else {
//                    Toast.makeText(getApplicationContext(), "문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", Toast.LENGTH_SHORT).show();
//                }
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
}