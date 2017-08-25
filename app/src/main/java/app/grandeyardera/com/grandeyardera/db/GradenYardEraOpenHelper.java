package app.grandeyardera.com.grandeyardera.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 13118467271 on 2017/8/22.
 */

public class GradenYardEraOpenHelper extends SQLiteOpenHelper {


    public  static final String CREATE_USER = "create table Login ("
            + "id integer primary key autoincrement,"
            + "user_name text,"
            + "user_email text,"
            + "user_password text,"
            + "user_school text,"
            + "user_number integer)"
            ;

    public GradenYardEraOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
