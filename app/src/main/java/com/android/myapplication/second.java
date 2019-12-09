package com.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class second extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private static final boolean D = true;


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private int mSelectedBtn;
    private static final int STATE_SENDING = 1;
    private static final int STATE_NO_SENDING = 2;
    private int mSendingState;

    public static final int MODE_REQUEST = 1;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_WRITE = 2;


    private MainActivity mactivity;

    private ImageButton msetting;

    private Button mbtn1;
    private Button mbtn2;
    private Button mbtn3;

    private RadioGroup Rg;
    private EditText editText;

    private String mtimer = "";
    private int mswitch = 0;
    private int msize = 0;

    public BluetoothService bluetoothService_obj = null;
    private StringBuffer mOutStringBuffer;

    private final Handler mHandler = new Handler() {
        //핸들러의 기능을 수행할 클래스(handleMessage)
        public void handleMessage(Message msg) {
            //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);

            switch (msg.what) {

                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE:" + msg.arg1);

                    switch (msg.arg1) {

                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 성공하였습니다!", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_FAIL:
                            Toast.makeText(getApplicationContext(), "블루투스 연결에 실패하였습니다..", Toast.LENGTH_SHORT).show();

                            /**/
                        case MESSAGE_WRITE:
                            String writeMessage = null;

                            if (mSelectedBtn == 1) {
                                writeMessage = mbtn1.getText().toString();
                                mSelectedBtn = -1;
                            } else if (mSelectedBtn == 2) {
                                writeMessage = mbtn2.getText().toString();
                                mSelectedBtn = -1;
                            } else { // mSelectedBtn = -1 : not selected

                                byte[] writeBuf = (byte[]) msg.obj;
// construct a string from the buffer
                                writeMessage = new String(writeBuf);
                            }

                            break;
                    }


            }


        }
    };


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_small:
                if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) {
                    msize = 0;
                    sendMessage("2" + Integer.toString(msize), MODE_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.set_midium:
                if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) {
                    msize = 1;
                    sendMessage("2" + Integer.toString(msize), MODE_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.set_big:
                if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) {
                    msize = 2;
                    sendMessage("2" + Integer.toString(msize), MODE_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mSelectedBtn = -1;


        mbtn1 = (Button) findViewById(R.id.btn1);
        mbtn1.setOnClickListener(mClickListener);
        mbtn2 = (Button) findViewById(R.id.btn2);
        mbtn2.setOnClickListener(mClickListener);
        mbtn3 = (Button) findViewById(R.id.btn3);
        mbtn3.setOnClickListener(mClickListener);
        Rg = (RadioGroup) findViewById(R.id.rg);
        editText = (EditText) findViewById(R.id.timer);
        msetting = (ImageButton) findViewById(R.id.setting);
        msetting.setOnClickListener(mClickListener);

        if (bluetoothService_obj == null) {
            bluetoothService_obj = MainActivity.getbluetoothservice();
            mOutStringBuffer = new StringBuffer("");
        }

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:

                    if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) { //연결된 상태에서만 값을 보낸다.
                        sendMessage("0" + Integer.toString(msize), MODE_REQUEST);
                        mSelectedBtn = 1;
                    } else {
                        Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn2:

                    if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) {
                        sendMessage("1" + Integer.toString(msize), MODE_REQUEST);
                        mSelectedBtn = 2;
                    } else {
                        Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn3:
                    if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) {
                        if (editText.getText().toString().length() == 0 || editText.getText().toString() == "0") {
                            break;
                        } else {
                            RadioButton rd = (RadioButton) findViewById(Rg.getCheckedRadioButtonId());
                            switch (rd.getId()) {
                                case R.id.radio_on:
                                    mswitch = 0;
                                    break;
                                case R.id.radio_off:
                                    mswitch = 1;
                                    break;
                            }
                            mtimer = (editText.getText().toString());
                            sendMessage(Integer.toString(mswitch) + Integer.toString(msize) + mtimer, MODE_REQUEST);
                        }

                        mSelectedBtn = 3;
                    } else {
                        Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.setting:
                    AlertDialog.Builder dlg = new AlertDialog.Builder(second.this);

                    dlg.setTitle("사이즈 설정");
                    final String[] sizearray = new String[]{"작은스위치", "중간스위치", "큰스위치"};
                    dlg.setPositiveButton("설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (bluetoothService_obj.getState() == BluetoothService.STATE_CONNECTED) { //연결된 상태에서만 값을 보낸다.
                                        Toast.makeText(getApplicationContext(), msize, Toast.LENGTH_SHORT).show();
                                        sendMessage("2" + Integer.toString(msize), MODE_REQUEST);
                                    } else {

                                        Toast.makeText(getApplicationContext(), "2" + Integer.toString(msize), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    dlg.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    dlg.setSingleChoiceItems(sizearray, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (sizearray[i].compareTo("작은스위치") == 0) {
                                msize = 0;
                            } else if (sizearray[i].compareTo("중간스위치") == 0) {
                                msize = 1;
                            } else if (sizearray[i].compareTo("큰스위치") == 0) {
                                msize = 2;
                            }
                        }
                    });
                    dlg.show();

                default:
                    break;
            }//switch
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult" + resultCode);
// TODO Auto-generated method stub

        switch (requestCode) {

            case REQUEST_ENABLE_BT:
//When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    bluetoothService_obj.scanDevice();
                } else {
                    Log.d(TAG, "Bluetooth is not enable");
                }
                break;

            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothService_obj.getDeviceInfo(data);
                }
                break;
        }
    }

    /*메시지를 보낼 메소드 정의*/
    private synchronized void sendMessage(String message, int mode) {

        if (mSendingState == STATE_SENDING) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mSendingState = STATE_SENDING;

// Check that we're actually connected before trying anything
        if (bluetoothService_obj.getState() != BluetoothService.STATE_CONNECTED) {
            mSendingState = STATE_NO_SENDING;
            return;
        }

// Check that there's actually something to send
        if (message.length() > 0) {
// Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bluetoothService_obj.write(send, mode);

// Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);

        }

        mSendingState = STATE_NO_SENDING;
        notify();
    }

}
