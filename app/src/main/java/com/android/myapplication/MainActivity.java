package com.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private Button btn_Connect;
    private BluetoothService bluetoothService_obj = null;
    private final Handler mHandler = new Handler() {
        //핸들러의 기능을 수행할 클래스(handleMessage)
        public void handleMessage(Message msg) {
        //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Connect=(Button) findViewById((R.id.bluetooth_connect));
        btn_Connect.setOnClickListener(mClickListener);

        if(bluetoothService_obj==null)
        {
            bluetoothService_obj = new BluetoothService(this,mHandler);
        }

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ( v.getId() ){

                case R.id.bluetooth_connect : //모든 블루투스의 활성화는 블루투스 서비스 객체를 통해 접근한다.

                    if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
                    {

                        bluetoothService_obj.enableBluetooth(); //블루투스 활성화 시작.
                    }
                    else
                    {
                        finish();
                    }
                    break ;
                default: break;
            }//switch
        }
    };

}
