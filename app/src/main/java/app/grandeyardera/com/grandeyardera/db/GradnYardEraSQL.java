package app.grandeyardera.com.grandeyardera.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 13118467271 on 2017/8/22.
 */

public class GradnYardEraSQL extends SQLiteOpenHelper {


    public  static final String CREATE_LOGIN = "create table Login ("
            + "id integer primary key autoincrement,"
            + "login_email text,"
            + "login_password text)";

    public GradnYardEraSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
