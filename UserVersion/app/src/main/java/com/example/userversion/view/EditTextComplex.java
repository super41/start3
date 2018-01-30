package com.example.userversion.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.userversion.R;

/**
 * Created by XJP on 2018/1/16.
 */
public class EditTextComplex extends RelativeLayout{

    EditText et;
    TextView tv;

    public EditTextComplex(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_complex, this);
        et= (EditText) ((ViewGroup)this.getChildAt(0)).getChildAt(0);

        tv= (TextView) findViewById(R.id.tv);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv.setText(s.length()+"/"+10);
            }
        });
    }
}
