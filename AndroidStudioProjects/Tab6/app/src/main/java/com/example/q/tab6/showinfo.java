package com.example.q.tab6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class showinfo extends Activity{
    Intent intent2;
    private TextView total_buy;
    private TextView total_account;
    private TextView information1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        intent2 = getIntent();

        total_buy = (TextView) findViewById(R.id.total_buy);
        total_account = (TextView) findViewById(R.id.total_account);
        information1 = (TextView) findViewById(R.id.information1);


        Log.e("test", intent2.getIntExtra("total_buy", 0) + "");
        total_account.setText("계좌 평가 금액 : " + intent2.getIntExtra("total_buy", 0) + " 원");
        total_buy.setText("총 매수 금액 : " + intent2.getIntExtra("account", 0) + " 원");
    }
}
