package com.speant.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
//        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(SplashActivity.this);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }*/

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                if (sessionManager.isFirstTimeOpen()){
                    Intent i = new Intent(SplashActivity.this, OnBoardActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, 3 * 1000); // wait for 2 seconds

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 101) {
                    // Update the progress status
//                    progressStatus += 25;
                    progressStatus=progressStatus+25;
                    Log.e("splash", "run: "+progressStatus );

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            Log.e("progress", "run: "+progressStatus );
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }


}
