package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.PersistableBundle;
//import android.support.annotation.Nullable;

import app.grandeyardera.com.grandeyardera.R;
import app.grandeyardera.com.grandeyardera.model.GradenYardEraDB;


public class WelcomeActivity extends Activity {
    private GradenYardEraDB gradenYardEraDB;
    private String[] emailPassword = new String[2];

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
       // String response = (String) msg.obj;
            if (msg.arg1 == 0){
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }else if (msg.arg1 == 1){
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }
    };
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Message msg = new Message();
                    emailPassword = gradenYardEraDB.loadEmailPassword();
                    if (emailPassword[0] == null || emailPassword[1] == null){
                    msg.arg1 = 0;
                    }else{
                        msg.arg1 = 1;
                    }
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //当计时结束,跳转至主界面
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                startActivity(intent);
//                WelcomeActivity.this.finish();
//            }
//        }, 300);
    }
}
