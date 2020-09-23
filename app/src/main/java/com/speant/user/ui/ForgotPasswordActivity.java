package com.speant.user.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_new_pwd)
    AppCompatEditText edtNewPwd;
    @BindView(R.id.edt_cnfm_pwd)
    AppCompatEditText edtCnfmPwd;
    @BindView(R.id.txt_reset)
    AppCompatTextView txtReset;
    String phoneStr;
    @BindView(R.id.lay_reset)
    LinearLayout layReset;
    @BindView(R.id.txt_mob_otp_verify)
    AppCompatTextView txtMobOtpVerify;
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
    @BindView(R.id.resend_otp_txt)
    TextView resendOtpTxt;
    @BindView(R.id.txt_demo)
    AppCompatTextView txtDemo;
    @BindView(R.id.lay_otp)
    LinearLayout layOtp;
    private String otpStr;
    private String resetPassword;
    APIInterface apiInterface;
    SessionManager sessionManager;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setResetVisibility();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        phoneStr = getIntent().getStringExtra("PhoneNumber");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        forgotOtp1Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp1Edt));
        forgotOtp2Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp2Edt));
        forgotOtp3Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp3Edt));
        forgotOtp4Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp4Edt));
        forgotOtp5Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp5Edt));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                setBackClickVisibility();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBackClickVisibility() {
        if (layOtp.getVisibility() == View.VISIBLE) {
            setResetVisibility();
        } else if (layReset.getVisibility() == View.VISIBLE) {
            finish();
        }
    }

    private void setResetVisibility() {
        layOtp.setVisibility(View.GONE);
        layReset.setVisibility(View.VISIBLE);
        txtReset.setText(getString(R.string.reset));
    }

    private void setOtpVisibility() {
        txtReset.setText(getString(R.string.confirm_otp));
        layReset.setVisibility(View.GONE);
        layOtp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.txt_reset,R.id.resend_otp_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_reset:
                if (txtReset.getText().toString().equalsIgnoreCase(getString(R.string.reset))) {
                    if (validate()) {
                        resetPassword = edtNewPwd.getText().toString();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(CONST.Params.phone, phoneStr);
                        map.put(CONST.Params.is_forgot_password, "1");
                        jsonRequestOtp("send_otp", map);
                    }
                } else if (txtReset.getText().toString().equalsIgnoreCase(getString(R.string.confirm_otp))) {
                    //here we need to check on the otp but now we are prefilling it for demo
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, phoneStr);
                    map.put(CONST.Params.password, resetPassword);
                    jsonRequestOtp("reset_password", map);
                }
                break;

            case R.id.resend_otp_txt:
                if(resendOtpTxt.getText().toString().equals(getString(R.string.resend_otp))){
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, phoneStr);
                    map.put(CONST.Params.is_forgot_password, "1");
                    jsonRequestOtp("send_otp", map);
                }
        }
    }

    private boolean validate() {
        if (edtNewPwd.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(this, getString(R.string.enter_new_password));
            return false;
        } else if (edtCnfmPwd.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(this, getString(R.string.enter_confirm_password));
            return false;
        } else if (edtCnfmPwd.getText().toString().length() < 8) {
            CommonFunctions.shortToast(this, getString(R.string.minimum_length));
            return false;
        } else if (!edtNewPwd.getText().toString().equals(edtCnfmPwd.getText().toString())) {
            CommonFunctions.shortToast(this,  getString(R.string.password_mismatch));
            return false;
        }
        return true;
    }

    public void jsonRequestOtp(final String url, HashMap<String, String> map) {
        if (CommonFunctions.isGPSEnabled(this)) {
            Call<SuccessPojo> call = apiInterface.sendOtp(url, map);
            call.enqueue(new Callback<SuccessPojo>() {
                @Override
                public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                    if (response.code() == 200) {
                        Log.e("fwd", "onResponse: "+new Gson().toJson(response.body()));

                        if (response.body().getStatus()) {

                            if (url.equalsIgnoreCase("send_otp")) {

                                setOtpVisibility();
                                setCountDownTimer();
                                txtMobOtpVerify.setText(phoneStr);
                                otpStr = response.body().getOtp();
                                char[] otpChar = otpStr.toCharArray();
                                /*forgotOtp1Edt.setText("" + otpChar[0]);
                                forgotOtp2Edt.setText("" + otpChar[1]);
                                forgotOtp3Edt.setText("" + otpChar[2]);
                                forgotOtp4Edt.setText("" + otpChar[3]);
                                forgotOtp5Edt.setText("" + otpChar[4]);*/

                            }
                            else if (url.equalsIgnoreCase("reset_password")) {
                                CommonFunctions.shortToast(ForgotPasswordActivity.this,"Password Reset Successfully");
                                finish();
                            }

                        } else
                            CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                    } else if (response.code() == 401) {
                        sessionManager.logoutUser();
                    }

                }

                @Override
                public void onFailure(Call<SuccessPojo> call, Throwable t) {

                }
            });
        } else {
            CommonFunctions.shortToast(this, getString(R.string.network_not_found));
        }

    }
    private void setCountDownTimer() {
        final int[] counter = {60};
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        resendOtpTxt.setText(getString(R.string.resend_otp));
        countDownTimer = new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                resendOtpTxt.setTextColor(getResources().getColor(R.color.grey));
                resendOtpTxt.setText(getString(R.string.resend_code_in)+" "+String.valueOf(counter[0]));
                counter[0]--;
            }
            public  void onFinish(){
                resendOtpTxt.setTextColor(getResources().getColor(R.color.green));
                resendOtpTxt.setText(getString(R.string.resend_otp));
            }
        }.start();
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();

            switch (view.getId()) {

                case R.id.forgot_otp1_edt:
                    if (text.length() == 1)
                        forgotOtp2Edt.requestFocus();
                    break;
                case R.id.forgot_otp2_edt:
                    if (text.length() == 1)
                        forgotOtp3Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp1Edt.requestFocus();
                    break;
                case R.id.forgot_otp3_edt:
                    if (text.length() == 1)
                        forgotOtp4Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp2Edt.requestFocus();
                    break;
                case R.id.forgot_otp4_edt:
                    if (text.length() == 1)
                        forgotOtp5Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp3Edt.requestFocus();
                    break;
                case R.id.forgot_otp5_edt:
                    if (text.length() == 0)
                        forgotOtp4Edt.requestFocus();
                    else if (text.length() == 1)
//                        confirmOtp();
                        break;
                case R.id.forgot_confirm_password_edt:

                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
