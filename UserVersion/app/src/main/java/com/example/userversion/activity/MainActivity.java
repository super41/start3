package com.example.userversion.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.userversion.PublicDefine;
import com.example.userversion.R;

import com.example.userversion.Util.PermissionUtils;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.bean.Package;
import com.example.userversion.info.PackageManagerActivity;
import com.example.userversion.info.SetNameActivity;
import com.example.userversion.info.SettingActivity;
import com.example.userversion.view.TopBar;
import com.example.userversion.zxing.android.CaptureActivity;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TopBar mTopbar;
    Button mBtnScan;
    Button mBtnRegister;
    String TAG=getClass().getSimpleName();


    //扫描二维码
    private static final int SCANNING_CODE = 1;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    public void initView() {
        mTopbar = (TopBar) findViewById(R.id.topBar);
        mBtnScan= (Button) findViewById(R.id.btn_scan);
        mBtnRegister= (Button) findViewById(R.id.btn_register);



        mTopbar.setCall(new TopBar.Call() {
           @Override
           public void onRightClick() {
              Intent intent=new Intent(MainActivity.this,SettingActivity.class);
               startActivity(intent);


           }
       });


        //***
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PackageManagerActivity.class);
                startActivity(intent);
            }
        });

        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             PermissionUtils.requestPermission(MainActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);

            }
        });


    }

    public void checkIsHaveData(){
        SharedPreferences sharedPreference=getSharedPreferences(PublicDefine.SP_INFO,MODE_PRIVATE);
        String name=sharedPreference.getString("name","");
        String company=sharedPreference.getString("company","");
        String email=sharedPreference.getString("email","");
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(company) || TextUtils.isEmpty(email)){
            mBtnScan.setEnabled(false);
            mBtnScan.setAlpha(0.6f);
        }else{
            mBtnRegister.setVisibility(View.GONE);
            mBtnScan.setAlpha(1.0f);
            mBtnScan.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        List<Package> mList= DataSupport.findAll(Package.class);

        if(mList == null || mList.size()<1){
            mTopbar.setRightShow(false);
        }else{
            mTopbar.setRightShow(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode){
                case PermissionUtils.CODE_CAMERA:
                    beginScan();
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    public void beginScan() {
        Intent intent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, SCANNING_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == SCANNING_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                if(content==null || content.length()!=14 || ! isNumeric(content)){
                    Toast.makeText(MainActivity.this,"二维码格式不对",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "onActivityResult: "+content);
                Intent intent=new Intent(MainActivity.this,SetNameActivity.class);
                SpUtil.sumbitQRCode(MainActivity.this,content);
                startActivity(intent);
            }
        }
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
