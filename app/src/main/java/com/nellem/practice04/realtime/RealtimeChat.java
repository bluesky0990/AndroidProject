package com.nellem.practice04.realtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nellem.practice04.R;
import com.nellem.practice04.login.Login;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RealtimeChat extends AppCompatActivity {
    ListView list;
    EditText etMessage;
    Button btnAction, btnSend, btnReturn;
    Socket socket;
    RealtimeChatAdapter realtimeChatAdapter;
    Handler handler;

    String data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_chat);

        list = (ListView)findViewById(R.id.LvChat);
        etMessage = (EditText)findViewById(R.id.etMessage);
        btnAction = (Button)findViewById(R.id.btnActon);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnReturn = (Button)findViewById(R.id.btnReturn);

        realtimeChatAdapter = new RealtimeChatAdapter();
        list.setAdapter(realtimeChatAdapter);

        handler = new Handler();

    }

    @Override
    protected void onStart() {
        super.onStart();

        actionBar();

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = btnAction.getText().toString();
                if(text.equals("연결")) {
                    startClient();
                    btnAction.setText("종료");
                    realtimeChatAdapter.addItem("채팅서버에 연결되었습니다.");
                    realtimeChatAdapter.notifyDataSetChanged();
                } else if(text.equals("종료")) {
                    stopClient();
                    btnAction.setText("연결");
                    RealtimeChatAdapter.itemlist.clear();
                    realtimeChatAdapter.addItem("연결이 종료되었습니다.");
                    realtimeChatAdapter.notifyDataSetChanged();
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = btnAction.getText().toString();

                if(text.equals("연결")) {
                    Toast.makeText(getApplicationContext(), "서버연결 후 전송 가능합니다.", Toast.LENGTH_SHORT).show();
                } else if(text.equals("종료")) {
                    send(etMessage.getText().toString());
                }

            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopClient();
                btnAction.setText("연결");
                finish();
            }
        });
    }

    void startClient(){
        // 연결 시작 코드
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("115.138.121.32", 6112));
                    Log.e("error", "[서버연결 완료] " + socket.getRemoteSocketAddress());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(Login.dbId + "/" + "무관" + "/" + "무관");
                }catch(IOException e){
                    Log.e("error",
                            "[startClient()] 서버 연결중 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
                    e.printStackTrace();
                    if(!socket.isClosed()) { stopClient(); }
                    return;
                }
                receive(); // 서버에서 보낸 데이터 받기
            }
        };
        thread.start();
    }

    void stopClient(){
        // 연결 끊기 코드
        try{
            Log.e("error", "[서버 연결종료]");
            //btnStart.setText("매칭 시작");
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    void receive(){
        // 데이터 받기 코드
        while(true){
            try {
                byte[] byteArr = new byte[100];
                InputStream inputStream = socket.getInputStream();
                int readByteCount = inputStream.read(byteArr);

                if(readByteCount == -1) { throw new IOException();}

                data = new String(byteArr, 0, readByteCount, "UTF-8");

                Log.e("error", "[메세지 수신완료] " + data);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        realtimeChatAdapter.addItem(data);
                        realtimeChatAdapter.notifyDataSetChanged();
                    }
                });
            } catch (IOException e) {
                Log.e("error", "[receive()] 서버 연결중 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
                e.printStackTrace();
                stopClient();
                break;
            }
        }
    }

    void send(final String data){
        // 데이터 전송 코드
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    byte[] byteArr = data.getBytes("UTF-8");
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(byteArr);
                    outputStream.flush();
                    Log.e("error", "[메세지 전송완료]");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            etMessage.setText("");
                        }
                    });
                } catch (IOException e) {
                    Log.e("error", "[send()] 서버 연결중 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
                    e.printStackTrace();
                    stopClient();
                }
            }
        };
        thread.start();
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