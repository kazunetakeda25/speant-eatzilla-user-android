package com.speant.user.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.LoginPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.Keywords;
import static com.speant.user.Common.CONST.Params;

//Login,signup,forgot password,reset password, otp varification all in same page

public class LoginActivity extends BaseActivity implements TextWatcher {

    private static final String TAG = "LoginActivity";
    private static final int MY_SMS_CODE = 186;
    private static final int RC_SIGN_IN = 101;

    @BindView(R.id.sign_in_txt)
    TextView signInTxt;
    @BindView(R.id.sign_up_txt)
    TextView signUpTxt;
    @BindView(R.id.login_phone_num_edt)
    EditText loginPhoneNumEdt;
    @BindView(R.id.login_password_edt)
    EditText loginPasswordEdt;
    @BindView(R.id.login_forgot_pswd_txt)
    TextView loginForgotPswdTxt;
    @BindView(R.id.sign_in_linear)
    LinearLayout signInLinear;
    @BindView(R.id.signup_phone_num_edt)
    EditText signupPhoneNumEdt;
    @BindView(R.id.sign_up_verified_txt)
    TextView signUpVerifiedTxt;
    @BindView(R.id.signup_email_edt)
    EditText signupEmailEdt;
    @BindView(R.id.signup_password_edt)
    EditText signupPasswordEdt;
    @BindView(R.id.signup_linear)
    LinearLayout signupLinear;
    @BindView(R.id.forgot_phone_num_edt)
    EditText forgotPhoneNumEdt;
    @BindView(R.id.forgot_linear)
    LinearLayout forgotLinear;
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
    @BindView(R.id.resend_otp_txt)
    TextView resendOtpTxt;
    @BindView(R.id.forgot_otp_linear)
    LinearLayout forgotOtpLinear;
    @BindView(R.id.forgot_new_password_edt)
    EditText forgotNewPasswordEdt;
    @BindView(R.id.forgot_confirm_password_edt)
    EditText forgotConfirmPasswordEdt;
    @BindView(R.id.forgot_new_pswd_linear)
    LinearLayout forgotNewPswdLinear;
    @BindView(R.id.signin_card_relative)
    RelativeLayout signinCardRelative;
    @BindView(R.id.card_relative)
    RelativeLayout cardRelative;
    @BindView(R.id.login_linear)
    LinearLayout loginLinear;
    @BindView(R.id.submit_txt)
    TextView submitTxt;
    @BindView(R.id.login_social_linear)
    LinearLayout loginSocialLinear;
    @BindView(R.id.social_googel_img)
    ImageView socialGoogelImg;
    @BindView(R.id.social_fb_img)
    ImageView socialFbImg;
    @BindView(R.id.login_up_img)
    ImageView loginUpImg;
    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.ccp_signup)
    CountryCodePicker ccpSignup;
    @BindView(R.id.ccp_forget)
    CountryCodePicker ccpForget;

    String lastCardStr = "";
    String password;
    APIInterface apiInterface;
    SessionManager sessionManager;
    @BindView(R.id.login_skip_txt)
    TextView loginSkipTxt;
    @BindView(R.id.txt_demo)
    AppCompatTextView txtDemo;
    private String otpStr = "";
    private String phoneStr = "";
    private String verifiedMobile;
    private boolean fromVerification;


    //Social Login
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTextChangeListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green));
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);


        FirebaseApp.initializeApp(this);


        /*if (refreshedToken == null) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sessionManager.saveDeviceToken(refreshedToken);
            Log.e(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "onCreate: " + sessionManager.getDeviceToken());
        }*/

        mAuth = FirebaseAuth.getInstance();

        ccpSignup.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if (verifiedMobile != null) {
                    signUpVerifiedTxt.setText("Verify");
                    signUpVerifiedTxt.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();
        fbLogin();*/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Log.e(TAG, "onCreate: " + sessionManager.getDeviceToken());

        forgotOtp1Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp1Edt));
        forgotOtp2Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp2Edt));
        forgotOtp3Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp3Edt));
        forgotOtp4Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp4Edt));
        forgotOtp5Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp5Edt));
        forgotOtp6Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp6Edt));
        forgotOtp6Edt.setVisibility(View.GONE);

        visibilityChanges(Keywords.sign_in);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_SMS_CODE);
            }
        }

        loginPhoneNumEdt.setText("9600771099");
        loginPasswordEdt.setText("12345678");

    }

    private void setTextChangeListener() {

    }

    @OnClick({R.id.sign_in_txt, R.id.sign_up_txt, R.id.login_forgot_pswd_txt, R.id.otp_mbl_num_txt,
            R.id.resend_otp_txt, R.id.sign_up_verified_txt, R.id.signup_phone_num_edt, R.id.submit_txt,
            R.id.social_googel_img, R.id.social_fb_img, R.id.login_skip_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_in_txt:
                visibilityChanges(Keywords.sign_in);
                break;
            case R.id.sign_up_txt:
                fromVerification = false;
                visibilityChanges(Keywords.sign_up);
                break;
            case R.id.login_forgot_pswd_txt:
                lastCardStr = Keywords.forgot_pswd;
                visibilityChanges(Keywords.forgot_pswd);
                break;
            case R.id.signup_phone_num_edt:
                signupPhoneNumEdt.setError(null);
                break;
            case R.id.login_skip_txt:
                Log.e(TAG, "onViewClicked:login_skip_txt ");
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finishAffinity();
                break;
            case R.id.sign_up_verified_txt:
                if (!signUpVerifiedTxt.getText().toString().equalsIgnoreCase("Verified")) {
                    if (signupPhoneNumEdt.getText().toString().length() < 8) {
                        signupPhoneNumEdt.setError("Enter Valid Mobile Number", null);
                    } else {
                        phoneStr = ccpSignup.getSelectedCountryCode() + signupPhoneNumEdt.getText().toString();
                        otpMblNumTxt.setText(phoneStr);
                        visibilityChanges(Keywords.verification);

                        HashMap<String, String> map = new HashMap<>();
                        map.put(Params.phone, phoneStr);
                        jsonRequestOtp("send_otp", map);
                    }
                }
                break;
            case R.id.otp_mbl_num_txt:
                if (lastCardStr.equalsIgnoreCase(Keywords.sign_up)) {
                    visibilityChanges(Keywords.sign_up);
                } else if (lastCardStr.equalsIgnoreCase(Keywords.forgot_pswd)) {
                    visibilityChanges(Keywords.forgot_pswd);
                }
                break;
            case R.id.resend_otp_txt:
                resendOtpTxt.setVisibility(View.GONE);
                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, phoneStr);
                jsonRequestOtp("send_otp", map);
                break;
            case R.id.submit_txt:
                submitBtn();
                break;
            case R.id.social_googel_img:
                signIn();
                break;
            case R.id.social_fb_img:
                break;
        }
    }

    public void submitBtn() {

        //Login
        if (signInLinear.getVisibility() == View.VISIBLE) {

            if (loginPhoneNumEdt.getText().toString().length() < 8) {
                loginPhoneNumEdt.setError("Enter valid Mobile Number");
            } else if (loginPasswordEdt.getText().toString().length() < 6) {
                loginPasswordEdt.setError("Enter Valid Password");
            } else {
                password = loginPasswordEdt.getText().toString().trim();
                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, ccp.getSelectedCountryCode() + loginPhoneNumEdt.getText().toString().trim());
                map.put(Params.password, loginPasswordEdt.getText().toString().trim());
                map.put(Params.login_type, "0");
//                map.put(Params.device_token, refreshedToken);
                map.put(Params.device_token, sessionManager.getDeviceToken());
                map.put(Params.device_type, "android");
                jsonLogin("login", map);
            }
        }

        //Signup
        if (signupLinear.getVisibility() == View.VISIBLE) {

            if (signupPhoneNumEdt.getText().toString().length() < 8) {
                signupPhoneNumEdt.setError("Enter valid Mobile Number");
            } else if (!signUpVerifiedTxt.getText().toString().equalsIgnoreCase("Verified")) {
                CommonFunctions.shortToast(getApplicationContext(), "Verify Your Phone Number");
            } else if (!CommonFunctions.isValidEmail(signupEmailEdt.getText().toString())) {
                signupEmailEdt.setError("Enter Valid Email");
            } else if (signupPasswordEdt.getText().toString().length() < 6) {
                signupPasswordEdt.setError("Enter Valid Password");
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, ccpSignup.getSelectedCountryCode() + signupPhoneNumEdt.getText().toString().trim());
                map.put(Params.password, signupPasswordEdt.getText().toString().trim());
                map.put(Params.login_type, "0");
                map.put(Params.email, signupEmailEdt.getText().toString().trim());
                map.put(Params.device_token, sessionManager.getDeviceToken());
//                map.put(Params.device_token, refreshedToken);
                map.put(Params.device_type, "android");
                jsonLogin("register", map);
            }

        }

        //Forgot password
        if (forgotLinear.getVisibility() == View.VISIBLE) {
            fromVerification = true;
            phoneStr = ccpForget.getSelectedCountryCode() + forgotPhoneNumEdt.getText().toString();
            if (phoneStr.length() < 8) {
                forgotPhoneNumEdt.setError("Valid Mobile Number");
            } else {
                otpMblNumTxt.setText(phoneStr);
                visibilityChanges(Keywords.verification);

                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, phoneStr);
                jsonRequestOtp("send_otp", map);
            }

        } else if (forgotOtpLinear.getVisibility() == View.VISIBLE) { //OTP verificaton Linear
            if (lastCardStr.equalsIgnoreCase(Keywords.sign_up)) {
                confirmOtp();
            } else if (lastCardStr.equalsIgnoreCase(Keywords.forgot_pswd)) {
                confirmOtp();
            }

        } else if (forgotNewPswdLinear.getVisibility() == View.VISIBLE) { //REset password screen
            fromVerification = true;
            String pswd1 = forgotNewPasswordEdt.getText().toString();
            String pswd2 = forgotConfirmPasswordEdt.getText().toString();

            if (pswd1.length() < 8) {
                forgotNewPasswordEdt.setError("Enter Valid Password");
            } else if (!pswd1.contentEquals(pswd2)) {
                forgotConfirmPasswordEdt.setError("Password Mismatch");
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, phoneStr);
                map.put(Params.password, pswd1);
                jsonRequestOtp("reset_password", map);
            }

        }

    }

    public void visibilityChanges(String show) {

        switch (show) {
            case Keywords.sign_in:
                loginPhoneNumEdt.setText("");
                loginPasswordEdt.setText("");
                signInLinear.setVisibility(View.VISIBLE);
                signupLinear.setVisibility(View.GONE);
                forgotLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.GONE);
                forgotNewPswdLinear.setVisibility(View.GONE);
//                loginSocialLinear.setVisibility(View.VISIBLE);
                signInTxt.setTextColor(Color.WHITE);
                signUpTxt.setTextColor(getResources().getColor(R.color.lit_grey));
                submitTxt.setText("Sign In");
                loginUpImg.setImageResource(R.drawable.login_up_img);
                break;
            case Keywords.sign_up:
                if (!fromVerification) {
                    Log.e(TAG, "visibilityChanges:fromVerification " + fromVerification);
                    signupPhoneNumEdt.setText("");
                    signupEmailEdt.setText("");
                    signupPasswordEdt.setText("");
                }
                lastCardStr = Keywords.sign_up;
                signInLinear.setVisibility(View.GONE);
                signupLinear.setVisibility(View.VISIBLE);
                forgotLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.GONE);
                forgotNewPswdLinear.setVisibility(View.GONE);
