package com.example.userversion.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.userversion.PackageAdapter;
import com.example.userversion.R;
import com.example.userversion.Util.SpUtil;
import com.example.userversion.activity.MainActivity;
import com.example.userversion.bean.Package;
import com.example.userversion.view.GridDivider;
import com.example.userversion.view.TopBar;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XJP on 2017/12/3.
 */
public class PackageManagerActivity extends AppCompatActivity {
    final  String TAG=getClass().getSimpleName();
    RecyclerView mRecyclerView;
    PackageAdapter mPackageAdapter;
    List<Package> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_package);
        initView();

    }
    public void initView(){

        mList= DataSupport.order("time desc").find(Package.class);
        Log.d(TAG, "initView: "+mList.size());
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mPackageAdapter=new PackageAdapter(this,mList);
        TopBar topBar= (TopBar) findViewById(R.id.topBar);
        topBar.setCall(new TopBar.Call() {

            @Override
            public void onLeftClick(Context context) {
                goToHomePage();
            }

            @Override
            public void onRightClick() {
                boolean isGrid=SpUtil.getIsGridView(PackageManagerActivity.this);
                Log.d(TAG, "onRightClick: "+isGrid);
               if(!isGrid){
                   LinearLayoutManager mGridManager = new GridLayoutManager(PackageManagerActivity.this, 2);
                   mPackageAdapter.setMode(PackageAdapter.MODE_GRID);
                   mRecyclerView.setLayoutManager(mGridManager);
                   SpUtil.sumbitGridView(PackageManagerActivity.this,true);
               } else{
                   LinearLayoutManager mLineManager=new LinearLayoutManager(PackageManagerActivity.this);
                   mPackageAdapter.setMode(PackageAdapter.MODE_LINE);
                   mRecyclerView.setLayoutManager(mLineManager);
                   SpUtil.sumbitGridView(PackageManagerActivity.this,false);
               }
                mRecyclerView.setAdapter(mPackageAdapter);
            }
        });

        boolean isGrid=SpUtil.getIsGridView(this);
        if(isGrid){
            LinearLayoutManager mManagerColor = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mManagerColor);
            mRecyclerView.setAdapter(mPackageAdapter);
        }else{
            LinearLayoutManager mLineManager=new LinearLayoutManager(PackageManagerActivity.this);
            mPackageAdapter.setMode(PackageAdapter.MODE_LINE);
            mRecyclerView.setLayoutManager(mLineManager);
            mRecyclerView.setAdapter(mPackageAdapter);
        }


    }

    @Override
    public void onBackPressed() {
        //TODO something
         goToHomePage();

    }

    public void goToHomePage(){
        Log.d(TAG, "onBackPressed: ");
        Intent intent=new Intent(PackageManagerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPackageAdapter.setData(DataSupport.order("time desc").find(Package.class));
        mPackageAdapter.notifyDataSetChanged();
    }
}
