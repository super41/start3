package com.example.userversion.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.PackageAdapter;
import com.example.userversion.R;
import com.example.userversion.Util.Util;
import com.example.userversion.activity.WifiConnectActivity;
import com.example.userversion.bean.Package;
import com.example.userversion.view.MyAutoCompleteTextView;
import com.example.userversion.view.TopBar;

import org.litepal.crud.DataSupport;

import java.util.List;

import static com.example.userversion.R.id.ll;

/**
 * Created by XJP on 2017/12/3.
 */
public class PackageDetailActivity extends AppCompatActivity {
   final String TAG=getClass().getSimpleName();
    LinearLayout mLinearLayout;
    Package pg=null;
    EditText mPgName;
    EditText mWifiId;
    EditText mWifiPsw;
    EditText mDistance;
    TextView mEmail;
    EditText mEmailTitle;
    EditText mEmailContent;
    Button mBtnModify;
    Button mBtnTransfrom;
    TextView nameInfo;
    TextView nameInfo2;
    TextView nameInfo3;
    TextView nameInfo4;
    TextView nameInfo5;
    int REQ_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_package);
        initView();

    }
    public void initView(){
        mLinearLayout= (LinearLayout) findViewById(R.id.ll);
        setAllClick(mLinearLayout,onClickListener);
        pg=getPackage();
        Log.d(TAG, "initView: "+pg.getQrCode());



        nameInfo= (TextView) findViewById(R.id.name_info);
        nameInfo2= (TextView) findViewById(R.id.name_info2);
        nameInfo3= (TextView) findViewById(R.id.name_info3);
        nameInfo4= (TextView) findViewById(R.id.name_info4);
        nameInfo5= (TextView) findViewById(R.id. name_info5);

        mPgName= (EditText) findViewById(R.id.et_pg_name);
        mWifiId= (EditText) findViewById(R.id.et_wifi_name);
        mWifiPsw= (EditText) findViewById(R.id.et_wifi_psw);
        mDistance= (EditText) findViewById(R.id.et_wifi_distance);
        mEmail= (TextView) findViewById(R.id.et_email);
        mEmailTitle= (EditText) findViewById(R.id.et_email_title);
        mEmailContent= (EditText) findViewById(R.id.et_email_content);
        mBtnModify= (Button) findViewById(R.id.btn_modify);
        mBtnTransfrom= (Button) findViewById(R.id.btn_transform);

        mEmailTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>20){
                    mEmailTitle.setTextColor(getResources().getColor(R.color.colorRed));
                }else{
                    mEmailTitle.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        mEmailContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>30){
                    mEmailContent.setTextColor(getResources().getColor(R.color.colorRed));
                }else{
                    mEmailContent.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        //
//        Util.controlKeyboardLayout(mLinearLayout,mEmailTitle);

        mPgName.setText(pg.getName());
        mWifiId.setText(pg.getWifiId());
        mWifiPsw.setText(pg.getWifiPsw());
        String s=pg.getEmail1()+Util.getPgEmailSize(pg)+getString(R.string.one);
        SpannableString spannableString = new SpannableString(s);
        //spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0,pg.getEmail1().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green2)), pg.getEmail1().length(),s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30), pg.getEmail1().length(),s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mDistance.setText(pg.getDistance());
        mEmail.setText(spannableString);
        mEmailTitle.setText(pg.getEmailTitle());
        mEmailContent.setText(pg.getEmailContent());
        mPgName.requestFocus();



    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.delete_1:
                    mPgName.setText("");
                    break;
                case R.id.delete_2:
                    mWifiId.setText("");
                    break;
                case R.id.delete_3:
                    mWifiPsw.setText("");
                    break;
                case R.id.delete_4:
                    mDistance.setText("");
                    break;
                case R.id.delete_5:
                    Intent intent=new Intent(PackageDetailActivity.this,EmailConfigActivity.class);
                    intent.putExtra("pg_data",pg);
                    startActivityForResult(intent,REQ_CODE);
                    break;
                case R.id.delete_6:
                    mEmailTitle.setText("");
                    break;
                case R.id.delete_7:
                    mEmailContent.setText("");
                    break;
                case R.id.btn_modify:
                    onModify(true);
                    break;
                case R.id.btn_transform:
                    onTransform();
                    break;
                case R.id.btn_left:
                    finish();
                    break;

            }
        }
    };

    public void onTransform(){
        if(onModify(false)){
            WifiConnectActivity.startTrans(this,pg);
            finish();
        }

    }

    public boolean onModify(boolean need){
        String pgName=mPgName.getText().toString();
        String wifiId=mWifiId.getText().toString();
        String wifiDistance=mDistance.getText().toString();
        String wifiPsw=mWifiPsw.getText().toString();
        String email=mEmail.getText().toString();
        String emailTitle=mEmailTitle.getText().toString();
        String emailContent=mEmailContent.getText().toString();
        if(isEmpty(pgName,wifiId,wifiPsw,wifiDistance,emailTitle,emailContent)){
            Util.showAlpha(nameInfo);
            return false;
        }
        if(emailTitle.length()>20){
            Util.showAlpha(nameInfo4);
            return false;
        }
        if(emailContent.length()>30){
            Util.showAlpha(nameInfo5);
            return false;
        }

        pg.setName(pgName);
        pg.setWifiId(wifiId);
        pg.setDistance(wifiDistance);
        pg.setWifiPsw(wifiPsw);
        //pg.setEmail(email);
        pg.setEmailTitle(emailTitle);
        pg.setEmailContent(emailContent);
        pg.setToDefault("isDelivery");
        pg.updateAll("time = ?",pg.getTime()+"");
        if(need)
        Util.showAlpha(nameInfo3);
        return true;
    }



    public boolean isEmpty(String... sArray){
        for(String s: sArray){
            if(TextUtils.isEmpty(s)){
                return true;
            }
        }
     return false;
    }




    public void setAllClick(ViewGroup root, View.OnClickListener onClickListener){
        for(int i=0;i<root.getChildCount();i++){
            View child=root.getChildAt(i);
            if( child instanceof ViewGroup){
                setAllClick((ViewGroup) child , onClickListener);
            }else{
                child.setOnClickListener(onClickListener);
            }
        }
    }

    public static void startDetail(Context context, Package pg){
        Intent intent=new Intent(context,PackageDetailActivity.class);
        intent.putExtra("pg_data",pg);
        context.startActivity(intent);
    }

    public Package getPackage(){
        Package pg= (Package) getIntent().getSerializableExtra("pg_data");
        return pg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Package pgTemp= (Package) data.getSerializableExtra("data");
                Util.pgTopg_email(pg,pgTemp);
                String s=pg.getEmail1()+Util.getPgEmailSize(pg)+getString(R.string.one);
                SpannableString spannableString = new SpannableString(s);
                //spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0,pg.getEmail1().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green2)), pg.getEmail1().length(),s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new AbsoluteSizeSpan(30), pg.getEmail1().length(),s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mEmail.setText(spannableString);
            }
        }
    }


}
