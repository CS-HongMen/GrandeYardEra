package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import app.grandeyardera.com.grandeyardera.R;
import app.grandeyardera.com.grandeyardera.db.GradenYardEraOpenHelper;
import app.grandeyardera.com.grandeyardera.model.GradenYardEraDB;
import app.grandeyardera.com.grandeyardera.model.User;
import app.grandeyardera.com.grandeyardera.util.NetUtil;

/**
 * Created by 13118467271 on 2017/8/21.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText loginMail;
    private EditText loginPassword;
    private Button login;
    private Button findPassword;
    private GradenYardEraDB gradenYardEraDB;
    public  GradenYardEraOpenHelper gradenYardEraOpenHelper = new GradenYardEraOpenHelper(this,"User.db",null,1);

    private Button closeLogin;
    private Button intentRegister;
    private ProgressDialog progressDialog;
    private String result = null;
    private User user;
    private boolean FLAG = false;
    private boolean isLoginFromResgister;
    private Handler handler = new Handler(){
      public void handleMessage(Message msg){
          if (msg.what == 0){
              //Log.d("result" ,result);
              if (result == null){
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(3000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                          progressDialog.dismiss();

                      }

                  }).start();
                  Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();

              } else if (result.equals("error")){
                  Log.d("error", result);
                  progressDialog.dismiss();
                  Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
              }else{
                  progressDialog.dismiss();
                  Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                  Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                  startActivity(intentMainActivity);
                  finish();
              }
          }
      }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        loginMail = (EditText) findViewById(R.id.login_mail);
        loginPassword = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login);
        findPassword = (Button) findViewById(R.id.find_password);
        closeLogin = (Button) findViewById(R.id.close_login);
        intentRegister = (Button) findViewById(R.id.intent_register);
        loginMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        isLoginFromResgister = getIntent().getBooleanExtra("login_from_register",false);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        if (isLoginFromResgister) {
            String userEmail = getIntent().getStringExtra("userEmail");
            loginMail.setText(userEmail);
        }else if (pref != null){
            String userEmail = pref.getString("email","");
            loginMail.setText(userEmail);
        }
        intentRegister.setOnClickListener(this);
        login.setOnClickListener(this);
        closeLogin.setOnClickListener(this);
        findPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intent_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login:
                String emailText = loginMail.getText().toString();
                String passwordText = loginPassword.getText().toString();
                if (emailText.equals("") || passwordText.equals("")){
                    Toast.makeText(this,"请正确填入以上内容",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("等待连接...");
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    checkUser(emailText, passwordText);
                }

                break;
            case R.id.close_login:
                finish();
                break;
            case R.id.find_password:
                Intent intentFindPassword = new Intent(LoginActivity.this,FindPasswordActivity.class);
                startActivity(intentFindPassword);
                finish();
                break;

        }
    }

    private void checkUser(final String email, final String password) {
        new Thread(new Runnable() {


            @Override
            public void run() {
               // HttpURLConnection connection = null;
                String url = "http://cf9ef4ea.ngrok.io/login";
                String request = "user_email=" + email + "&user_password=" + password;
                NetUtil netUtil = new NetUtil();
                result = netUtil.upInfo(url, "", request, "utf-8");
                parseJSONWithGSON(result);
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
                Log.d(TAG, "return" + result);

            }
        }).start();


    }

    public void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        user = gson.fromJson(jsonData,User.class);

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("name",user.getUserName());
        editor.putString("school",user.getUserSchool());
        editor.putString("number",user.getUserNumber());
        editor.putString("password",user.getUserPassword());
        editor.putString("email",user.getUserEmail());
        editor.commit();
    }
}