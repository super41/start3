package com.example.userversion.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.userversion.R;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.Util.Util;
import com.example.userversion.bean.Package;
import com.example.userversion.view.MyAutoCompleteTextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XJP on 2017/12/2.
 */
public class SetWifiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wifi);
        initView();
    }
    public void initView(){


        final MyAutoCompleteTextView et_name= (MyAutoCompleteTextView) findViewById(R.id.et_name);
        final EditText et_psw= (EditText) findViewById(R.id.et_psw);
        final TextView nameInfo= (TextView) findViewById(R.id.name_info);
        Button btn_next= (Button) findViewById(R.id.btn_next);

        List<Package> mList= DataSupport.order("time desc").find(Package.class);
        if(mList != null && mList.size() > 0){
            et_name.setText(mList.get(0).getWifiId());
            et_psw.setText(mList.get(0).getWifiPsw());
            et_name.requestFocus();
            List<String> list=Util.getWifiList(mList);
            if(list!=null){
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
                et_name.setAdapter(arrayAdapter);
            }
        }



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=et_name.getText().toString();
                String psw=et_psw.getText().toString();

                if(TextUtils.isEmpty(id) || TextUtils.isEmpty(psw)){
                    Util.showAlpha(nameInfo);
                    return ;
                }

                SpUtil.sumbitWifi(SetWifiActivity.this,id,psw);
                String name=SpUtil.getName(SetWifiActivity.this);
                String email=SpUtil.getEmail(SetWifiActivity.this);
                String qrCode=SpUtil.getQRCode(SetWifiActivity.this);

                Package pg=new Package();
                pg.setName(name);
                pg.setEmail1(email);
                pg.setEmail2("");
                pg.setEmail3("");
                pg.setEmail4("");
                pg.setEmail5("");
                pg.setEmail6("");
                pg.setEmail7("");
                pg.setEmail8("");
                pg.setEmail9("");
                pg.setEmail10("");
                pg.setQrCode(qrCode);
                pg.setDistance("2");
                pg.setWifiId(id);
                pg.setWifiPsw(psw);
                pg.setEmailTitle(name);
                pg.setEmailContent("You Have Received A Delivery");
                pg.setTime(System.currentTimeMillis());
                pg.setDelivery(false);
                pg.save();

                Intent intent=new Intent(SetWifiActivity.this,PackageManagerActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ibtn_del= (ImageButton) findViewById(R.id.delete_1);
        ibtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setText("");
            }
        });

        ImageButton ibtn_del2= (ImageButton) findViewById(R.id.delete_2);
        ibtn_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_psw.setText("");
            }
        });


    }

}
