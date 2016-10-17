package heartbeat.social.tcs.socialhb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SessionManager sessionManager = new SessionManager(getApplicationContext());

                //Checking Session
                if (sessionManager.isLoggedIn()) {

                    //Opening Dashboard
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);

                } else {
                    //Opening User Login Activity
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
