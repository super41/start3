package com.example.userversion.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.PublicDefine;
import com.example.userversion.R;
import com.example.userversion.Util.SocketUtil;
import com.example.userversion.Util.Util;
import com.example.userversion.Util.WifiUtil;
import com.example.userversion.bean.Package;

import java.util.Arrays;

/**
 * Created by XJP on 2017/11/14.
 */
public class WifiConnectActivity extends AppCompatActivity implements SocketUtil.SocketImp {

    //两个提示TextView
    TextView tv_status;
    TextView tv_reason;

    //WIFI工具类
    WifiUtil wifiUtil;
    //wifi连接工作线程
    HandlerThread handlerThread;
    Handler handler;
    Handler mMainHanler;
    public final int MSG_CONNECT = 0;
    public final int MSG_DELAYSHOW = 1;
    //广播接收
    WifiReceiver wifiReceiver;

    //Socket工具类
    SocketUtil socketUtil;

    //最下面的重试按钮
    Button btn_retry;

    //wifi 状态信息
    int wifi_close = R.string.wifi_close;
    int wifi_opening = R.string.wifi_opening;
    int wifi_already_open = R.string.wifi_already_open;
    int wifi_disconnecting = R.string.wifi_disconnecting;
    int wifi_open_not_connect = R.string.wifi_open_not_connected;
    int wifi_connected_wrong = R.string.wifi_connected_wrong;
    int wifi_connected_right = R.string.wifi_connected_right;
    int wifi_connected = R.string.wifi_connected;
    int wifi_connecting = R.string.wifi_connecting;
    Package pg;
    int STEP = 1;
    int EmailCount = 1;
    int EmailSTEP = 1;
    int AllCount=6;
    ProgressDialog p ;
    ProgressDialog p2;

    ConnectivityManager connec;


