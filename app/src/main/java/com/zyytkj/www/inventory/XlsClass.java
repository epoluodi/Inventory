package com.zyytkj.www.inventory;

import android.os.Environment;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjw on 15/7/22.
 */
public class XlsClass {

    final static  int DataRowStart = 2;
    HSSFWorkbook hssfWorkbook;
    HSSFSheet hssfSheet;
    HSSFRow hssfRow;




    public void LoadXLS(FileInputStream fileInputStream,int i)
    {
        PandianPlan pandianPlan;
        try {
            hssfWorkbook = new HSSFWorkbook(fileInputStream);
            hssfSheet= hssfWorkbook.getSheetAt(0);
            hssfRow = hssfSheet.getRow(0);
            pandianPlan = new PandianPlan();
            pandianPlan.setID(ConvToString(hssfRow.getCell(0)));
            pandianPlan.setName(ConvToString(hssfRow.getCell(1)));
            pandianPlan.setBEGINDATE(ConvToString(hssfRow.getCell(3)));
            pandianPlan.setUSER(ConvToString(hssfRow.getCell(2)));
            pandianPlan.setENDDATE(ConvToString(hssfRow.getCell(4)));
            pandianPlan.setREMARK(ConvToString(hssfRow.getCell(5)));


            hssfWorkbook.close();

            PandianPlan.setPandianPlansObj(i,pandianPlan);

        }
        catch (Exception e)
        {e.printStackTrace();}



    }


    String ConvToString(HSSFCell hssfCell)
    {
       switch (hssfCell.getCellType()) {
           case 0:
               return String.valueOf(hssfCell.getNumericCellValue());
           case 1:
               return hssfCell.getStringCellValue();
           case 2:
               return "formula";
           case 3:
               return "blank";
           case 4:
               return "boolean";
           case 5:
               return "error";
           default:
               return "";
       }
    }


    public void LoadAssentXLS(File file,String PID)
    {
        Map<String,Assent> stringAssentMap;
        stringAssentMap = new HashMap<>();
        Assent assent;
        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            hssfWorkbook = new HSSFWorkbook(fileInputStream);
            hssfSheet= hssfWorkbook.getSheetAt(0);
            Boolean isFinish=true;
            int indexrow=0;


            while (isFinish)
            {
                hssfRow = hssfSheet.getRow(DataRowStart + indexrow);
                assent = new Assent();
                assent.setID(ConvToString(hssfRow.getCell(0)));
                if (assent.getID().equals(""))
                {
                    isFinish=false;
                    continue;
                }
                assent.setPID(PID);
                assent.setNAME(ConvToString(hssfRow.getCell(1)));
                assent.setTYPE(ConvToString(hssfRow.getCell(2)));
                assent.setMODEL(ConvToString(hssfRow.getCell(3)));
                assent.setBRAND(ConvToString(hssfRow.getCell(4)));
                assent.setCODE(ConvToString(hssfRow.getCell(5)));
                assent.setWARRANTY(ConvToString(hssfRow.getCell(6)));
                assent.setDEPARTMENT(ConvToString(hssfRow.getCell(7)));
                assent.setUSER(ConvToString(hssfRow.getCell(8)));
                assent.setREMARK(ConvToString(hssfRow.getCell(9)));
                assent.setState((int) (hssfRow.getCell(10).getNumericCellValue()));
                assent.setIndex(indexrow);
                stringAssentMap.put(assent.getCODE(),assent);
                indexrow++;
            }



        }
        catch (Exception e) {
            e.printStackTrace();
            }
        finally {
            try {
                hssfWorkbook.close();
            }
            catch (Exception e)
            {e.printStackTrace();}
        }


        Assent.setStringAssentMap(stringAssentMap);

    }


    public void closeXLS(String ID)
    {
        String filenamepathnew = String.format(
                Environment.getExternalStorageDirectory() +
                        File.separator + Common.XLSPATH +  "/%1$s_new.xls",
                ID);
        File filenew = new File(filenamepathnew);
        try {
            hssfWorkbook.write(new FileOutputStream(filenew));
            hssfWorkbook.close();
        }
        catch (Exception e)
        {e.printStackTrace();}
    }

    public void openXLS(String ID)
    {
        String filenamepath = String.format(
                Environment.getExternalStorageDirectory() +
                        File.separator + Common.XLSPATH +  "/%1$s.xls",
                ID);

        File file = new File(filenamepath);



        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            hssfWorkbook = new HSSFWorkbook(fileInputStream);
            hssfSheet = hssfWorkbook.getSheetAt(0);
        }
        catch (Exception e)
        {e.printStackTrace();}


    }
    public void setStateXls(int index)
    {

//        String filenamepath = String.format(
//                Environment.getExternalStorageDirectory() +
//                        File.separator + Common.XLSPATH +  "/%1$s.xls",
//                ID);
//        String filenamepathnew = String.format(
//                Environment.getExternalStorageDirectory() +
//                        File.separator + Common.XLSPATH +  "/%1$s_new.xls",
//                ID);
//        File file = new File(filenamepath);
//        File filenew = new File(filenamepathnew);
        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            hssfWorkbook = new HSSFWorkbook(fileInputStream);
//            hssfSheet= hssfWorkbook.getSheetAt(0);
            hssfRow = hssfSheet.getRow(index + 2);

            hssfRow.getCell(10).setCellValue("1");
//            hssfWorkbook.write(new FileOutputStream(filenew));
//            hssfWorkbook.close();

        }
        catch (Exception e)
        {e.printStackTrace();}

    }



}
