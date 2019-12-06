package com.android.myapplication;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.UUID;

public class BluetoothService extends Service {


    private static final int REQUEST_ENABLE_BT=2;

    private  static  final String TAG= "BluetoothService";
    private static final UUID MY_UUID =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter btAdapter;
    private Activity mActivity;
    private Handler mHandler;

    public BluetoothService(Activity activity, Handler handler)
    {
        mActivity = activity;
        mHandler = handler;

//bluetoothAdapter 얻기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean getDeviceState()
    {
        Log.d(TAG, "Check the Bluetooth support");

        if(btAdapter==null)
        {
            Log.d(TAG, "Bluetooth is not available");
            return false;
        }

        else
        {
            Log.d(TAG, "Bluetooth is available");
            return true;
        }
    }
    public void enableBluetooth()
    {
        Log.i(TAG, "Check the enable Bluetooth");

        if(btAdapter.isEnabled())
        {
//기기의 블루투스 상태가 On일 경우..
            Log.d(TAG, "Bluetooth Enable Now");

        }
        else
        {
//기기의 블루투스 상태가 off일 경우
            Log.d(TAG, "Bluetooth Enable Request");

            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult" + resultCode);
// TODO Auto-generated method stub

        switch(requestCode)
        {

            case REQUEST_ENABLE_BT:
//When the request to enable Bluetooth returns
                if(resultCode != Activity.RESULT_OK) //취소를 눌렀을 때
                {
                    Log.d(TAG, "Bluetooth is not enable");
                }
                break;
        }
    }


}
