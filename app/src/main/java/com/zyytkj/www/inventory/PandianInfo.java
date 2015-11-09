package com.zyytkj.www.inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PandianInfo extends AppCompatActivity {

    ImageView btnreturn;
    TextView textView_name;
    TextView textView_startdt;
    TextView textView_enddt;
    Button btn_scan;

    int pandianid;
    String mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandian_info);
        btnreturn = (ImageView)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(onClickListenerreturn);
        btn_scan=(Button)findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(onClickListenerscan);

        textView_name = (TextView)findViewById(R.id.name);
        textView_startdt = (TextView)findViewById(R.id.start_dt);
        textView_enddt = (TextView)findViewById(R.id.end_dt);
        mode = getIntent().getStringExtra("mode");
        pandianid = getIntent().getIntExtra("ID",0);
        textView_name.setText(PandianPlan.getNewPandianPlan()[pandianid].getName());
        textView_startdt.setText(PandianPlan.getNewPandianPlan()[pandianid].getBEGINDATE());
        textView_enddt.setText(PandianPlan.getNewPandianPlan()[pandianid].getENDDATE());

    }


    View.OnClickListener onClickListenerscan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PandianInfo.this,Scan.class);
            intent.putExtra("ID", pandianid);
            intent.putExtra("mode", mode);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        }
    };



    /**
     * 点击返回
     */
    View.OnClickListener onClickListenerreturn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);

        }
    };



}
