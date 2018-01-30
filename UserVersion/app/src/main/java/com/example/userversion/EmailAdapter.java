package com.example.userversion;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.activity.WifiConnectActivity;
import com.example.userversion.bean.Package;
import com.example.userversion.info.PackageDetailActivity;

import java.util.List;

/**
 * Created by XJP on 2017/12/16.
 */
public class EmailAdapter extends RecyclerView.Adapter {
    List<String> mList;
    Context mContext;
    long lastTime;

    public EmailAdapter(Context context, List<String> list) {
        mList = list;
        mContext = context;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package3, parent, false);
        return new MyViewHolder(view);

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageButton btn_del;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_del = (ImageButton) itemView.findViewById(R.id.btn_del);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder h = (MyViewHolder) holder;
        h.tv_name.setText(mList.get(position));

        h.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nowTime = System.currentTimeMillis();
                if ((nowTime - lastTime) < 300) {
                    lastTime = nowTime;
                    //Toast.makeText(mContext, "too qucickly", Toast.LENGTH_SHORT).show();
                    return;
                }
                lastTime = nowTime;
                mList.remove(position);
                notifyItemRemoved(position);
                if (position != mList.size()) { // 如果移除的是最后一个，忽略
                    notifyItemRangeChanged(position, mList.size() - position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
