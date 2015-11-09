package com.zyytkj.www.inventory;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.zyytkj.www.inventory.db.DBManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 14-11-29.
 */
public class Common {

    public static String XLSPATH = "盘点文件目录";

    public static Context Appcontext;
    public static String ServerUrl;
    public static String Serverip="";
    public static String strKey="";
    public final static Object olock = new Object();


    public static String BackVer;

    public static DBManager dbManager = null;

    //popwindows方法
    static PopupWindow popupWindow;
    public static View popview;





    //显示popwindows
    public static void ShowPopWindow(View v, LayoutInflater inflater, String text) {


        popview = inflater.inflate(R.layout.popwindows, null);

        ((TextView) popview.findViewById(R.id.poptext)).setText(text);
        popupWindow = new PopupWindow(popview, 330, 120);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.Animationpopwindows);
        popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);

    }

    //设置popwindows中的文本
    public static void Setpoptext(String text) {
        ((TextView) popview.findViewById(R.id.poptext)).setText(text);
    }

    //关闭POPwindows
    public static void CLosePopwindow() {
        popupWindow.dismiss();
    }

    public static NotificationManager getNotificationManager() {
        return (NotificationManager) Appcontext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void CopyDb() {
        InputStream inputStream;
        try {
            inputStream = Appcontext.getResources().openRawResource(R.raw.main);
            byte[] bytebuff = new byte[inputStream.available()];
            inputStream.read(bytebuff);
            File file = new File(Appcontext.getFilesDir()+File.separator + "main.db" );
            if (file.exists())
                file.delete();
//            FileOutputStream fileOutputStream = context.openFileOutput("main.db", Context.MODE_PRIVATE);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(bytebuff);
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }






    public static String serveraddress(String ip) {
        return String.format("http://%1$s:9898/?wsdl", ip);
//        return "http://192.168.1.147:2672/Service1.asmx";
    }

    public static String GetSysTimeString()
    {
        SimpleDateFormat sDateFormat   =   new   SimpleDateFormat("yyyyMMddHHmmss");
        String   date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

    public static String GetSysTime()
    {
        SimpleDateFormat sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String   date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

    public static String GetSysDate()
    {
        SimpleDateFormat sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd");
        String   date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

}
