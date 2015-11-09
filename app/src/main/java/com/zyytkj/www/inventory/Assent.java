package com.zyytkj.www.inventory;

import java.util.Map;

/**
 * Created by cjw on 15/7/23.
 */
public class Assent {

//    "ID": "402881ab4a321131014a323bfef50ad4（资产ID）",
//            "NAME": "数据服务器1（名称）",
//            "TYPE": "服务器（资产类型）",
//            "MODEL": "R710（型号）",
//            "BRAND": "戴尔（厂商品牌）",
//            "CODE": "2001-320kj54（序列号）",
//            "REMARK": "例行盘点（备注）",
//            "WARRANTY": "20161020（保修到期时间）",
//            "DEPARTMENT": "综合部（所属部门）",
//            "USER": "李四（负责人）"



    static Map<String,Assent> stringAssentMap;

    public static Map<String, Assent> getStringAssentMap() {
        return stringAssentMap;
    }

    public static void setStringAssentMap(Map<String, Assent> stringAssentMap) {
        Assent.stringAssentMap = stringAssentMap;
    }

    String PID;
    String ID;
    String NAME;
    String TYPE;
    String MODEL;
    String BRAND;
    String CODE;
    String REMARK;
    String WARRANTY;
    String DEPARTMENT;
    String USER;
    int state=0;
    int index=0;

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getBRAND() {
        return BRAND;
    }

    public void setBRAND(String BRAND) {
        this.BRAND = BRAND;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getWARRANTY() {
        return WARRANTY;
    }

    public void setWARRANTY(String WARRANTY) {
        this.WARRANTY = WARRANTY;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }
}
