package heartbeat.social.tcs.socialhb.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.network.NetworkManager;
import heartbeat.social.tcs.socialhb.utility.Utils;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);
        editUsername = (EditText) findViewById(R.id.edtUsername);
        editPassword = (EditText) findViewById(R.id.edtPassword);
        Button btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userName = editUsername.getEditableText().toString();
        String Password = editPassword.getEditableText().toString();
        if (userName.equals("") || Password.equals("")) {
            Utils.showAlert(LoginActivity.this);
        } else {
            NetworkManager networkManager = new NetworkManager(LoginActivity.this);
            try {
                networkManager.makeLoginWebServiceCall(userName, Password);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
