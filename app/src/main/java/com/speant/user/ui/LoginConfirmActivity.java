package com.speant.user.ui;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.locationCheck.GPSChangeNotifyEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.LoginPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.LOGIN;
import static com.speant.user.Common.CONST.LOG_TYPE;
import static com.speant.user.Common.CONST.REGISTER;

public class LoginConfirmActivity extends CurrentLocation {

    private static final String TAG = "LoginConfirmActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edt_mobile)
    AppCompatEditText edtMobile;
    @BindView(R.id.lay_mobile)
    LinearLayout layMobile;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.lay_password)
    LinearLayout layPassword;
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
    @BindView(R.id.edt_email)
    AppCompatEditText edtEmail;
    @BindView(R.id.edt_signup_password)
    AppCompatEditText edtSignupPassword;

    @BindView(R.id.txt_next)
    AppCompatTextView txtNext;
    @BindView(R.id.txt_forgot_password)
    AppCompatTextView txtForgotPassword;
    @BindView(R.id.lay_sign_up_email)
    LinearLayout laySignUpEmail;
    @BindView(R.id.lay_sign_up_password)
    LinearLayout laySignUpPassword;
    private Snackbar snackbar;
    APIInterface apiInterface;
    SessionManager sessionManager;
    private String phoneStr;
    private String passwordStr;
    private String otpStr;
    private String isNewUser;
    private String emailStr;
    private CountDownTimer countDownTimer;
    private String countryCode;
    private LocationUpdate locationUpdate;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setMobileLayVisible();
        locationUpdate = new LocationUpdate(this);
        locationUpdate.locationPermissionCheck(true);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        forgotOtp1Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp1Edt));
        forgotOtp2Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp2Edt));
        forgotOtp3Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp3Edt));
        forgotOtp4Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp4Edt));
        forgotOtp5Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp5Edt));
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceived(ProviderLocation providerLocation) {
        if (!isEventStarted) {
            isEventStarted = true;
            Log.e("TAG", "onLocationReceived:mLastLocation " + providerLocation.getLng());
            location = providerLocation.getLat() + "/" + providerLocation.getLng();
            getCurrentCountryCode();
            isEventStarted = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSChangeNotify(GPSChangeNotifyEvent gpsChangeNotifyEvent) {
        if (!isDialogOpen) {
            Log.e(TAG, "onGPSNotify:EventCalled ");
            locationUpdate.locationPermissionCheck(true);
        }
    }

    @OnClick({R.id.txt_next, R.id.txt_forgot_password, R.id.resend_otp_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_next:
                setNextClickVisibility();
                break;
            case R.id.txt_forgot_password:
                Intent intent = new Intent(LoginConfirmActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("PhoneNumber", phoneStr);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.resend_otp_txt:
                if (resendOtpTxt.getText().toString().equals(getString(R.string.resend_otp))) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, phoneStr);
                    map.put(CONST.Params.is_forgot_password, "0");
                    jsonRequestOtp("send_otp", map);
                }
                break;
        }
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
        if (layMobile.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else if (laySignUpPassword.getVisibility() == View.VISIBLE) {
            setSignUpEmailLayVisible();
        } else {
            setMobileLayVisible();
        }
    }

    private void setNextClickVisibility() {
        if (layMobile.getVisibility() == View.VISIBLE) {
            if (validateMobile()) {
                phoneStr = ccp.getSelectedCountryCode() + edtMobile.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.phone, phoneStr);
                jsonRequestOtp("send_otp", map);
            }
        } else if (layPassword.getVisibility() == View.VISIBLE) {
            if (passwordValidate()) {
                passwordStr = edtPassword.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.phone, phoneStr);
                map.put(CONST.Params.password, passwordStr);
                map.put(CONST.Params.login_type, "0");
                map.put(CONST.Params.device_token, sessionManager.getDeviceToken());
                map.put(CONST.Params.device_type, "android");
                if(sessionManager.getDeviceToken().isEmpty()){
                    CommonFunctions.shortToast(this,"Poor Network Connection!.Check your network");
                    map.put(CONST.Params.device_token, sessionManager.getDeviceToken());
                }else{
                    jsonLogin(LOGIN, map);
                }

            }
        } else if (layOtp.getVisibility() == View.VISIBLE) {
            if (OTPvalidate()) {
                //here we need to check on the otp but now we are prefilling it for demo
                setSignUpEmailLayVisible();
            }
        } else if (laySignUpEmail.getVisibility() == View.VISIBLE) {
            if (signUpEmailValidate()) {
                setSignUpPasswordLayVisible();
            }
        } else if (laySignUpPassword.getVisibility() == View.VISIBLE) {
            if (signUpValidate()) {
                passwordStr = edtSignupPassword.getText().toString().trim();
                emailStr = edtEmail.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.phone, phoneStr);
                map.put(CONST.Params.password, passwordStr);
                map.put(CONST.Params.login_type, "0");
                map.put(CONST.Params.email, emailStr);
                map.put(CONST.Params.device_token, sessionManager.getDeviceToken());
                map.put(CONST.Params.device_type, "android");
                if(sessionManager.getDeviceToken().isEmpty()){
                    CommonFunctions.shortToast(this,"Poor Network Connection!.Check your network");
                    map.put(CONST.Params.device_token, sessionManager.getDeviceToken());
                }else{
                    jsonLogin(REGISTER, map);
                }

            }
        }
    }

    private boolean OTPvalidate() {
        String forgotPwd1, forgotPwd2, forgotPwd3, forgotPwd4, forgotPwd5;
        forgotPwd1 = forgotOtp1Edt.getText().toString();
        forgotPwd2 = forgotOtp2Edt.getText().toString();
        forgotPwd3 = forgotOtp3Edt.getText().toString();
        forgotPwd4 = forgotOtp4Edt.getText().toString();
        forgotPwd5 = forgotOtp5Edt.getText().toString();
        String tempOtp = forgotPwd1+forgotPwd2+forgotPwd3+forgotPwd4+forgotPwd5;
        if (forgotPwd1.isEmpty() || forgotPwd2.isEmpty() || forgotPwd3.isEmpty() || forgotPwd4.isEmpty() || forgotPwd5.isEmpty()) {
            CommonFunctions.shortToast(this, getString(R.string.enter_valid_OTP));
            return false;
        }else if(!tempOtp.equals(otpStr)){
            CommonFunctions.shortToast(this, getString(R.string.enter_valid_OTP));
            return false;
        }
        return true;
    }

    private boolean signUpEmailValidate() {
        if (edtEmail.getText().toString().isEmpty() || !CommonFunctions.isValidEmail(edtEmail.getText().toString())) {
            CommonFunctions.shortToast(this, getString(R.string.enter_mail));
            return false;
        }
        return true;
    }


    private boolean signUpValidate() {
        if (edtEmail.getText().toString().isEmpty() || !CommonFunctions.isValidEmail(edtEmail.getText().toString())) {
            CommonFunctions.shortToast(this, getString(R.string.enter_mail));
            return false;
        } else if (edtSignupPassword.getText().toString().isEmpty() || edtSignupPassword.getText().toString().length() < 8) {
            CommonFunctions.shortToast(this, getString(R.string.enter_your_password));
            return false;
        }
        return true;
    }

    private boolean passwordValidate() {
        if (edtPassword.getText().toString().isEmpty() || edtPassword.getText().toString().length() < 8) {
            CommonFunctions.shortToast(this, getString(R.string.enter_your_password));
            return false;
        }
        return true;
    }

    private boolean validateMobile() {
        if (edtMobile.getText().toString().isEmpty() || edtMobile.getText().toString().length() < 8) {
            CommonFunctions.shortToast(this, getString(R.string.enter_mobile_number));
            return false;
        }
        return true;
    }

    public void jsonRequestOtp(final String url, HashMap<String, String> map) {
        Call<SuccessPojo> call = apiInterface.sendOtp(url, map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {
                    Log.e("ragu", "onResponse: "+new Gson().toJson(response.body()));
                    if (response.body().getStatus()) {
                        if (url.equalsIgnoreCase("send_otp")) {
                            isNewUser = response.body().getIs_new_user();
                            if (response.body().getIs_new_user().equals("0")) {
                                setPasswordLayVisible();
                            } else {
                                setOtpLayVisible();
                                setCountDownTimer();
                                txtMobOtpVerify.setText(phoneStr);
                                otpStr = response.body().getOtp();
                                char[] otpChar = otpStr.toCharArray();
                               /* forgotOtp1Edt.setText("" + otpChar[0]);
                                forgotOtp2Edt.setText("" + otpChar[1]);
                                forgotOtp3Edt.setText("" + otpChar[2]);
                                forgotOtp4Edt.setText("" + otpChar[3]);
                                forgotOtp5Edt.setText("" + otpChar[4]);*/
//--------------------------------------------------------------------
                            }

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

    }

    private void setCountDownTimer() {
        final int[] counter = {60};
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        resendOtpTxt.setText(getString(R.string.resend_otp));
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendOtpTxt.setTextColor(getResources().getColor(R.color.grey));
                resendOtpTxt.setText(getString(R.string.resend_code_in) + " " + String.valueOf(counter[0]));
                counter[0]--;
            }

            public void onFinish() {
                resendOtpTxt.setTextColor(getResources().getColor(R.color.green));
                resendOtpTxt.setText(getString(R.string.resend_otp));
            }
        }.start();
    }

    public void jsonLogin(String type, HashMap<String, String> map) {
        Call<LoginPojo> call = apiInterface.loginorSignup(type, map);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        LoginPojo lo = response.body();
                        Log.e("Giri ", "onResponse:getUserName " + lo.getUserName());
                        Log.e("Giri ", "onResponse:getPhone " + lo.getEmail());
                        Log.e("Giri ", "onResponse:getEmail " + lo.getPhone());
                        Log.e("Giri ", "onResponse:password " + passwordStr);
                        sessionManager.createLoginSession(lo.getAuthid(), lo.getAuthtoken(), lo.getUserName(),
                                lo.getEmail(), lo.getPhone(), lo.getProfileImage(), passwordStr);
                         intent = new Intent(LoginConfirmActivity.this, HomeActivity.class);
                         intent.putExtra(LOG_TYPE,type);
                        startActivity(intent);
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                    Log.e("fail", "onResponse: "+response.body().getMessage() );


                } else if (response.code() == 401) {
                    System.out.println("the response is:"+response);
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }


    private void setInvisibleTransition(View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void setVisibleTranistion(View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setOtpLayVisible() {
        laySignUpEmail.setVisibility(View.GONE);
        laySignUpPassword.setVisibility(View.GONE);
        layPassword.setVisibility(View.GONE);
        layMobile.setVisibility(View.GONE);
        layOtp.setVisibility(View.VISIBLE);
        forgotOtp1Edt.requestFocus();
       /* setInvisibleTransition(layMobile);
        setVisibleTranistion(layOtp);*/
    }

    private void setMobileLayVisible() {
        laySignUpEmail.setVisibility(View.GONE);
        laySignUpPassword.setVisibility(View.GONE);
        layOtp.setVisibility(View.GONE);
        layPassword.setVisibility(View.GONE);
        layMobile.setVisibility(View.VISIBLE);
        edtMobile.requestFocus();
       /* setInvisibleTransition(layPassword);
        setVisibleTranistion(layMobile);*/
    }


    private void setPasswordLayVisible() {
        laySignUpEmail.setVisibility(View.GONE);
        laySignUpPassword.setVisibility(View.GONE);
        layOtp.setVisibility(View.GONE);
        layMobile.setVisibility(View.GONE);
        layPassword.setVisibility(View.VISIBLE);
        edtPassword.requestFocus();
       /* setInvisibleTransition(layMobile);
        setVisibleTranistion(layPassword);*/
    }

    private void setSignUpEmailLayVisible() {
        layOtp.setVisibility(View.GONE);
        layMobile.setVisibility(View.GONE);
        layPassword.setVisibility(View.GONE);
        laySignUpPassword.setVisibility(View.GONE);
        laySignUpEmail.setVisibility(View.VISIBLE);
        edtEmail.requestFocus();
          /* setInvisibleTransition(layMobile);
        setVisibleTranistion(layPassword);*/
    }

    private void setSignUpPasswordLayVisible() {
        layOtp.setVisibility(View.GONE);
        layMobile.setVisibility(View.GONE);
        layPassword.setVisibility(View.GONE);
        laySignUpEmail.setVisibility(View.GONE);
        laySignUpPassword.setVisibility(View.VISIBLE);
        edtSignupPassword.requestFocus();
          /* setInvisibleTransition(layMobile);
        setVisibleTranistion(layPassword);*/

    }

    public void setSnackBar(String message) {
        //getDecorView() uses rootview to display the snackbar
        snackbar = Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.show();
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

    public void getCurrentCountryCode() {
        //GetCurrentLocation from AgentCurrentLocation Activity's String named as "location"

        if (location != null) {
            String[] currentLocation = location.split("/");
            double lat = Double.parseDouble(currentLocation[0]);
            double lng = Double.parseDouble(currentLocation[1]);
            LatLng latLng = new LatLng(lat, lng);
            Log.e("Giri ", "run:getCurrentLocation " + latLng);
          countryCode = Global.getLocationCountryCode(LoginConfirmActivity.this, latLng);
//            countryCode = "880";
            Log.e("Giri ", "run:countryCode " + countryCode);
            if (countryCode != null) {
                ccp.setCountryForNameCode(countryCode);
            }
        }

    }


    @Override
    public void onBackPressed() {
        setBackClickVisibility();
    }
}
