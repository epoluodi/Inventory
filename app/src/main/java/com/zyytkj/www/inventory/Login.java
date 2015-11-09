package com.zyytkj.www.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zyytkj.www.inventory.WebService.Webservice;
import com.zyytkj.www.inventory.db.DBManager;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;

public class Login extends AppCompatActivity {

    Button btnlogin;
    Button btn_online;
    Button btn_offline;
    Button btn_setting;
    Button btn_about;
    Button btn_select = null;

    File file = new File(Environment.getExternalStorageDirectory()+
            File.separator + Common.XLSPATH);
    File[] filexls;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin = (Button)findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(onClickListenerlogin);
        btn_online=(Button)findViewById(R.id.online);
        btn_offline=(Button)findViewById(R.id.offline);
        btn_setting=(Button)findViewById(R.id.setting);
        btn_about=(Button)findViewById(R.id.about);

//        btn_online.setOnClickListener(onClickListenerall);
        btn_offline.setOnClickListener(onClickListeneroffline);
        btn_setting.setOnClickListener(onClickListenerall);
        btn_about.setOnClickListener(onClickListenerall);
        Common.Appcontext =this;



        if (!file.exists())
            file.mkdir();





        if (!CheckDbfile())
            Common.CopyDb();

        DBManager dbManager = new DBManager(this,getFilesDir()+File.separator + "main.db");
        Common.dbManager = dbManager;
        Common.Serverip = Common.dbManager.getconfig("server");
        Common.strKey = Common.dbManager.getconfig("key");
        Common.ServerUrl = String.format("http://%1$s:8088/mockVMSWebserviceServiceSoapBinding?wsdl",
                Common.dbManager.getconfig("server"));




    }


    View.OnClickListener onClickListeneroffline = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          filexls =   file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().endsWith(".xls")
                            && !pathname.getName().contains("_"))
                        return true;
                    return false;
                }
            });

            if (filexls.length == 0 )
            {
                Toast.makeText(Login.this,"没有发现盘点数据文件",Toast.LENGTH_SHORT).show();

                return;
            }




            Log.i("盘点文件数量:", String.valueOf(filexls.length));
            thread=new Thread(runnableoffline);
            thread.start();



        }
    };


    boolean CheckDbfile()
    {
        File file = new File(getFilesDir()+File.separator + "main.db");
//        file.delete();
        return file.exists();
    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try
            {
                Thread.sleep(500);
                handler.sendEmptyMessage(1);

                Webservice webservice = new Webservice(Common.ServerUrl,10000);
                PropertyInfo[] propertyInfos = new PropertyInfo[2];
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("userName");
                propertyInfo.setValue("123");
                propertyInfos[0] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("password");
                propertyInfo.setValue("123");
                propertyInfos[1] = propertyInfo;
                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"longin");

                if (r.equals("-1"))
                {
                    handler.sendEmptyMessage(3);
                    return;
                }

                if (PandianPlan.MakeNew(r))
                    handler.sendEmptyMessage(0);
                else
                    handler.sendEmptyMessage(2);


            }
            catch (Exception e)
            {e.printStackTrace();}
        }
    };


    Runnable runnableoffline = new Runnable() {
        @Override
        public void run() {

            try
            {
                Thread.sleep(500);
                handler.sendEmptyMessage(1);
                FileInputStream fileInputStream;

                XlsClass xlsClass = new XlsClass();
                PandianPlan.InitPandianPlanList(filexls.length);
                for (int i=0;i<filexls.length;i++)
                {

                    fileInputStream = new FileInputStream(filexls[i]);
                    xlsClass.LoadXLS(fileInputStream,i);
                    fileInputStream.close();

                }


                handler.sendEmptyMessage(9);

            }
            catch (Exception e)
            {e.printStackTrace();
                handler.sendEmptyMessage(2);}
        }
    };


    /**
     * 登录按钮
     */
    View.OnClickListener onClickListenerlogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            thread=new Thread(runnable);
            thread.start();

        }
    };




    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what)
            {
                case 1:
                    Common.ShowPopWindow(btnlogin,getLayoutInflater(),"同步数据...");
                    break;
                case 0:
                    Common.CLosePopwindow();
                    intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("mode","online");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    break;
                case 9:
                    Common.CLosePopwindow();
                    intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("mode","offline");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    break;
                case 2:
                    Common.CLosePopwindow();
                    Toast.makeText(Login.this,"数据解析失败",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Common.CLosePopwindow();
                    Toast.makeText(Login.this,"同步失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    void resetbar()
    {
        btn_select.setBackground(getResources().getDrawable(R.drawable.bar_select));
        btn_select=null;
    }


    View.OnClickListener onClickListenerall = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            if (v.getTag().toString().equals("1")) {
                intent = new Intent(Login.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
//                v.setBackground(getResources().getDrawable(R.drawable.bar_select2));

            }
            if (v.getTag().toString().equals("2")) {
                intent = new Intent(Login.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
            if (v.getTag().toString().equals("3")) {
                intent = new Intent(Login.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
            if (v.getTag().toString().equals("4")) {
                intent = new Intent(Login.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
            btn_select =(Button)v;

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode ==4)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("是否退出程序");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;

        }
        return super.onKeyUp(keyCode, event);
    }


    //    void setAnimation()
//    {
//        TranslateAnimation animation = new TranslateAnimation(
//                0, 240 , 0, 0);
//        animation.setInterpolator(new AccelerateInterpolator());
//        animation.setDuration(400);
//        animation.setStartOffset(280);
//        animation.setRepeatCount(0);
////		animation.setRepeatMode(Animation.REVERSE);
//        animation.setFillAfter(true);
////		animationSet.setFillAfter(true);
////		animationSet.addAnimation(animation);
//        imageViewline.startAnimation(animation);
//    }

}
