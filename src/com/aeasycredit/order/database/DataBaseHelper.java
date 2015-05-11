
package com.aeasycredit.order.database;

import java.util.ArrayList;
import java.util.List;

import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.utils.AeaConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseHelper {
    // 数据库名称
    private static String DB_NAME = "aeasycredit.db";
    // 数据库版本
    private static int DB_VERSION = 4;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataBaseHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<RequestBody> getRequestBodyList() {
        Cursor cursor = null;
        List<RequestBody> userList = null;
        try {
            RequestBody user = null;
            cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null,
                    null, RequestBody.INVESTIGATEENDTIME + " DESC");
            if (cursor.moveToFirst()) {
                userList = new ArrayList<RequestBody>();
                while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
                    user = new RequestBody();
                    user.setId((cursor.getInt(0)));
                    user.setTaskid(cursor.getString(1));
                    user.setInvestigateType(cursor.getString(2));
                    user.setInvestigateEndTime(cursor.getString(3));
                    user.setInvestigateStartTime(cursor.getString(4));
                    user.setInvestigateAddr(cursor.getString(5));
                    user.setContactName(cursor.getString(6));
                    user.setContactPhone(cursor.getString(7));
                    user.setContactPost(cursor.getString(8));
                    user.setCompanyName(cursor.getString(9));
                    user.setCompanyNature(cursor.getString(10));
                    user.setStaffNumber(cursor.getString(11));
                    user.setBusinessArea(cursor.getString(12));
                    user.setIsHaveCompanyBoard(cursor.getInt(13) != 0);
                    user.setIsHaveCompanyNameAtOfficeArea(cursor.getInt(14) != 0);
                    user.setServiceContent(cursor.getString(15));
                    user.setCompanyScale(cursor.getString(16));
                    user.setProductionApparatus(cursor.getString(17));
                    user.setInventory(cursor.getString(18));
                    user.setInterviewContent(cursor.getString(19));
                    user.setSummary(cursor.getString(20));
                    user.setOther(cursor.getString(21));
                    // user.setImageSize(cursor.getString(20));
                    user.setFiles(cursor.getString(22));
                    userList.add(user);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userList;
    }

    public RequestBody getRequestBodyByTaskIdAndType(String taskId, int type) {
        Cursor cursor = null;
        RequestBody user = null;
        try {
            cursor = db.query(SqliteHelper.TB_NAME, null, RequestBody.TASKID
                    + "=? and " + RequestBody.INVESTIGATETYPE + "=?", new String[] {
                    taskId, type + ""
            }, null, null, RequestBody.INVESTIGATEENDTIME + " DESC");
            cursor.moveToFirst();
            if (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
                user = new RequestBody();
                user.setId((cursor.getInt(0)));
                user.setTaskid(cursor.getString(1));
                user.setInvestigateType(cursor.getString(2));
                user.setInvestigateEndTime(cursor.getString(3));
                user.setInvestigateStartTime(cursor.getString(4));
                user.setInvestigateAddr(cursor.getString(5));
                user.setContactName(cursor.getString(6));
                user.setContactPhone(cursor.getString(7));
                user.setContactPost(cursor.getString(8));
                user.setCompanyName(cursor.getString(9));
                user.setCompanyNature(cursor.getString(10));
                user.setStaffNumber(cursor.getString(11));
                user.setBusinessArea(cursor.getString(12));
                user.setIsHaveCompanyBoard(cursor.getInt(13) != 0);
                user.setIsHaveCompanyNameAtOfficeArea(cursor.getInt(14) != 0);
                user.setServiceContent(cursor.getString(15));
                user.setCompanyScale(cursor.getString(16));
                user.setProductionApparatus(cursor.getString(17));
                user.setInventory(cursor.getString(18));
                user.setInterviewContent(cursor.getString(19));
                user.setSummary(cursor.getString(20));
                user.setOther(cursor.getString(21));
                // user.setImageSize(cursor.getString(20));
                user.setFiles(cursor.getString(22));
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    // 判断users表中的是否包含某个taskId and type的记录
    public Boolean haveRequestBody(String taskId, int investigateType) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper.TB_NAME, null, RequestBody.TASKID
                + "=? and " + RequestBody.INVESTIGATETYPE + "=?", new String[] {
                taskId, investigateType + ""
        }, null, null, null);
        b = cursor.moveToFirst();
        Log.e(AeaConstants.TAG, "hava " + taskId + " and type " + investigateType + " = " + b);
        cursor.close();
        return b;
    }

    // 更新users表的记录，根据UserId更新用户昵称和用户图标
    public boolean updateRequestBody(RequestBody body) {
        ContentValues values = new ContentValues();
        values.put(RequestBody.TASKID, body.getTaskid());
        values.put(RequestBody.INVESTIGATETYPE, body.getInvestigateType());
        values.put(RequestBody.INVESTIGATEENDTIME, body.getInvestigateEndTime());
        values.put(RequestBody.INVESTIGATESTARTTIME, body.getInvestigateStartTime());
        values.put(RequestBody.INVESTIGATEADDR, body.getInvestigateAddr());
        values.put(RequestBody.CONTACTNAME, body.getContactName());
        values.put(RequestBody.CONTACTPHONE, body.getContactPhone());
        values.put(RequestBody.CONTACTPOST, body.getContactPost());
        values.put(RequestBody.COMPANYNAME, body.getCompanyName());
        values.put(RequestBody.COMPANYNATURE, body.getCompanyNature());
        values.put(RequestBody.STAFFNUMBER, body.getStaffNumber());
        values.put(RequestBody.BUSINESSAREA, body.getBusinessArea());
        int isHavaCompanyboard = body.getIsHaveCompanyBoard() ? 1 : 0;
        values.put(RequestBody.ISHAVECOMPANYBOARD, isHavaCompanyboard);
        int isHaveCompanyNameInArea = body.getIsHaveCompanyNameAtOfficeArea() ? 1 : 0;
        values.put(RequestBody.ISHAVECOMPANYNAMEATOFFICEAREA, isHaveCompanyNameInArea);
        values.put(RequestBody.SERVICECONTENT, body.getServiceContent());
        values.put(RequestBody.COMPANYSCALE, body.getCompanyScale());
        values.put(RequestBody.PRODUCTIONAPPARATUS, body.getProductionApparatus());
        values.put(RequestBody.INVENTORY, body.getInventory());
        values.put(RequestBody.INTERVIEWCONTENT, body.getInterviewContent());
        values.put(RequestBody.SUMMARY, body.getSummary());
        values.put(RequestBody.OTHER, body.getOther());
        values.put(RequestBody.FILES, body.getFiles());
        // 构造SQLite的Content对象，这里也可以使用raw
        int id = db.update(SqliteHelper.TB_NAME, values, RequestBody.TASKID + "=? and "
                + RequestBody.INVESTIGATETYPE + "=?", new String[] {
                body.getTaskid(), body.getInvestigateType()
        });
        Log.e(AeaConstants.TAG, "update body = " + id);
        return id > 0;
    }

    // // 更新users表的记录
    // public int UpdateUserInfo(RequestBody body) {
    // ContentValues values = new ContentValues();
    // values.put(UserInfo.USERID, user.getUserId());
    // values.put(UserInfo.TOKEN, user.getToken());
    // values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
    // int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "="
    // + user.getUserId(), null);
    // Log.e("UpdateUserInfo", id + "");
    // return id;
    // }

    // 添加users表的记录
    public boolean insertRequestBody(RequestBody body) {
        ContentValues values = new ContentValues();
        values.put(RequestBody.TASKID, body.getTaskid());
        values.put(RequestBody.INVESTIGATETYPE, body.getInvestigateType());
        values.put(RequestBody.INVESTIGATEENDTIME, body.getInvestigateEndTime());
        values.put(RequestBody.INVESTIGATESTARTTIME, body.getInvestigateStartTime());
        values.put(RequestBody.INVESTIGATEADDR, body.getInvestigateAddr());
        values.put(RequestBody.CONTACTNAME, body.getContactName());
        values.put(RequestBody.CONTACTPHONE, body.getContactPhone());
        values.put(RequestBody.CONTACTPOST, body.getContactPost());
        values.put(RequestBody.COMPANYNAME, body.getCompanyName());
        values.put(RequestBody.COMPANYNATURE, body.getCompanyNature());
        values.put(RequestBody.STAFFNUMBER, body.getStaffNumber());
        values.put(RequestBody.BUSINESSAREA, body.getBusinessArea());
        int isHavaCompanyboard = body.getIsHaveCompanyBoard() ? 1 : 0;
        values.put(RequestBody.ISHAVECOMPANYBOARD, isHavaCompanyboard);
        int isHaveCompanyNameInArea = body.getIsHaveCompanyNameAtOfficeArea() ? 1 : 0;
        values.put(RequestBody.ISHAVECOMPANYNAMEATOFFICEAREA, isHaveCompanyNameInArea);
        values.put(RequestBody.SERVICECONTENT, body.getServiceContent());
        values.put(RequestBody.COMPANYSCALE, body.getCompanyScale());
        values.put(RequestBody.PRODUCTIONAPPARATUS, body.getProductionApparatus());
        values.put(RequestBody.INVENTORY, body.getInventory());
        values.put(RequestBody.INTERVIEWCONTENT, body.getInterviewContent());
        values.put(RequestBody.SUMMARY, body.getSummary());
        values.put(RequestBody.OTHER, body.getOther());
        values.put(RequestBody.FILES, body.getFiles());
        Long uid = db.insert(SqliteHelper.TB_NAME, null, values);
        Log.e(AeaConstants.TAG, uid + "");
        return uid > -1;
    }

    // // 添加users表的记录
    // public Long SaveUserInfo(UserInfo user, byte[] icon) {
    // ContentValues values = new ContentValues();
    // values.put(UserInfo.USERID, user.getUserId());
    // values.put(UserInfo.USERNAME, user.getUserName());
    // values.put(UserInfo.TOKEN, user.getToken());
    // values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
    // if (icon != null) {
    // values.put(UserInfo.USERICON, icon);
    // }
    // Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
    // Log.e("SaveUserInfo", uid + "");
    // return uid;
    // }

    public boolean delRequestBodyByIdAndType(String taskId, int type) {
        int id = db.delete(SqliteHelper.TB_NAME,
                RequestBody.TASKID + "=? and " + RequestBody.INVESTIGATETYPE + "=?", new String[] {
                        taskId, type + ""
                });
        Log.e(AeaConstants.TAG, id + "");
        return id > 0;
    }
    
    public boolean delRequestBodyById(String taskId) {
        int id = db.delete(SqliteHelper.TB_NAME,
                RequestBody.TASKID + "=?" , new String[] {
                taskId
        });
        Log.e(AeaConstants.TAG, id + "");
        return id > 0;
    }

    public boolean clearTable() {
        int id = db.delete(SqliteHelper.TB_NAME,
                null, null);
        Log.e(AeaConstants.TAG, id + "");
        return id > 0;
    }

}