//                loginSocialLinear.setVisibility(View.GONE);
                signUpTxt.setTextColor(Color.WHITE);
                signInTxt.setTextColor(getResources().getColor(R.color.lit_grey));
                submitTxt.setText("Sign Up");
                loginUpImg.setImageResource(R.drawable.signup_up_img);
                break;
            case Keywords.forgot_pswd:
                lastCardStr = Keywords.forgot_pswd;
                signInLinear.setVisibility(View.GONE);
                signupLinear.setVisibility(View.GONE);
                forgotLinear.setVisibility(View.VISIBLE);
                forgotOtpLinear.setVisibility(View.GONE);
                forgotNewPswdLinear.setVisibility(View.GONE);
//                loginSocialLinear.setVisibility(View.GONE);
                submitTxt.setText("Get OTP");
                loginUpImg.setImageResource(R.drawable.forgot_up_img);
                break;
            case Keywords.verification:
                signupPhoneNumEdt.setError(null);
                otpMblNumTxt.setText(phoneStr);
                signInLinear.setVisibility(View.GONE);
                signupLinear.setVisibility(View.GONE);
                forgotLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.VISIBLE);
                forgotNewPswdLinear.setVisibility(View.GONE);
//                loginSocialLinear.setVisibility(View.GONE);
                submitTxt.setText("Verify");//Setting button name
                loginUpImg.setImageResource(R.drawable.verification_up_img);
                break;
            case Keywords.new_pswd:
                signInLinear.setVisibility(View.GONE);
                signupLinear.setVisibility(View.GONE);
                forgotLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.GONE);
                forgotNewPswdLinear.setVisibility(View.VISIBLE);