    public final String TAG = this.getClass().getSimpleName();
    public final String WIFI_STATE_CHANGE_ACTION = WifiManager.WIFI_STATE_CHANGED_ACTION;
    public final String NETWORK_STATE_CHANGED_ACTION = WifiManager.NETWORK_STATE_CHANGED_ACTION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_new);
        initView();

    }

    public void connect() {
        wifiUtil.connect(wifiUtil.createWifiInfo(PublicDefine.SSID, PublicDefine.PASSWORD, PublicDefine.TYPE));
    }

    public void initView() {
        pg = getPackage();
        p=new ProgressDialog(this);
        p2=new ProgressDialog(this);
        connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        p2.setMax(100);
        p.setCancelable(false);
        p2.setCancelable(false);
        p.setMessage(getString(R.string.connect_package));
        p2.setMessage(getString(R.string.loading));
        EmailCount = Util.getPgEmailSize(pg);
        AllCount = EmailCount + 5;

        tv_reason= (TextView) findViewById(R.id.tv_reason);
        tv_status= (TextView) findViewById(R.id.tv_status);
        btn_retry = (Button) findViewById(R.id.btn_retry);

        wifiUtil = new WifiUtil(WifiConnectActivity.this);
        handlerThread = new HandlerThread("connect");
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
                        p.dismiss();
                        p2.dismiss();
                        break;
                }
            }
        };

        wifiReceiver = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PublicDefine.WIFI_CONNECTTING_ACTION);
        intentFilter.addAction(WIFI_STATE_CHANGE_ACTION);
        intentFilter.addAction(NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);

        socketUtil = new SocketUtil(this, this);


        String code = pg.getQrCode();
        if (code != null) {
//            tv_qrcode.setText(code);
        }


        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有连接上目标wifi,请尝试重连
            /*    if (!TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())) {
                    wifiUtil.removeNowConnectingID();
                    handler.sendMessage(handler.obtainMessage(MSG_CONNECT));
                } else {
                    Log.d(TAG, "onClick: "+wifiUtil.getSSID());
                    //连上了，则建立连接
                    btn_retry.setEnabled(false);
                    btn_retry.setAlpha(0.4f);
                    p.setMessage(getString(R.string.connect_package));
                    p.show();
                    mMainHanler.removeMessages(MSG_DELAYSHOW);
                    mMainHanler.sendMessageDelayed(handler.obtainMessage(MSG_DELAYSHOW), 5000);
                    socketUtil.connect();
                }*/


                if ((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                        &&
                        TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())
                      ){
                    Log.d(TAG, "onClick: connecting pg..."+wifiUtil.getSSID());
                    //连上了，则建立连接
                    btn_retry.setEnabled(false);
                    btn_retry.setAlpha(0.4f);
                    tv_status.setText(R.string.wifi_connecting);
                    tv_reason.setText(R.string.please_colse_packae);
                    p2.dismiss();
                    p.show();
                    mMainHanler.removeMessages(MSG_DELAYSHOW);
                    mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW), 8000);
                    socketUtil.connect();
                }else if( connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ){
                    Log.d(TAG, "onClick:  wifi reConnecting...");
                    wifiUtil.removeNowConnectingID();
                    handler.removeMessages(MSG_CONNECT);
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_CONNECT),300);
                }else{
                    Log.d(TAG, "onClick:  wifi connecting...");
//                    wifiUtil.removeNowConnectingID();
                    handler.removeMessages(MSG_CONNECT);
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_CONNECT),300);
                }


            }
        });

    }

    void sendEmail1() {
        String s = pg.getQrCode() + pg.getEmail1();
        socketUtil.send(s, 0x01);

    }

    void sendEmail2() {
        String s = pg.getQrCode() + pg.getEmail2();
        socketUtil.send(s, 9);


        int progress=(100/AllCount)*2;
        setTranProgress(progress);
    }

    void sendEmail3() {
        String s = pg.getQrCode() + pg.getEmail3();
        socketUtil.send(s, 10);

        int progress=(100/AllCount)*3;
        setTranProgress(progress);
    }

    void sendEmail4() {
        String s = pg.getQrCode() + pg.getEmail4();
        socketUtil.send(s, 11);

        int progress=(100/AllCount)*4;
        setTranProgress(progress);
    }

    void sendEmail5() {
        String s = pg.getQrCode() + pg.getEmail5();
        socketUtil.send(s, 12);

        int progress=(100/AllCount)*5;
        setTranProgress(progress);
    }

    void sendEmail6() {
        String s = pg.getQrCode() + pg.getEmail6();
        socketUtil.send(s, 13);

        int progress=(100/AllCount)*6;
        setTranProgress(progress);
    }

    void sendEmail7() {
        String s = pg.getQrCode() + pg.getEmail7();
        socketUtil.send(s, 14);

        int progress=(100/AllCount)*7;
        setTranProgress(progress);
    }

    void sendEmail8() {
        String s = pg.getQrCode() + pg.getEmail8();
        socketUtil.send(s, 15);

        int progress=(100/AllCount)*8;
        setTranProgress(progress);
    }

    void sendEmail9() {
        String s = pg.getQrCode() + pg.getEmail9();
        socketUtil.send(s, 16);

        int progress=(100/AllCount)*9;
        setTranProgress(progress);
    }

    void sendEmail10() {
        String s = pg.getQrCode() + pg.getEmail10();
        socketUtil.send(s, 17);

        int progress=(100/AllCount)*10;
        setTranProgress(progress);
    }


    void sendSecond() {
        String s = pg.getQrCode() + pg.getWifiId();
        socketUtil.send(s, 0x02);

        int progress=(100/AllCount)*(AllCount-4);
        setTranProgress(progress);
    }


    void sendThree() {
        String s = pg.getQrCode() + pg.getWifiPsw();
        socketUtil.send(s, 0x03);

        int progress=(100/AllCount)*(AllCount-3);
        setTranProgress(progress);
    }

    void sendFour() {
        String s = pg.getQrCode() + pg.getDistance();
        socketUtil.send(s, 0x04);

        int progress=(100/AllCount)*(AllCount-2);
        setTranProgress(progress);
    }

    void sendFive() {
        String s = pg.getQrCode() + pg.getEmailTitle();
        socketUtil.send(s, 0x05);

        int progress=(100/AllCount)*(AllCount-1);
        setTranProgress(progress);
    }

    void sendSix() {
        String s = pg.getQrCode() + pg.getEmailContent();
        socketUtil.send(s, 0x06);

        int progress=99;
        setTranProgress(progress);
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

    void send() {
        SharedPreferences sharedPreference = getSharedPreferences(PublicDefine.SP_INFO, MODE_PRIVATE);
        String email = sharedPreference.getString("email", "");
        String code = getIntent().getStringExtra("qr_code");
        String s = code + "000000" + email;
        socketUtil.send(s, 0x01);
        byte[] b = SocketUtil.getByte(s, 1);

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex = hex.toUpperCase();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果已经连接WIFI，但是WIFI不是SmartaBox，断开连接重连
        if(connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                &&
                !TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())
                ){
            //断开连接再重连
            Log.d(TAG, "onClick: wifi  reConnecting...");
            wifiUtil.removeNowConnectingID();
            handler.removeMessages(MSG_CONNECT);
            handler.sendMessageDelayed(handler.obtainMessage(MSG_CONNECT),300);
        }else if(connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED){
            //如果没连接，重连
            Log.d(TAG, "onClick: wifi connecting...");
//                    wifiUtil.removeNowConnectingID();
            handler.removeMessages(MSG_CONNECT);
            handler.sendMessageDelayed(handler.obtainMessage(MSG_CONNECT),300);
        }
//        handler.sendMessage(handler.obtainMessage(MSG_CONNECT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        socketUtil.setDone();
    }

    @Override
    public void onSuccess() {


        STEP = 1;
        EmailSTEP=1;
        int progress=(100/AllCount)*1;
        setTranProgress(progress);

        sendEmail1();
    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onResult(byte[] b) {

        switch (STEP) {
            case 1:
                    if(EmailSTEP==1){
                        checkHelper(b,0x01);
                    }else{
                        checkHelper(b,EmailSTEP+7);
                    }
                break;
            case 2:
                if (checkReply2(b)) {
                    STEP = 3;
                    sendThree();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.wifi_name_not, Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (checkReply3(b)) {
                    STEP = 4;
                    sendFour();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.wifi_not_psw, Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (checkReply4(b)) {
                    STEP = 5;
                    sendFive();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.distance_not, Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                if (checkReply5(b)) {
                    sendSix();
                    STEP = 6;
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email_title, Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                if (checkReply6(b)) {
                  //  Toast.makeText(WifiConnectActivity.this, "全部通过", Toast.LENGTH_SHORT).show();
                    pg.setDelivery(true);
                    pg.updateAll("time = ?", pg.getTime() + "");
//                    p.dismiss();
//                    p2.dismiss();
                    mMainHanler.removeMessages(MSG_DELAYSHOW);
                    Intent intent = new Intent(WifiConnectActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email_content, Toast.LENGTH_SHORT).show();
                }
                STEP = 1;
                break;
        }
    }

    public String byteToString(byte[] b) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex = hex.toUpperCase();
            s.append(hex + " ");
        }
        return s.toString();
    }

    boolean checkReply_Email(byte[] b, int cmd) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, cmd);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    void checkHelper(byte[] b, int cmd) {
        if (checkReply_Email(b, cmd)) {
            if (EmailSTEP == EmailCount) {
                STEP = 2;
                sendSecond();
            } else {
                EmailSTEP++;
                switch (EmailSTEP) {
                    case 1:
                        sendEmail1();
                        break;
                    case 2:
                        sendEmail2();
                        break;
                    case 3:
                        sendEmail3();
                        break;
                    case 4:
                        sendEmail4();
                        break;
                    case 5:
                        sendEmail5();
                        break;
                    case 6:
                        sendEmail6();
                        break;
                    case 7:
                        sendEmail7();
                        break;
                    case 8:
                        sendEmail8();
                        break;
                    case 9:
                        sendEmail9();
                        break;
                    case 10:
                        sendEmail10();
                        break;
                }
            }
        } else {
            Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email, Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkReply2(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x02);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply3(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x03);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply4(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x04);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }


    boolean checkReply5(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x05);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply6(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x06);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }


    boolean checkReply(byte[] b) {
        int length = 23;
        if (b.length < 23) {
            return false;
        }
        byte[] trueByte = new byte[length];
        trueByte[0] = (byte) 0xFF;
        trueByte[1] = (byte) 0xAA;
        trueByte[2] = (byte) (length - 3);
        trueByte[3] = (byte) 0x08;
        String s = getIntent().getStringExtra("qr_code") + "000000";
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            trueByte[4 + i] = (byte) c[i];
        }
        trueByte[s.length() + 4] = (byte) 0x4F;
        trueByte[s.length() + 5] = (byte) 0x4B;
        byte sum = 0;
        for (int i = 0; i < 20; i++) {
            sum += trueByte[i];
        }
        trueByte[s.length() + 6] = (byte) sum;
        trueByte[s.length() + 7] = (byte) 0xFF;
        trueByte[s.length() + 8] = (byte) 0x55;
        for (int i = 0; i < 23; i++) {
            if (b[i] != trueByte[i]) {
                Log.d(TAG, "checkReply: b" + b[i] + " t " + trueByte[i]);
                return false;
            }
        }
        return true;
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {

                case PublicDefine.WIFI_CONNECTTING_ACTION:
                    setWifiConnectting();
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
                        Log.i(TAG, "已经打开");

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
                            break;
                        case DISCONNECTED:
                            if (wifiUtil.isOpen()) {

                                Log.i(TAG, "onReceive: 已经打开,未连接");
                            }
                            break;
                        case CONNECTING:

                            Log.i(TAG, "onReceive: 正在连接");
                            break;
                        case CONNECTED:

                            Log.i(TAG, "onReceive: " + wifiUtil.getSSID());
                            Log.i(TAG, "onReceive: 已连接");
                            if (TextUtils.equals("\"SmartaBox\"", wifiUtil.getSSID())) {

                                btn_retry.setEnabled(false);
                                btn_retry.setAlpha(0.4f);
                                p2.dismiss();
                                p.show();
                                tv_status.setText(R.string.wifi_connecting);
                                tv_reason.setText(R.string.please_colse_packae);
                                mMainHanler.removeMessages(MSG_DELAYSHOW);
                                mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW), 8000);
                                socketUtil.connect();

                                Log.d(TAG, "onReceive: connetcd:++");
                            } else {
                                mMainHanler.removeMessages(MSG_DELAYSHOW);
                                mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW), 1000);
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

    public static void startTrans(Context context, Package pg) {
        Intent intent = new Intent(context, WifiConnectActivity.class);
        intent.putExtra("pg_data", pg);
        context.startActivity(intent);
    }

    public Package getPackage() {
        Package pg = (Package) getIntent().getSerializableExtra("pg_data");
        return pg;
    }

    void setWifiConnectting(){
        mMainHanler.removeMessages(MSG_DELAYSHOW);
        p.dismiss();
        p2.dismiss();
        btn_retry.setEnabled(false);
        btn_retry.setAlpha(0.4f);
        tv_status.setText(R.string.wifi_connecting);
        tv_reason.setText(R.string.please_colse_packae);
        mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW), 12000);
    }

   void setTranProgress(int progress){
       btn_retry.setEnabled(false);
       btn_retry.setAlpha(0.4f);
       p.dismiss();
       p2.setProgress(progress);
       p2.show();
       mMainHanler.removeMessages(MSG_DELAYSHOW);
       mMainHanler.sendMessageDelayed(mMainHanler.obtainMessage(MSG_DELAYSHOW), 8000);
   }
}
