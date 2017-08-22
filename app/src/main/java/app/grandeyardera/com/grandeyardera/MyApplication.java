package app.grandeyardera.com.grandeyardera;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by 13118467271 on 2017/8/21.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);

    }
}
