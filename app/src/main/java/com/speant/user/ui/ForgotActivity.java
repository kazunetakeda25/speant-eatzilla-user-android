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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotActivity extends BaseActivity {

    @BindView(R.id.login_welcon_txt)
    TextView loginWelconTxt;
    @BindView(R.id.forgot_phone_edt)
    EditText forgotPhoneEdt;
    @BindView(R.id.forgot_mbl_submit_txt)
    TextView forgotMblSubmitTxt;
    @BindView(R.id.forgot_mbl_linear)
    LinearLayout forgotMblLinear;
    @BindView(R.id.forgot_otp_head_txt)
    TextView forgotOtpHeadTxt;
    @BindView(R.id.otp_mbl_num_txt)
    TextView otpMblNumTxt;
    @BindView(R.id.forgot_otp1_edt)
    EditText forgotOtp1Edt;
    @BindView(R.id.forgot_otp2_edt)
    EditText forgotOtp2Edt;
    @BindView(R.id.forgot_otp3_edt)
    EditText forgotOtp3Edt;
    @BindView(R.id.forgot_otp4_edt)
    EditText forgotOtp4Edt;
    @BindView(R.id.forgot_otp5_edt)
    EditText forgotOtp5Edt;
    @BindView(R.id.forgot_otp6_edt)
    EditText forgotOtp6Edt;
    @BindView(R.id.forgot_otp_submit_txt)
    TextView forgotOtpSubmitTxt;
    @BindView(R.id.forgot_otp_linear)
    LinearLayout forgotOtpLinear;
    @BindView(R.id.forgot_new_pswd_edt)
    EditText forgotNewPswdEdt;
    @BindView(R.id.forgot_conf_pswd_edt)
    EditText forgotConfPswdEdt;
    @BindView(R.id.forgot_pswd_submit_txt)
    TextView forgotPswdSubmitTxt;
    @BindView(R.id.forgot_reset_pswd_linear)
    LinearLayout forgotResetPswdLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        forgotMblLinear.setVisibility(View.VISIBLE);
        forgotOtpLinear.setVisibility(View.GONE);
        forgotResetPswdLinear.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


    }

    public Boolean backCheckVisibility() {

        if (forgotMblLinear.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (forgotOtpLinear.getVisibility() == View.VISIBLE) {
            forgotMblLinear.setVisibility(View.VISIBLE);
            forgotOtpLinear.setVisibility(View.GONE);
            forgotResetPswdLinear.setVisibility(View.GONE);
            return false;
        }

        if (forgotResetPswdLinear.getVisibility() == View.VISIBLE) {
            forgotMblLinear.setVisibility(View.GONE);
            forgotOtpLinear.setVisibility(View.VISIBLE);
            forgotResetPswdLinear.setVisibility(View.GONE);
            return false;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        if (backCheckVisibility()) {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.forgot_mbl_submit_txt, R.id.forgot_otp_submit_txt, R.id.forgot_pswd_submit_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_mbl_submit_txt:
                forgotMblLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.VISIBLE);
                forgotResetPswdLinear.setVisibility(View.GONE);
                break;
            case R.id.forgot_otp_submit_txt:
                forgotMblLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.GONE);
                forgotResetPswdLinear.setVisibility(View.VISIBLE);
                break;
            case R.id.forgot_pswd_submit_txt:
                Intent intent = new Intent(ForgotActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }
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
