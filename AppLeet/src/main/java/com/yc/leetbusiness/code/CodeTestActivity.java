package com.yc.leetbusiness.code;

import android.view.View;
import android.widget.TextView;

import com.yc.leetbusiness.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 算法导论
 *     revise:
 * </pre>
 */
public class CodeTestActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;

    @Override
    public int getContentView() {
        return R.layout.activity_leet_code;
    }

    @Override
    public void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        tv8 = findViewById(R.id.tv_8);
        tv9 = findViewById(R.id.tv_9);
        tv10 = findViewById(R.id.tv_10);
        tv11 = findViewById(R.id.tv_11);
        tv12 = findViewById(R.id.tv_12);
    }

    @Override
    public void initListener() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tv1) {
            cla(100);
        } else if (v == tv2) {

        } else if (v == tv3) {

        } else if (v == tv4) {

        } else if (v == tv5){

        }
    }


    private int cla(int n){
        int sum = 0;
        for (int i=1 ;i<=n; i++){
            sum = sum + 1;
        }
        return sum;
    }

}
