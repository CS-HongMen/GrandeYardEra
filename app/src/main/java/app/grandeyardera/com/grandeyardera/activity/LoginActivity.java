package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import app.grandeyardera.com.grandeyardera.R;

/**
 * Created by 13118467271 on 2017/8/21.
 */

public class LoginActivity extends Activity{

    private EditText loginMail;
    private EditText loginPassword;
    private Button login;
    private Button findPassword;

    private Button closeLogin;
    private Button intentRegister;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        loginMail = (EditText)findViewById(R.id.login_mail);
        loginPassword = (EditText)findViewById(R.id.login_password);
        login = (Button)findViewById(R.id.login);
        findPassword = (Button)findViewById(R.id.find_password);
        closeLogin = (Button)findViewById(R.id.close_login);
        intentRegister = (Button)findViewById(R.id.register);
    }
}
