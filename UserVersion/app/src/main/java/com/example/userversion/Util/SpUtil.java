package com.example.userversion.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.userversion.PublicDefine;

/**
 * Created by XJP on 2017/12/3.
 */
public class SpUtil {
   public static void sumbitName(Context context,String name){
       SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
       SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
       editor.putString("name", name);
       editor.commit();//提交修改
   }

    public static void sumbitEmail(Context context,String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("email", email);
        editor.commit();//提交修改
    }

    public static void sumbitFastMode(Context context,boolean isFast){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("fastMode", isFast);
        editor.commit();//提交修改
    }

    public static boolean getIsFastMode(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        boolean isFast=sharedPreference.getBoolean("fastMode",false);
        return isFast;
    }

    public static void sumbitWifi(Context context,String wifiId,String wifiPsw){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("wifiId", wifiId);
        editor.putString("wifiPsw", wifiPsw);
        editor.commit();//提交修改
    }

    public static void sumbitQRCode(Context context,String qrCode){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("qrCode", qrCode);
        editor.commit();//提交修改
    }

    public static void sumbitGridView(Context context,boolean isGrid){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PublicDefine.SP_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("gridView", isGrid);
        editor.commit();//提交修改
    }

    public static boolean getIsGridView(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        boolean isGrid=sharedPreference.getBoolean("gridView",true);
        return isGrid;
    }


    public static String getQRCode(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        String code=sharedPreference.getString("qrCode","");
        return code;
    }

    public static String getName(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        String name=sharedPreference.getString("name","");
        return name;
    }
    public static String getEmail(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        String email=sharedPreference.getString("email","");
        return email;
    }

    public static String getWifiId(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        String id=sharedPreference.getString("wifiId","");
        return id;
    }

    public static String getWifiPsw(Context context){
        SharedPreferences sharedPreference=context.getSharedPreferences(PublicDefine.SP_INFO,Context.MODE_PRIVATE);
        String psw=sharedPreference.getString("wifiPsw","");
        return psw;
    }


}
