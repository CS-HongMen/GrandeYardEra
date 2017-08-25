package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.annotation.Nullable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;

import app.grandeyardera.com.grandeyardera.R;
import app.grandeyardera.com.grandeyardera.util.NetUtil;


public class FindPasswordActivity extends Activity implements View.OnClickListener {

    private EditText findPasswordEmail;
    private String result;
    private ProgressDialog progressDialog;
    private Handler handle = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == 0){
                if (result.equals("error")){
                    progressDialog.dismiss();
                    Toast.makeText(FindPasswordActivity.this,"找回失败",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(FindPasswordActivity.this,"请查看您的邮箱找回密码",Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity = new Intent(FindPasswordActivity.this,LoginActivity.class);
                    startActivity(intentLoginActivity);
                    finish();
                }

            }
        }
    };


    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);
        findPasswordEmail = (EditText) findViewById(R.id.find_password_email);
        Button backLogin = (Button) findViewById(R.id.find_back_login);
        Button sendEmail = (Button) findViewById(R.id.send_email);
        backLogin.setOnClickListener(this);
        sendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_email:
                if (findPasswordEmail.getText().toString().equals("")){
                    Toast.makeText(this,"请输入邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(FindPasswordActivity.this);
                progressDialog.setTitle("等待连接...");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                findPassword(findPasswordEmail.getText().toString());

                break;
            case R.id.find_back_login:
                Intent intent = new Intent(FindPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void findPassword(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "";
                String request = "&user_email=" + s;
                NetUtil netUtil = new NetUtil();
                result = netUtil.upInfo(url, "", request, "utf-8");
                Message message = new Message();
                message.what = 0;
                handle.handleMessage(message);
            }
        }).start();
    }
}