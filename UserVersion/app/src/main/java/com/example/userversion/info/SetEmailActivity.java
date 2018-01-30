package com.example.userversion.info;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.R;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.Util.Util;
import com.example.userversion.bean.Package;
import com.example.userversion.view.MyAutoCompleteTextView;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by XJP on 2017/12/2.
 */
public class SetEmailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_email);
        initView();
    }

    public void initView() {
        final EditText et_name = (EditText) findViewById(R.id.et_name);
        final TextView nameInfo = (TextView) findViewById(R.id.name_info);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_name.getText().toString();
                boolean flag = isEmail(s);
                if (!flag) {
                    Util.showAlpha(nameInfo);
                    return;
                }
                SpUtil.sumbitEmail(SetEmailActivity.this, s);
                Intent intent = new Intent(SetEmailActivity.this, SetWifiActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ibtn_del = (ImageButton) findViewById(R.id.delete_1);
        ibtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setText("");
            }
        });

        List<Package> mList= DataSupport.order("time desc").find(Package.class);
        if(mList != null && mList.size() > 0){
            et_name.setText(mList.get(0).getEmail1());
        }
       // List<Package> mList = DataSupport.where("email != ?", "").order("time desc").find(Package.class);

       /* String[] array = Util.getPackages(mList, Util.EMAIL);
        if (array != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            et_name.setAdapter(arrayAdapter);
            et_name.setText(array[0]);
        }*/

    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }
}
