package com.example.userversion.info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.userversion.PublicDefine;
import com.example.userversion.R;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.Util.Util;
import com.example.userversion.bean.Package;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XJP on 2017/12/2.
 */
public class SetNameActivity extends AppCompatActivity {
    final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        initView();

    }
    public void initView(){
        final EditText et_name= (EditText) findViewById(R.id.et_name);
        final TextView nameInfo= (TextView) findViewById(R.id.name_info);
        Button btn_next= (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et_name.getText().toString();
                if(TextUtils.isEmpty(s)){
                    Util.showAlpha(nameInfo);
                    return ;
                }
                Package mPg=DataSupport.findLast(Package.class);
                if(!SpUtil.getIsFastMode(SetNameActivity.this) || mPg==null ) {
                    SpUtil.sumbitName(SetNameActivity.this, s);
                    Intent intent = new Intent(SetNameActivity.this, SetEmailActivity.class);
                    startActivity(intent);
                }else{

                    String name=s;

                    String wifiId=mPg.getWifiId();
                    String wifiPsw=mPg.getWifiPsw();
                    String qrCode=SpUtil.getQRCode(SetNameActivity.this);

                    Package pg=new Package();
                    pg.setName(name);
                    Util.pgTopg_email(pg,mPg);
                    pg.setQrCode(qrCode);
                    pg.setDistance("2");
                    pg.setWifiId(wifiId);
                    pg.setWifiPsw(wifiPsw);
                    pg.setEmailTitle("BRIZE BOX");
                    pg.setEmailContent("Have a nice day");
                    pg.setTime(System.currentTimeMillis());
                    pg.setDelivery(false);
                    pg.save();

                    Intent intent=new Intent(SetNameActivity.this,PackageManagerActivity.class);
                    startActivity(intent);
                }
            }
        });



        ImageButton ibtn_del= (ImageButton) findViewById(R.id.delete_1);
        ibtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setText("");
            }
        });

        List<Package> mList= DataSupport.findAll(Package.class);
        et_name.setText("My Home "+(mList.size()+1));
        et_name.requestFocus();
    }



}
