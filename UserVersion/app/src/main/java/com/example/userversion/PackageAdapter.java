package com.example.userversion;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.userversion.activity.UnLockActivity;
import com.example.userversion.activity.WifiConnectActivity;
import com.example.userversion.bean.Package;
import com.example.userversion.info.PackageDetailActivity;

import java.util.List;

/**
 * Created by XJP on 2017/12/3.
 */
public class PackageAdapter extends RecyclerView.Adapter{

    List<Package> mList;
    Context mContext;

    int colorGreen;
    int colorGray;

    public static final int MODE_GRID=0;
    public static final int MODE_LINE=1;


    int mMode=0;
    public PackageAdapter(Context context,List<Package> list){
        mContext=context;
        mList=list;
        colorGreen=context.getResources().getColor(R.color.card_green);
        colorGray =context.getResources().getColor(R.color.card_gray);
    }

    public void setData(List<Package> list){
        mList=list;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_code;
        ImageButton btn_connect;
        ImageButton btn_del;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            btn_connect= (ImageButton) itemView.findViewById(R.id.btn_connect);
            btn_del= (ImageButton) itemView.findViewById(R.id.btn_del);
            card= (CardView) itemView.findViewById(R.id.card);
            tv_code= (TextView) itemView.findViewById(R.id.tv_code);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mMode == MODE_GRID){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package,parent,false);
        return new MyViewHolder(view);
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package2,parent,false);
            return new MyViewHolder(view);
        }
    }

    public void setMode(int mode){
        this.mMode=mode;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final Package pg=mList.get(position);
            MyViewHolder h= (MyViewHolder) holder;
            h.tv_name.setText(pg.getName());
            h.tv_code.setText(pg.getQrCode().substring(0,8));
            h.card.setCardBackgroundColor( pg.isDelivery() ? colorGreen : colorGray);


            h.btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnLockActivity.startTrans(mContext,pg);
                }
            });
            h.btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessage(mContext, pg.getName(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pg.delete();
                            mList.remove(position);
                            notifyItemRemoved(position);
                            if(position != mList.size()){ // 如果移除的是最后一个，忽略
                                notifyItemRangeChanged(position, mList.size() - position);
                            }
                        }
                    });
                }
            });

            h.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent=new Intent(mContext, PackageDetailActivity.class);
                    intent.putExtra("data",pg);
                    mContext.startActivity(intent);*/
                    PackageDetailActivity.startDetail(mContext,pg);
                }
            });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static void showMessage(final Context context, String message, DialogInterface.OnClickListener okListener) {
     //   String title = context.getResources().getString(R.string.permission_apply_title);
     //   String cancel=context.getResources().getString(R.string.permission_cancel);
     //   String setting=context.getResources().getString(R.string.permission_setting);
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete)
                .setMessage(message)
                .setPositiveButton(R.string.sure, okListener)
                .setNegativeButton(R.string.no, null)
                .create()
                .show();
    }
}
