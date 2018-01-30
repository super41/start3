package com.example.userversion.Util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.example.userversion.bean.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XJP on 2017/12/3.
 */
public class Util {

    public static final String EMAIL = "email";
    public static final String WIFIID = "wifiId";
    public static final String WIFIPSW = "wifiPsw";


    public static void showAlpha(final View view) {
        view.setVisibility(View.VISIBLE);
        // S alpha = ObjectAnimator.ofFloat(view, "alpha", 1f,0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        alpha.setDuration(1000);//设置动画时间
        alpha.setRepeatCount(0);
        alpha.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        alpha.start();//启动动画。

        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.INVISIBLE);
                    }
                }, 800);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static List<String> getWifiList(List<Package> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<String> temp = new ArrayList<String>();
        for (Package p : list) {
            if (!temp.contains(p.getWifiId()))
                temp.add(p.getWifiId());
        }
        return temp;
    }
   /* public static String[] getPackages(List<Package> list,String input){
        if(list == null || list.size()==0){
            return null;
        }
        List<String> tempList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String s="empty";
            switch (input){
                case EMAIL:
                    s=list.get(i).getEmail();
                    break;
                case WIFIID:
                    s=list.get(i).getWifiId();
                    break;
                case WIFIPSW:
                    s=list.get(i).getWifiPsw();
                    break;
                default:
                    return null;
            }
            if(!tempList.contains(s)){
                tempList.add(s);
            }
        }
        String[] array=new String[tempList.size()];
        for(int i=0;i<tempList.size();i++){
            array[i]=tempList.get(i);
        }
        return array;
    }*/

    public static void pgTopg_email(Package pg, Package mPg) {
        pg.setEmail1(mPg.getEmail1());
        pg.setEmail2(mPg.getEmail2());
        pg.setEmail3(mPg.getEmail3());
        pg.setEmail4(mPg.getEmail4());
        pg.setEmail5(mPg.getEmail5());
        pg.setEmail6(mPg.getEmail6());
        pg.setEmail7(mPg.getEmail7());
        pg.setEmail8(mPg.getEmail8());
        pg.setEmail9(mPg.getEmail9());
        pg.setEmail10(mPg.getEmail10());
    }

    public static int getPgEmailSize(Package pg) {
        int size = 0;
        if (!TextUtils.isEmpty(pg.getEmail1()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail2()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail3()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail4()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail5()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail6()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail7()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail8()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail9()))
            size += 1;
        if (!TextUtils.isEmpty(pg.getEmail10()))
            size += 1;
        return size;
    }

    public static List<String> getPgEmailList(Package pg) {
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(pg.getEmail1()))
            list.add(pg.getEmail1());
        if (!TextUtils.isEmpty(pg.getEmail2()))
            list.add(pg.getEmail2());
        if (!TextUtils.isEmpty(pg.getEmail3()))
            list.add(pg.getEmail3());
        if (!TextUtils.isEmpty(pg.getEmail4()))
            list.add(pg.getEmail4());
        if (!TextUtils.isEmpty(pg.getEmail5()))
            list.add(pg.getEmail5());
        if (!TextUtils.isEmpty(pg.getEmail6()))
            list.add(pg.getEmail6());
        if (!TextUtils.isEmpty(pg.getEmail7()))
            list.add(pg.getEmail7());
        if (!TextUtils.isEmpty(pg.getEmail8()))
            list.add(pg.getEmail8());
        if (!TextUtils.isEmpty(pg.getEmail9()))
            list.add(pg.getEmail9());
        if (!TextUtils.isEmpty(pg.getEmail10()))
            list.add(pg.getEmail10());
        return list;
    }

    /**
     * 解决在页面底部置输入框，输入法弹出遮挡部分输入框的问题
     *
     * @param root       页面根元素
     * @param editLayout 被软键盘遮挡的输入框
     */
    public static void controlKeyboardLayout(final View root,
                                             final View editLayout) {
        // TODO Auto-generated method stub
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInVisibleHeigh = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInVisibleHeigh > 100) {
                    Log.v("hjb", "不可视区域高度大于100，则键盘显示");
                    int[] location = new int[2];
                    //获取editLayout在窗体的坐标
                    editLayout.getLocationInWindow(location);
                    //计算root滚动高度，使editLayout在可见区域
                    int srollHeight = (location[1] + editLayout.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    Log.v("hjb", "不可视区域高度小于100，则键盘隐藏");
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}
