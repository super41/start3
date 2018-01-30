package com.example.userversion.info;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.EmailAdapter;
import com.example.userversion.PackageAdapter;
import com.example.userversion.R;
import com.example.userversion.Util.Util;
import com.example.userversion.bean.Package;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XJP on 2017/12/16.
 */
public class EmailConfigActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    EmailAdapter mEmailAdapter;
    List<String> mList;
    Package pg;
    Button mBtnMore;
    Button mBtnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_config);
        initView();
    }

    public void initView(){
        pg=getPackage();
        mList= Util.getPgEmailList(pg);

        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mEmailAdapter=new EmailAdapter(this,mList);
        LinearLayoutManager mLineManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLineManager);
        mRecyclerView.setAdapter(mEmailAdapter);
        mBtnMore= (Button) findViewById(R.id.btn_more);
        mBtnSave= (Button) findViewById(R.id.btn_rights);

        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showMessage();
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    public Package getPackage(){
        Package pg= (Package) getIntent().getSerializableExtra("pg_data");
        return pg;
    }

    public void showMessage(){
        final EditText et = new EditText(this);
        final TextView nameInfo = (TextView) findViewById(R.id.name_info);
        final TextView nameInfo2 = (TextView) findViewById(R.id.name_info2);
        final TextView nameInfo3 = (TextView) findViewById(R.id.name_info3);

        new AlertDialog.Builder(this).setTitle(R.string.input_email)
                .setView(et)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if(!isEmail(input)){
                            Util.showAlpha(nameInfo);
                            return;
                        }
                        if(mList.contains(input)){
                            Util.showAlpha(nameInfo2);
                            return;
                        }
                        if(mList.size()>=10){
                            Util.showAlpha(nameInfo3);
                            return;
                        }

                        mList.add(input);
                        mEmailAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public void save(){
        final TextView nameInfo4 = (TextView) findViewById(R.id.name_info4);
        if(mList.size()==0){
            Util.showAlpha(nameInfo4);
            return;
        }
        if(mList.size()<10){
            for(int i=mList.size();i<10;i++){
                mList.add("");
            }
        }
        Log.d("xjp", "save: "+mList.size());


        pg.setEmail1(mList.get(0));
        pg.setEmail2(mList.get(1));
        pg.setEmail3(mList.get(2));
        pg.setEmail4(mList.get(3));
        pg.setEmail5(mList.get(4));
        pg.setEmail6(mList.get(5));
        pg.setEmail7(mList.get(6));
        pg.setEmail8(mList.get(7));
        pg.setEmail9(mList.get(8));
        pg.setEmail10(mList.get(9));
        pg.setToDefault("isDelivery");
        pg.updateAll("time = ?",pg.getTime()+"");
        Intent intent=new Intent();
        intent.putExtra("data",pg);
        setResult(RESULT_OK,intent);
        finish();
        Toast.makeText(EmailConfigActivity.this,"Modified Successfully",Toast.LENGTH_SHORT).show();
    }
}