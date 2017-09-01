package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import app.grandeyardera.com.grandeyardera.R;
import app.grandeyardera.com.grandeyardera.util.NetUtil;

/**
 * Created by 13118467271 on 2017/8/22.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText userName;
    private EditText eMail;
    private EditText password;
    private EditText passwordAgain;
    private EditText school;
    private EditText number;
    private Button back;
    private Button register;
    private String result;
    private ProgressDialog progressDialog;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg){
            if (msg.what == 0){
                if (result.equals("error")){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"请查看您的邮箱进行验证",Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intentLoginActivity);
                    finish();
                }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        userName = (EditText)findViewById(R.id.user_name);
        eMail = (EditText)findViewById(R.id.e_mail);
        password = (EditText)findViewById(R.id.register_password);
        passwordAgain = (EditText)findViewById(R.id.register_password_again);
        back = (Button)findViewById(R.id.back_login);
        register = (Button)findViewById(R.id.register);
        school = (EditText) findViewById(R.id.school);
        number = (EditText)findViewById(R.id.number);
        back.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                String userNameText = userName.getText().toString();
                String emailText = eMail.getText().toString();
                String passwordText = password.getText().toString();
                String passwordAgainText = passwordAgain.getText().toString();
                String schoolText = school.getText().toString();
                String numberText = number.getText().toString();
                if ("".equals(userNameText) || "".equals(schoolText) ||"".equals(numberText) || "".equals(emailText) || "".equals(passwordText) || "".equals(passwordAgainText)){
                    Toast.makeText(this,"请完整填入以上的信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passwordText.equals(passwordAgainText)) {
                    Toast.makeText(this,"两次输入的密码不同，请修改",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("等待连接...");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                createUser(userNameText,emailText,passwordText);
                break;
            case R.id.back_login:
                Intent intentLoginActivity = new Intent(RegisterActivity.this,LoginActivity.class);
                intentLoginActivity.putExtra("userEmail", eMail.getText().toString());
                intentLoginActivity.putExtra("login_from_register",true);
                startActivity(intentLoginActivity);
                finish();
                break;
        }
    }

    private void createUser(final String userNameText, final String emailText, String passwordText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "";
                String request = "user_name=" + userNameText + "&user_email=" + emailText + "&user_password=" + password;
                NetUtil netUtil = new NetUtil();
                result = netUtil.upInfo(url, "", request, "utf-8");
                Message message = new Message();
                message.what = 0;
                handle.handleMessage(message);
            }
        }).start();
    }

}
