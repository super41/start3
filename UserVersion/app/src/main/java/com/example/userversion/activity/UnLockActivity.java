package com.example.userversion.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.userversion.PublicDefine;
import com.example.userversion.R;
import com.example.userversion.Util.SocketUtil;
import com.example.userversion.Util.WifiUtil;
import com.example.userversion.bean.Package;

import java.util.Arrays;

/**
 * Created by XJP on 2018/1/21.
 */
public class UnLockActivity extends AppCompatActivity implements SocketUtil.SocketImp {

    String TAG="UnLockActivity";
    //广播接收
    WifiReceiver wifiReceiver;

    //最下面的重试按钮
    Button btn_retry;
    ConnectivityManager connec;

    //两个提示TextView
    TextView tv_status;
    TextView tv_reason;

    Package pg;
    //WIFI工具类
    WifiUtil wifiUtil;
    HandlerThread handlerThread;
    Handler handler;

    //Socket工具类
    SocketUtil socketUtil;

    //主线程的handler
    Handler mMainHanler;
    public final int MSG_CONNECT = 0;
    public final int MSG_DELAYSHOW = 1;

    public final String WIFI_STATE_CHANGE_ACTION = WifiManager.WIFI_STATE_CHANGED_ACTION;
    public final String NETWORK_STATE_CHANGED_ACTION = WifiManager.NETWORK_STATE_CHANGED_ACTION;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        initView();

    }

    void initView(){
        pg = getPackage();
        wifiReceiver = new WifiReceiver();
        btn_retry = (Button) findViewById(R.id.btn_retry);
        tv_reason= (TextView) findViewById(R.id.tv_reason);
        tv_status= (TextView) findViewById(R.id.tv_status);

        connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);



        wifiUtil = new WifiUtil(UnLockActivity.this);
        handlerThread = new HandlerThread("unlock");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_CONNECT:
                        connect();
                        break;

                }
            }
        };

        mMainHanler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_DELAYSHOW:
                        btn_retry.setAlpha(1.0f);
                        btn_retry.setEnabled(true);
                        tv_status.setText(R.string.connection_failed);
                        tv_reason.setText(R.string.reason);
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PublicDefine.WIFI_CONNECTTING_ACTION);
        intentFilter.addAction(WIFI_STATE_CHANGE_ACTION);
        intentFilter.addAction(NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);

        socketUtil = new SocketUtil(this, this);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                        &&
                        TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())
                        ){
                    Log.d(TAG, "onClick: connecting pg..."+wifiUtil.getSSID());
                    //连上了，则建立连接
                    setConnecting();
                    socketUtil.connect();
                }else{
                    Intent intent = new Intent();
                    intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                    startActivity(intent);
                }


            }
        });

    }

    public static void startTrans(Context context, Package pg) {
        Intent intent = new Intent(context, UnLockActivity.class);
        intent.putExtra("pg_data", pg);
        context.startActivity(intent);
    }
    public Package getPackage() {
        Package pg = (Package) getIntent().getSerializableExtra("pg_data");
        return pg;
    }

    public void connect() {
        wifiUtil.connect(wifiUtil.createWifiInfo(PublicDefine.SSID, PublicDefine.PASSWORD, PublicDefine.TYPE));
    }

    @Override
    public void onSuccess() {
        setConnecting(6000);
        sendUnlock();
    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onResult(byte[] s){
            if(checkUnlock(s)){
                Intent intent = new Intent(UnLockActivity.this, UnLockSuccessActivity.class);
                startActivity(intent);
                finish();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果已经连接WIFI，但是WIFI不是SmartaBox，断开连接重连
        if(connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                &&
                TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())
                ){
            setConnecting();
            socketUtil.connect();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        socketUtil.setDone();
    }

    void sendUnlock() {
        String s = pg.getQrCode() + "op";
        socketUtil.send(s, 0x07);

    }

    boolean checkUnlock(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x07);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    byte[] getTrueReplyByte(String s, int commond) {
        int length = s.length() + 9;
        Log.d(TAG, "getTrueReplyByte: " + length);
        byte[] trueByte = new byte[length];
        trueByte[0] = (byte) 0xFF;
        trueByte[1] = (byte) 0xAA;
        trueByte[2] = (byte) (length - 3);
        trueByte[3] = (byte) commond;

        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            trueByte[4 + i] = (byte) c[i];
        }
        trueByte[s.length() + 4] = (byte) 0x4F;
        trueByte[s.length() + 5] = (byte) 0x4B;
        byte sum = 0;
        for (int i = 0; i < s.length() + 6; i++) {
            sum += trueByte[i];
        }
        trueByte[s.length() + 6] = (byte) sum;
        trueByte[s.length() + 7] = (byte) 0xFF;
        trueByte[s.length() + 8] = (byte) 0x55;
        return trueByte;
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case PublicDefine.WIFI_CONNECTTING_ACTION:
//                    setConnecting();
                    break;

                case WIFI_STATE_CHANGE_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                        Log.i(TAG, "正在关闭");
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
                        Log.i(TAG, "正在打开");

                    } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {

                        Log.i(TAG, "已经关闭");
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {

                    } else {
                        Log.i(TAG, "未知状态");
                    }
                    break;
                case NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info == null) {
                        return;
                    }
                    NetworkInfo.State state = info.getState();
                    //CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
                    switch (state) {
                        case DISCONNECTING:
                            Log.i(TAG, "onReceive: 正在断开");
                            btn_retry.setText(getString(R.string.connect_wifi));
                            break;
                        case DISCONNECTED:
                            if (wifiUtil.isOpen()) {
                                Log.i(TAG, "onReceive: 已经打开,未连接");
                            }
                            btn_retry.setText(getString(R.string.connect_wifi));
                            break;
                        case CONNECTING:
                            Log.i(TAG, "onReceive: 正在连接");
                            btn_retry.setText(getString(R.string.connect_wifi));
                            break;
                        case CONNECTED:

                            Log.i(TAG, "onReceive: " + wifiUtil.getSSID());
                            Log.i(TAG, "onReceive: 已连接");
                            if (TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())) {
                                btn_retry.setText(getString(R.string.connect_socket));
                            } else {
                                btn_retry.setText(getString(R.string.connect_wifi));
                            }
                            Log.i("xjp", "onReceive: " + wifiUtil.getSSID());
                            break;
                        default:
                            break;
                    }

                default:
                    break;

            }
        }
    }

    private  void setConnecting(){
        mMainHanler.removeMessages(MSG_DELAYSHOW);
        tv_status.setText(R.string.wifi_connecting);
        tv_reason.setText(R.string.please_colse_packae);
        btn_retry.setAlpha(0.4f);
        btn_retry.setEnabled(false);
        mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW),12000);
    }

    private  void setConnecting(long TimeOut){
        mMainHanler.removeMessages(MSG_DELAYSHOW);
        tv_status.setText(R.string.wifi_connecting);
        tv_reason.setText(R.string.please_colse_packae);
        btn_retry.setAlpha(0.4f);
        btn_retry.setEnabled(false);
        mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW),TimeOut);
    }
}
