package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import app.grandeyardera.com.grandeyardera.R;

/**
 * Created by 13118467271 on 2017/8/22.
 */

public class RegisterActivity extends Activity{
    private EditText userName;
    private EditText eMail;
    private EditText password;
    private EditText passwordAgain;
    private Button back;
    private Button register;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userName = (EditText)findViewById(R.id.user_name);
        eMail = (EditText)findViewById(R.id.e_mail);
        password = (EditText)findViewById(R.id.register_password);
        passwordAgain = (EditText)findViewById(R.id.register_password_again);
        back = (Button)findViewById(R.id.basic);
        register = (Button)findViewById(R.id.register);
    }
}
