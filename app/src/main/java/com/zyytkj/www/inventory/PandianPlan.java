package com.zyytkj.www.inventory;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cjw on 15/7/15.
 */
public class PandianPlan {

    static  PandianPlan[] pandianPlans = null;

    String ID;
    String Name;
    String BEGINDATE;
    String USER;
    String ENDDATE;
    String REMARK;

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(String ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public String getBEGINDATE() {
        return BEGINDATE;
    }

    public void setBEGINDATE(String BEGINDATE) {
        this.BEGINDATE = BEGINDATE;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }




    /**
     * 获得盘点计划对象数组
     * @return
     */
    public static PandianPlan[] getNewPandianPlan()
    {
        return pandianPlans;
    }


    public static void InitPandianPlanList(int i)
    {
        pandianPlans = new PandianPlan[i];
    }

    public static void setPandianPlansObj(int i,PandianPlan pandianPlansObj)
    {
        pandianPlans[i] = pandianPlansObj;
    }
    /**
     * 生成盘点计划对象
     * @param json
     * @return
     */
    public static  Boolean  MakeNew(String json)
    {

        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1true  = jsonObject.getJSONObject("true");
            JSONArray  jsonArray = jsonObject1true.getJSONObject("DATA").getJSONArray("BATCH");

            pandianPlans = new PandianPlan[jsonArray.length()];
            for (int i = 0;i<jsonArray.length();i++)
            {
                JSONObject jsonObjectSingle = (JSONObject)jsonArray.get(i);
                pandianPlans[i] = new PandianPlan();
                pandianPlans[i].setID(jsonObjectSingle.getString("ID"));
                pandianPlans[i].setName(jsonObjectSingle.getString("NAME"));
                pandianPlans[i].setBEGINDATE(jsonObjectSingle.getString("BEGINDATE"));
                pandianPlans[i].setUSER(jsonObjectSingle.getString("USER"));
                pandianPlans[i].setENDDATE(jsonObjectSingle.getString("ENDDATE"));
                pandianPlans[i].setREMARK(jsonObjectSingle.getString("REMARK"));

            }



        }
        catch (Exception e)
        {e.printStackTrace();
        return false;}
        return true;
    }


}
