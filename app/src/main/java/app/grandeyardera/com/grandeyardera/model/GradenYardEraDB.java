package app.grandeyardera.com.grandeyardera.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.grandeyardera.com.grandeyardera.db.GradenYardEraOpenHelper;

/**
 * Created by 13118467271 on 2017/8/23.
 */

public class GradenYardEraDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "grade_yard_era";
    /**
     * 数据库版本
      */
    public static final int VERSION = 1;
    private  static GradenYardEraDB gradenYardEraDB;
    private SQLiteDatabase db;
    /**
     * 将构造方法私有化
     */
    private GradenYardEraDB(Context context){
        GradenYardEraOpenHelper dbHelper = new GradenYardEraOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /**
     * 获取GradenYardEraBD的实例
     */
    public synchronized static GradenYardEraDB getInstentance(Context context){
        if (gradenYardEraDB == null){
            gradenYardEraDB = new GradenYardEraDB(context);
        }
        return gradenYardEraDB;
    }
    /**
     * 将User实例储存到数据库
     */
    public void saveUser(User user){
        if (user != null){
            ContentValues values = new ContentValues();
            values.put("user_name",user.getUserName());
            values.put("user_email",user.getUserEmail());
            values.put("user_password",user.getUserPassword());
            values.put  ("user_number",user.getUserNumber());
            values.put("user_school",user.getUserSchool());
        }
    }
    /**
     * 从数据库读取user的所有信息
     */
    public String[] loadAllUser(){
        String[] allUser = new String[5];
        Cursor cursor = db.query("User",null,null,null,null,null,null);
        User user = new User();
        user.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
        user.setUserEmail(cursor.getString(cursor.getColumnIndex("user_email")));
        user.setUserNumber(cursor.getString(cursor.getColumnIndex("user_number")));
        user.setUserPassword(cursor.getString(cursor.getColumnIndex("user_password")));
        user.setUserSchool(cursor.getString(cursor.getColumnIndex("user_school")));
        allUser[0] = user.getUserName();
        allUser[1] = user.getUserEmail();
        allUser[2] = user.getUserPassword();
        allUser[3] = user.getUserNumber();
        allUser[4] = user.getUserSchool();
        if (cursor != null){
            cursor.close();
        }
        return allUser;
    }
}
