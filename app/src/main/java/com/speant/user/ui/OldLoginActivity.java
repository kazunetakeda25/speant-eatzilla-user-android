package com.speant.user.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OldLoginActivity extends BaseActivity {

    @BindView(R.id.login_welcon_txt)
    TextView loginWelconTxt;
    @BindView(R.id.login_mail_edt)
    EditText loginMailEdt;
    @BindView(R.id.login_pswd_edt)
    EditText loginPswdEdt;
    @BindView(R.id.login_txt)
    TextView loginTxt;
    @BindView(R.id.login_forget_pswd_txt)
    TextView loginForgetPswdTxt;
    @BindView(R.id.login_no_acc_txt)
    TextView loginNoAccTxt;
    private androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    @OnClick({R.id.login_txt, R.id.login_forget_pswd_txt, R.id.login_no_acc_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_txt:
                Intent login = new Intent(OldLoginActivity.this, HomeActivity.class);
                startActivity(login);
                break;
            case R.id.login_forget_pswd_txt:
                Intent intent = new Intent(OldLoginActivity.this,ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.login_no_acc_txt:
                alertSignup();
                break;
        }
    }

    public void alertSignup(){

        // Create a alert dialog builder.
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(OldLoginActivity.this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.dialogue_signup, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView signup_close_img = view.findViewById(R.id.signup_close_img);
        EditText signup_phone_edt = view.findViewById(R.id.signup_phone_edt);
        EditText signup_mail_edt = view.findViewById(R.id.signup_mail_edt);
        EditText signup_pswd_edt = view.findViewById(R.id.signup_pswd_edt);
        TextView signup_submit_txt = view.findViewById(R.id.signup_submit_txt);

        signup_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        signup_submit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                Intent intent = new Intent(OldLoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
