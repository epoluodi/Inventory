package com.zyytkj.www.inventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ImageView tempimg = null;
    ImageView btnreturn;
    String mode;
    Thread thread;

    int pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout)findViewById(R.id.listview);
        btnreturn = (ImageView)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(onClickListenerreturn);
        mode = getIntent().getStringExtra("mode");
        initlistlayout();
    }



    void initlistlayout()
    {
        try {
            RelativeLayout relativeLayout;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(421, 82);
            layoutParams.setMargins(0, 20, 0, 0);

            TextView textname, txthead;
            for (int i = 0; i < PandianPlan.getNewPandianPlan().length; i++) {
                relativeLayout = (RelativeLayout) getLayoutInflater().
                        inflate(R.layout.list_batch, null);
                relativeLayout.setTag(i);
                relativeLayout.setOnClickListener(onClickListenerlist);
                textname = (TextView) relativeLayout.findViewById(R.id.txt_pdname);
                txthead = (TextView) relativeLayout.findViewById(R.id.txt_head);
                txthead.setText(String.valueOf(i));
                textname.setText(PandianPlan.getNewPandianPlan()[i].getName());

                linearLayout.addView(relativeLayout, layoutParams);
            }
        }
        catch (Exception e)
        {e.printStackTrace();}

    }


    /**
     * 点击列表
     */
    View.OnClickListener onClickListenerlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (tempimg !=null)
            {
                tempimg.setBackground(getResources().getDrawable(R.mipmap.back_list_head));

            }
            ImageView imageView = (ImageView)v.findViewById(R.id.headimg);
            imageView.setBackground(getResources().getDrawable(R.mipmap.back_list_head_click));
            tempimg = imageView;


            pid = (Integer)v.getTag();

            if (mode.equals("offline"))
            {

                thread = new Thread(runnable);

                thread.start();


            }
            else{
                Intent intent = new Intent(MainActivity.this,PandianInfo.class);
                intent.putExtra("mode","online");
                intent.putExtra("ID", (Integer) v.getTag());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }


        }
    };


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Common.ShowPopWindow(linearLayout,
                            getLayoutInflater(),"加载资产数据");
                    break;
                case 2:
                    Common.CLosePopwindow();
                    Toast.makeText(MainActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();

                    break;
                case 3:
                    Common.CLosePopwindow();
                    Toast.makeText(MainActivity.this, "数据文件不存在", Toast.LENGTH_SHORT).show();

                    break;
                case 0:
                    Common.CLosePopwindow();
                    Intent intent = new Intent(MainActivity.this,PandianInfo.class);
                    intent.putExtra("mode","offline");
                    intent.putExtra("ID", pid);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    break;
            }



        }
    };


    Runnable runnable = new Runnable() {



        @Override
        public void run() {

            try {
                handler.sendEmptyMessage(1);
                Thread.sleep(500);
                String filenamepath = String.format(
                        Environment.getExternalStorageDirectory() +
                                File.separator + Common.XLSPATH +  "/%1$s.xls",
                        PandianPlan.getNewPandianPlan()[pid].getID());

                File file = new File(filenamepath);
                if (!file.exists())
                {
                    handler.sendEmptyMessage(3);
                    return;
                }

                XlsClass xlsClass = new XlsClass();
                xlsClass.LoadAssentXLS(file,PandianPlan.getNewPandianPlan()[pid].getID());

                handler.sendEmptyMessage(0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                handler.sendEmptyMessage(2);
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
