package com.break_demo.trial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {

                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
