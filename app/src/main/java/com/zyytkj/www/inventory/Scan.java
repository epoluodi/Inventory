package com.zyytkj.www.inventory;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyytkj.www.inventory.WebService.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

public class Scan extends AppCompatActivity {

    ImageView btnreturn;
    private final static String SCAN_ACTION = "urovo.rcv.message";//扫描结束action
    int  pandianid;
    private ScanManager mScanManager;
    String qrcode;

    private String barcodeStr;
    private boolean isScaning = false;
    Thread thread;
    TextView txtinfo;

    private SoundPool soundpool = null;
    private int soundid;
    String mode;
    XlsClass xlsClass = new XlsClass();

    NfcAdapter nfcAdapter;



    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;



            String strbarcode;
            byte[] barcode = intent.getByteArrayExtra("barocode");
            //byte[] barcode = intent.getByteArrayExtra("barcode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            strbarcode = new String(barcode, 0, barocodelen);




            taskcode(strbarcode);
        }

    };

    void taskcode(String code)
    {
        try {
//            "sgccydjt"
            DesUtils des = new DesUtils(Common.strKey);
            barcodeStr = des.decrypt(code);
        }
        catch ( Exception e)
        {e.printStackTrace();}
        Log.i("debug", "----codetype--" + barcodeStr);


        qrcode = Qrcode(barcodeStr);
        if (qrcode.equals(""))
        {
            Toast.makeText(Scan.this,"非资产编码",Toast.LENGTH_SHORT).show();

            return;
        }
        if (mode.equals("online")) {
            thread = new Thread(runnable);
            thread.start();
        }
        else
        {

            if (!Assent.getStringAssentMap().containsKey(qrcode))
            {
                txtinfo.setText(barcodeStr);
                Toast.makeText(Scan.this,"盘点计划中无此资产",Toast.LENGTH_SHORT).show();
                return;
//                    盘点计划中无此资产
            }
            Assent assent = Assent.getStringAssentMap().get(barcodeStr);
            String info = String.format("名称：%1$s\n",assent.getNAME());
            info +=  String.format("资产类型：%1$s\n",assent.getTYPE());
            info +=  String.format("厂商品牌：%1$s\n",assent.getBRAND());
            info +=  String.format("型号：%1$s\n",assent.getMODEL());
            info +=  String.format("保修到期时间：%1$s\n",assent.getWARRANTY());
            info +=  String.format("所属部门：%1$s\n",assent.getDEPARTMENT());
            info +=  String.format("负责人：%1$s\n",assent.getUSER());
            txtinfo.setText(info);

            xlsClass.setStateXls(assent.getIndex());
        }
    }


