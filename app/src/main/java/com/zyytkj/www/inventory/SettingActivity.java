package com.zyytkj.www.inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    ImageView btnreturn;


    Button btn_online;
    Button btn_offline;
    Button btn_setting;
    Button btn_about;

    EditText textView;
    EditText key;
    Button btn_save;
    Button btn_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btnreturn = (ImageView)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(onClickListenerreturn);

        textView = (EditText)findViewById(R.id.serverurl);
        textView.setText(Common.Serverip);

        key = (EditText)findViewById(R.id.key);
        key.setText(Common.strKey);


        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
            }
        });

        btn_save = (Button)findViewById(R.id.btn_set);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().equals("") )
                {
                    Toast.makeText(SettingActivity.this,"请输入服务地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( key.getText().toString().equals(""))
                {
                    Toast.makeText(SettingActivity.this,"请输入密钥",Toast.LENGTH_SHORT).show();
                    return;
                }
                Common.dbManager.updateconfig("server",textView.getText().toString());
                Common.dbManager.updateconfig("key",key.getText().toString());
                Common.Serverip = textView.getText().toString();
                Common.strKey = key.getText().toString();
                Common.ServerUrl = String.format("http://%1$s:8088/mockVMSWebserviceServiceSoapBinding?WSDL",
                        textView.getText().toString());
                Toast.makeText(SettingActivity.this,"设置成功",Toast.LENGTH_SHORT).show();

            }
        });


        btn_online=(Button)findViewById(R.id.online);
        btn_offline=(Button)findViewById(R.id.offline);
        btn_setting=(Button)findViewById(R.id.setting);
        btn_about=(Button)findViewById(R.id.about);

        btn_online.setOnClickListener(onClickListenerall);
        btn_offline.setOnClickListener(onClickListenerall);
        btn_setting.setOnClickListener(onClickListenerall);
        btn_about.setOnClickListener(onClickListenerall);


    }


    View.OnClickListener onClickListenerall = new View.OnClickListener() {
        @Override
        public void onClick(View v) {



            if (!v.getTag().toString().equals("3")) {

                if (v.getTag().toString().equals("4"))
                {

                }
                finish();
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }


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
