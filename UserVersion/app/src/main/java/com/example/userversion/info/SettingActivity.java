package com.example.userversion.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.userversion.R;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.Util.Util;

/**
 * Created by XJP on 2017/12/8.
 */
public class SettingActivity extends AppCompatActivity{

    Switch mSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();

    }

    public void initView(){
        mSwitch= (Switch) findViewById(R.id.sw);
        final TextView nameInfo= (TextView) findViewById(R.id.name_info);

        if(SpUtil.getIsFastMode(this)){
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Util.showAlpha(nameInfo);
                if(isChecked){
                    SpUtil.sumbitFastMode(SettingActivity.this,true);
                }else{
                    SpUtil.sumbitFastMode(SettingActivity.this,false);
                }
            }
        });


    }

}