//    设备名称:防火墙76
//    备案编号:FHQ00162
//    资产编号:FHQ00162
//    安全区域:安全区II
//    所属部门:技术支持部
//    责任人:罗隆材
//    品牌:华为
//    设备分类:防火墙
//    出厂编号:2344142
    String Qrcode(String barcode)
    {
        try {
            if (barcode.contains("资产编号")) {
                int s = barcode.indexOf("资产编号");
                int e = barcode.indexOf("安全区域");
                String code = barcode.substring(s + 5, e - 1);
                Log.i("code: ", code);
                return code;

            } else
                return "";
        }
        catch (Exception e)
        {
            return "";
        }
    }



    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Common.ShowPopWindow(txtinfo,getLayoutInflater(),"同步数据...");
                    break;
                case 0:
                    Common.CLosePopwindow();
                    soundpool.play(soundid, 1, 1, 0, 0, 1);
                    String result = msg.obj.toString();
                    displayQrINfo(result);

                    break;
                case 2:
                    Common.CLosePopwindow();
                    Toast.makeText(Scan.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Common.CLosePopwindow();
                    txtinfo.setText(barcodeStr);
                    Toast.makeText(Scan.this,"盘点计划中无此资产",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    void displayQrINfo(String json)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.getJSONObject("true");
            JSONArray jsonArray = jsonObject1.getJSONObject("DATA").
                    getJSONArray("ASSETS");
            JSONObject jsonObject2 = (JSONObject)jsonArray.get(0);
            String info = String.format("名称：%1$s\n",jsonObject2.getString("NAME"));
            info +=  String.format("资产类型：%1$s\n",jsonObject2.getString("TYPE"));
            info +=  String.format("厂商品牌：%1$s\n",jsonObject2.getString("BRAND"));
            info +=  String.format("型号：%1$s\n",jsonObject2.getString("MODEL"));
            info +=  String.format("保修到期时间：%1$s\n",jsonObject2.getString("WARRANTY"));
            info +=  String.format("所属部门：%1$s\n",jsonObject2.getString("DEPARTMENT"));
            info +=  String.format("负责人：%1$s\n",jsonObject2.getString("USER"));


            txtinfo.setText(info);

//            "REMARK" : "例行盘点（备注）",
//                "DEPARTMENT" : "综合部（所属部门）",
//                "MODEL" : "R710（型号）",
//                "WARRANTY" : "20161020（保修到期时间）",
//                "CODE" : "862287025679010",
//                "BRAND" : "戴尔（厂商品牌）",
//                "TYPE" : "服务器（资产类型）",
//                "NAME" : "数据服务器1（名称）",
//                "ID" : "402881ab4a321131014a323bfef5010d",
//                "USER" : "李四（负责人）"
        }
        catch (Exception e)
        {e.printStackTrace();

        }
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
                propertyInfo.setName("batchId");
                propertyInfo.setValue(PandianPlan.getNewPandianPlan()[pandianid].getID());
                propertyInfos[0] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("code");
                propertyInfo.setValue(qrcode);
                propertyInfos[1] = propertyInfo;
                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"checkQr");

                if (r.equals("-1"))
                {
                    handler.sendEmptyMessage(3);
                    return;
                }

                Message message = handler.obtainMessage();
                message.obj = r;
                message.what =0;
                 Log.i("info:",r);
                handler.sendMessage(message);


            }
            catch (Exception e)
            {e.printStackTrace();}
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        btnreturn = (ImageView)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(onClickListenerreturn);

        pandianid = getIntent().getIntExtra("ID", 0);
        txtinfo = (TextView)findViewById(R.id.txtscaninfo);
        mode = getIntent().getStringExtra("mode");
        xlsClass.openXLS(PandianPlan.getNewPandianPlan()[pandianid].getID());


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null) {
            checkNfc();
        }
//        registerReceiver();


    }
    PendingIntent mPendingIntent;
    public void registerReceiver() {
        if (nfcAdapter != null) {
            String[][] TECHLISTS = new String[][] { { IsoDep.class.getName() },
                    { NfcV.class.getName() }, { NfcF.class.getName() },{ NfcA.class.getName() }
            ,{ NfcB.class.getName() },{ NdefFormatable.class.getName() },
                    { MifareClassic.class.getName() },
                    { MifareUltralight.class.getName() },{ Ndef.class.getName() }};
            IntentFilter[] mFilters = new IntentFilter[]{
                    new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                    new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                    new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            try {
                nfcAdapter.enableForegroundDispatch(this, mPendingIntent,  null, null);
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String ndcid= ByteArrayToHexString(tagFromIntent.getId());
        taskcode(ndcid);
        Toast.makeText(Scan.this,ndcid,Toast.LENGTH_SHORT).show();
    }

    String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    public void unregisterReceiver() {
        if (nfcAdapter != null) {
            try {
                nfcAdapter.disableForegroundDispatch(this);
            } catch (Exception e) {
            }
        }
    }


    protected void checkNfc() {
        if (!nfcAdapter.isEnabled()) {

            startActivity(new Intent(
                    android.provider.Settings.ACTION_NFC_SETTINGS));
        }
        // try {
        // nfcAdapter.wait();
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }


    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
        mScanManager.switchOutputMode(0);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(mScanManager != null) {
            mScanManager.stopDecode();
            mScanManager.closeScanner();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver);
        unregisterReceiver();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initScan();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
        registerReceiver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        xlsClass.closeXLS(PandianPlan.getNewPandianPlan()[pandianid].getID());
    }

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