//                loginSocialLinear.setVisibility(View.GONE);
                submitTxt.setText("Next");
                loginUpImg.setImageResource(R.drawable.new_pswd_up_img);
                break;
        }
    }

    public void jsonLogin(String type, HashMap<String, String> map) {

        if (CommonFunctions.isGPSEnabled(this)) {
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
                            Log.e("Giri ", "onResponse:password " + password);
                            sessionManager.createLoginSession(lo.getAuthid(), lo.getAuthtoken(), lo.getUserName(),
                                    lo.getEmail(), lo.getPhone(), lo.getProfileImage(), password);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else
                            CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                    } else if (response.code() == 401) {
                        sessionManager.logoutUser();
                    }
                }

                @Override
                public void onFailure(Call<LoginPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else
            CommonFunctions.shortToast(this, "Network not found");


    }

    public void jsonRequestOtp(final String url, HashMap<String, String> map) {
        setSnackBar("Generating OTP.Please Wait");
        if (CommonFunctions.isGPSEnabled(this)) {
            Call<SuccessPojo> call = apiInterface.sendOtp(url, map);
            call.enqueue(new Callback<SuccessPojo>() {
                @Override
                public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                    if (response.code() == 200) {

                        if (response.body().getStatus()) {

                            if (url.equalsIgnoreCase("send_otp")) {
                                otpStr = response.body().getOtp();
                                char[] otpChar = otpStr.toCharArray();
                                Log.e(TAG, "onResponse:otpChar " + otpChar);
                                forgotOtp1Edt.setText("" + otpChar[0]);
                                forgotOtp2Edt.setText("" + otpChar[1]);
                                forgotOtp3Edt.setText("" + otpChar[2]);
                                forgotOtp4Edt.setText("" + otpChar[3]);
                                forgotOtp5Edt.setText("" + otpChar[4]);
                            } else if (url.equalsIgnoreCase("reset_password")) {
                                visibilityChanges(Keywords.sign_in);
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
            CommonFunctions.shortToast(this, "Network not Found");
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");

                    String[] parts = message.split("is"); // escape .
                    String OTP = parts[1].replace(" ", "").trim();

                    String[] value = OTP.split("");
                    forgotOtp1Edt.setText(value[1]);
                    forgotOtp2Edt.setText(value[2]);
                    forgotOtp3Edt.setText(value[3]);
                    forgotOtp4Edt.setText(value[4]);
                    forgotOtp5Edt.setText(value[5]);

                }
            } catch (Exception e) {
                System.out.println(TAG + e.getMessage());
            }

        }
    };

    //text watcher used on Signup MobileNumber EditText Field
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String editedText = s.toString();
        if (verifiedMobile != null && !editedText.equals(verifiedMobile)) {
            signUpVerifiedTxt.setText("Verify");
            signUpVerifiedTxt.setTextColor(getResources().getColor(R.color.txt_color));
        }


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

    public void confirmOtp() {

        String otp1, otp2, otp3, otp4, otp5, otp6;
        otp1 = forgotOtp1Edt.getText().toString().trim();
        otp2 = forgotOtp2Edt.getText().toString().trim();
        otp3 = forgotOtp3Edt.getText().toString().trim();
        otp4 = forgotOtp4Edt.getText().toString().trim();
        otp5 = forgotOtp5Edt.getText().toString().trim();
        otp6 = forgotOtp6Edt.getText().toString().trim();

        String otp_str = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
        otp_str.trim();

        if (otpStr.equalsIgnoreCase(otp_str)) {
            fromVerification = true;
            if (lastCardStr.equalsIgnoreCase(Keywords.sign_up)) {
                visibilityChanges(Keywords.sign_up);
                clearVerificationCode();
                signUpVerifiedTxt.setText("Verified");
                verifiedMobile = phoneStr;
                signUpVerifiedTxt.setError(null);
                signUpVerifiedTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                signupPhoneNumEdt.setTag(signupPhoneNumEdt.getKeyListener());
//                signupPhoneNumEdt.setKeyListener(null);
                signupPhoneNumEdt.addTextChangedListener(this);
            } else if (lastCardStr.equalsIgnoreCase(Keywords.forgot_pswd)) {
                visibilityChanges(Keywords.new_pswd);
            }

        } else
            Snackbar.make(findViewById(R.id.login_layout), "Enter Valid OTP", Snackbar.LENGTH_SHORT).show();


    }

    private void clearVerificationCode() {
        forgotOtp1Edt.setText("");
        forgotOtp2Edt.setText("");
        forgotOtp3Edt.setText("");
        forgotOtp4Edt.setText("");
        forgotOtp5Edt.setText("");
        forgotOtp6Edt.setText("");
    }

    @Override
    public void onBackPressed() {
        if (signInLinear.getVisibility() == View.VISIBLE || signupLinear.getVisibility() == View.VISIBLE)
            super.onBackPressed();

        if (forgotLinear.getVisibility() == View.VISIBLE)
            visibilityChanges(Keywords.sign_in);

        if (forgotOtpLinear.getVisibility() == View.VISIBLE) {
            if (lastCardStr.equalsIgnoreCase(Keywords.forgot_pswd)) {
                visibilityChanges(Keywords.forgot_pswd);
            } else if (lastCardStr.equalsIgnoreCase(Keywords.sign_up)) {
                visibilityChanges(Keywords.sign_up);
            }
        }


        if (forgotNewPswdLinear.getVisibility() == View.VISIBLE)
            visibilityChanges(Keywords.verification);
    }

    @Override
    public void onResume() {
        //this enables broadcast receiver to OTP autofill from received SMS
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //this enables broadcast receiver to OTP autofill from received SMS
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed" + e.getMessage());
                Log.w(TAG, "Google sign in failed" + e.getCause());
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //Social login for Google
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String emailStr = user.getEmail();
            String nameStr = user.getDisplayName();
            String phoneStr = user.getPhoneNumber();
            Log.e(TAG, "updateUI: " + emailStr);

            //sign out of google account
            signOut();

            if (emailStr == null) {
                toast(this, "Email not found.Try another Login");
            } else if (phoneStr == null) {
                toast(this, "Mobile Number not found.Try another Login");
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put(Params.phone, phoneStr);
                map.put(Params.email, emailStr);
                map.put(Params.password, "");
                map.put(Params.login_type, "1");
                map.put(Params.device_token, sessionManager.getDeviceToken());
//                map.put(Params.device_token, refreshedToken);
                map.put(Params.device_type, "android");
                jsonLogin("login", map);
            }
        }

    }

    private void signOut() {

        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast(LoginActivity.this, "Signout Failed" + e.getMessage());
            }
        });
    }
}
