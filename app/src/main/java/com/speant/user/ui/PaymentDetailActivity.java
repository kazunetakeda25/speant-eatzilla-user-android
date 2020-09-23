package com.speant.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.app.App;
import com.speant.user.Common.localDb.DbStorage;
import com.speant.user.Common.localDb.GroupDb;
import com.speant.user.Common.localDb.QuantityDb;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.paymentUtils.AppPreference;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.PaymentRequest;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.N0_ADDON;
import static com.speant.user.Common.CONST.PAYMENT_REQUEST;

public class PaymentDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    private static final String TAG = "ActivityPayment";
    @BindView(R.id.cash_on_delivery_radio)
    RadioButton cashOnDeliveryRadio;
    @BindView(R.id.payment_btn)
    AppCompatButton paymentBtn;

    APIInterface apiInterface;
    SessionManager sessionManager;
    String couponStr;
    @BindView(R.id.toolbar_payment)
    Toolbar toolbarPayment;
    @BindView(R.id.card_radio)
    RadioButton cardRadio;
    @BindView(R.id.rad_grup)
    RadioGroup radGrup;
    private AlertDialog alertDialog;
    Boolean alertOpen = false;
    Boolean isPaymentSuccess;
    private boolean isDisableExitConfirmation = false;
    HashMap<String, String> paymentMap = new HashMap();
    ArrayList<String> foodIdList = new ArrayList<>();
    ArrayList<String> foodQtyList = new ArrayList<>();
    ArrayList<String> foodQuantityIdList = new ArrayList<>();
    ArrayList<String> foodQuantityPriceList = new ArrayList<>();
    ArrayList<String> addOnsIdList = new ArrayList<>();
    DbStorage dbStorage;
    String paymentTypeSelected = "1";
    private boolean isServiceCalled = false;
    private PaymentRequest paymentRequest;
    private HashMap<String, String> userDetails;
    private AppPreference mAppPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarPayment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAppPreference = new AppPreference();
        radGrup.setOnCheckedChangeListener(this::onCheckedChanged);
        dbStorage = new DbStorage();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUserDetails();
        getData();

    }



   /* public void jsonCartDetails(HashMap<String, String> map) {

        Call<CheckoutPojo> call = apiInterface.checkout(sessionManager.getHeader(), map);
        call.enqueue(new Callback<CheckoutPojo>() {
            @Override
            public void onResponse(Call<CheckoutPojo> call, Response<CheckoutPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        CheckoutPojo.RestaurantDetail restaurantDetail = response.body().getRestaurantDetail().get(0);
                        CheckoutPojo.Invoice invoice = response.body().getInvoice().get(0);

                        paymentMap.put(CONST.Params.restaurant_id, String.valueOf(restaurantDetail.getRestaurantId()));
                        paymentMap.put(CONST.Params.coupon_code, invoice.getCouponCode());
                        paymentMap.put(CONST.Params.item_total, String.valueOf(invoice.getItemTotal()));
                        paymentMap.put(CONST.Params.offer_discount, String.valueOf(invoice.getOfferDiscount()));
                        paymentMap.put(CONST.Params.restaurant_packaging_charge, String.valueOf(invoice.getRestaurantPackagingCharge()));
                        paymentMap.put(CONST.Params.gst, String.valueOf(invoice.getGst()));
                        paymentMap.put(CONST.Params.delivery_charge, String.valueOf(invoice.getDeliveryCharge()));
                        paymentMap.put(CONST.Params.bill_amount, String.valueOf(invoice.getBillAmount()));

                        paymentBtn.setText("Pay " + sessionManager.getCurrency() + invoice.getBillAmount());

                        List<CheckoutPojo.FoodDetail> foodDetailList = response.body().getFoodDetail();

                        foodIdList.clear();
                        foodQtyList.clear();
                        for (int i = 0; i < foodDetailList.size(); i++) {
                            foodIdList.add(String.valueOf(foodDetailList.get(i).getFoodId()));
                            foodQtyList.add(String.valueOf(foodDetailList.get(i).getQuantity()));
                        }


                    } else {

                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                    }

                }else if(response.code() == 401){
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }

            }

            @Override
            public void onFailure(Call<CheckoutPojo> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }*/

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            paymentRequest = (PaymentRequest) bundle.getSerializable(PAYMENT_REQUEST);
            paymentMap.put(CONST.Params.restaurant_id, paymentRequest.getRestaurant_id());
            paymentMap.put(CONST.Params.coupon_code, paymentRequest.getCoupon_code());
            paymentMap.put(CONST.Params.item_total, paymentRequest.getItem_total());
            paymentMap.put(CONST.Params.offer_discount, String.valueOf(paymentRequest.getOffer_discount()));
            paymentMap.put(CONST.Params.restaurant_packaging_charge, String.valueOf(paymentRequest.getRestaurant_packaging_charge()));
            paymentMap.put(CONST.Params.gst, String.valueOf(paymentRequest.getGst()));
            paymentMap.put(CONST.Params.delivery_charge, String.valueOf(paymentRequest.getDelivery_charge()));
            paymentMap.put(CONST.Params.bill_amount, String.valueOf(paymentRequest.getBill_amount()));
            paymentMap.put(CONST.Params.delivery_type, String.valueOf(paymentRequest.getDelivery_type()));
            paymentMap.put(CONST.Params.total_members, String.valueOf(paymentRequest.getTotal_members()));
            paymentMap.put(CONST.Params.pickup_dining_time, String.valueOf(paymentRequest.getPickup_dining_time()));
            paymentMap.put(CONST.Params.restaurant_discount, String.valueOf(paymentRequest.getRestaurant_discount()));
            paymentMap.put(CONST.Params.is_wallet, String.valueOf(paymentRequest.getIs_wallet()));
            paymentBtn.setText(getString(R.string.pay) + " " + paymentRequest.getWallet_amount() + " " + sessionManager.getCurrency());

            foodIdList.clear();
            foodQtyList.clear();
            foodQuantityIdList.clear();
            foodQuantityPriceList.clear();
            addOnsIdList.clear();

           /* List<FoodItemDb> foodItemDbList = App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll();
            for (FoodItemDb foodItemDb : foodItemDbList) {
                foodIdList.add(foodItemDb.getFood_id());
                foodQtyList.add(String.valueOf(foodItemDb.getFood_qty()));
            }*/

            List<GroupDb> groupDbList = App.getInstance().getmDaoSession().getGroupDbDao().loadAll();
            for (GroupDb groupDb : groupDbList) {
                foodIdList.add(groupDb.getFood_id());
                foodQtyList.add(String.valueOf(groupDb.getGroupCount()));

                List<QuantityDb> quantityDbList = dbStorage.getAddedQuantityDetailsQuery(groupDb.getQuantity_id(), groupDb.getFood_id());
                if (quantityDbList.size() > 0) {
                    if (quantityDbList.get(0).getName().equals("No Quantity")) {
                        foodQuantityIdList.add("0");
                        foodQuantityPriceList.add("0");
                    } else {
                        foodQuantityIdList.add(quantityDbList.get(0).getQuantity_id());
                        foodQuantityPriceList.add(quantityDbList.get(0).getPrice());
                    }
                } else {
                    foodQuantityIdList.add("0");
                    foodQuantityPriceList.add("0");
                }

                if (groupDb.getGroupData().equals(N0_ADDON)) {
                    addOnsIdList.add("0");
                } else {
                    addOnsIdList.add(groupDb.getGroupData());
                }

            }

        } else {
            setSnackBar(getString(R.string.bill_detail_not_received));
        }
    }

    public void jsonPayment(String paymentType) {

        paymentMap.put(CONST.Params.paid_type, paymentType);
        Log.e(TAG, "jsonPayment: " + paymentMap);
        Log.e(TAG, "paymentTypeSelected: " + paymentType);
        Log.e("Nive ", "foodIdList: " + foodIdList);
        Log.e("Nive ", "foodQtyList: " + foodQtyList);

        isServiceCalled = true;
        Call<SuccessPojo> call = apiInterface.finalPayment(sessionManager.getHeader(), paymentMap, foodIdList, foodQtyList, foodQuantityIdList, foodQuantityPriceList, addOnsIdList);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
                        alertSuccess();
                    } else {
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                        if (response.body().getMessage().equalsIgnoreCase("card not found")) {
                            startActivity(new Intent(PaymentDetailActivity.this, AddCardActivity.class));
                        }
                        paymentBtn.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }
                isServiceCalled = false;
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                isServiceCalled = false;
                paymentBtn.setVisibility(View.VISIBLE);
            }
        });

    }

    public void alertSuccess() {

        alertOpen = true;

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.alert_payment_success, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView payment_success_img = view.findViewById(R.id.payment_success_img);
        LinearLayout lay_order_btn = view.findViewById(R.id.lay_order_btn);
        TextView view_order_txt = view.findViewById(R.id.view_order_txt);

        lay_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentDetailActivity.this, HistoryActivity.class);
                startActivity(i);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                alertDialog.cancel();
                Intent i = new Intent(PaymentDetailActivity.this, HomeActivity.class);
                startActivity(i);
                finishAffinity();
                // close this activity

            }
        }, 2 * 1000);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        builder.setCancelable(true);
        alertDialog.show();

    }

    @OnClick(R.id.payment_btn)
    public void onViewClicked() {
        paymentBtn.setVisibility(View.GONE);
        if (paymentTypeSelected.equals("1")) {
            jsonPayment(paymentTypeSelected);
        } else if (paymentTypeSelected.equals("2")) {
            Log.e(TAG, "onViewClicked:paymentTypeSelected 2 ");
//            launchPayUMoneyFlow();
            jsonPayment(paymentTypeSelected);
        }
    }

   /* private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("BoxFood Payment");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("BoxFood Payment");

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = paymentRequest.getBill_amount();
//        double amount = Double.parseDouble("1");
        String txnId = System.currentTimeMillis() + "";
        String phone = userDetails.get(KEY_USER_MOBILE);
        phone = phone.substring(phone.length()-10);
        String productName = "BoxFood Order";
        String firstName = userDetails.get(KEY_USER_NAME).trim().isEmpty() ? "Guest User" : userDetails.get(KEY_USER_NAME);
        String email = userDetails.get(KEY_USER_EMAIL);
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        Log.e(TAG, "launchPayUMoneyFlow:phone "+phone );

        AppEnvironment appEnvironment = ((App) getApplication()).getAppEnvironment();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            //getting hashkey
            mPaymentParams = calculateServerSideHashAndInitiatePayment(mPaymentParams);
            Log.e(TAG, "onViewClicked:startPayUMoneyFlow before ");
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentDetailActivity.this, R.style.AppTheme_Green, mAppPreference.isOverrideResultScreen());


        } catch (Exception e) {
            // some exception occurred
            Log.e(TAG, "launchPayUMoneyFlow: Exception"+e.getMessage() );
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((App) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    CommonFunctions.shortToast(this,"Transaction Processing");
                    //Success Transaction
                    jsonPayment(paymentTypeSelected);
                } else {
                    //Failure Transaction
                    CommonFunctions.shortToast(this,"Transaction Failed");
                    paymentBtn.setVisibility(View.VISIBLE);
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

               *//* new android.app.AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*//*

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }else {
            Log.e("Tag onActivityResult", "" + resultCode);
            paymentBtn.setVisibility(View.VISIBLE);
        }
    }*/


    @Override
    public void onBackPressed() {
        setBackClick();
    }

    private void setBackClick() {
        if (isServiceCalled || alertOpen) {
            CommonFunctions.shortToast(PaymentDetailActivity.this, getResources().getString(R.string.order_confirm_wait_desc));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setBackClick();
                break;
        }
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
        //Refreshing the activity with new selected Video
        Intent intent = new Intent(PaymentDetailActivity.this, PaymentDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.cash_on_delivery_radio:
                paymentTypeSelected = "1";
                break;
            case R.id.card_radio:
                paymentTypeSelected = "2";
                break;

        }

    }

    /*private void getHashKey() {
        Call<SuccessPojo> call = apiInterface.getHaskKey(sessionManager.getHeader(), "" + paymentRequest.getBill_amount(), foodIdList, foodQtyList, foodQuantityIdList, foodQuantityPriceList, addOnsIdList);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        Log.e(TAG, "onResponse:HashKey " + response.body().getHash_key());
                        setPayUTabs(response.body().getHash_key());
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                CommonFunctions.shortToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    private void setPayUTabs(String hash_key) {
//        Log.e(TAG, "setPayUTabs: HashCode"+ getHashCode());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount("" + paymentRequest.getBill_amount())
                .setTxnId("1231231313") // Transaction ID
                .setPhone(userDetails.get(KEY_USER_MOBILE))
                .setProductName("BoxFood Order")
                .setFirstName(userDetails.get(KEY_USER_NAME))
                .setEmail(userDetails.get(KEY_USER_EMAIL))
                *//*.setsUrl(appEnvironment.surl()) // Success URL (surl)
                 .setfUrl(appEnvironment.furl())*//*  //Failure URL (furl)
                .setIsDebug(true)
                .setKey("BucIiJPg")                        // Merchant key
                .setMerchantId("cKdGMuJUOl");             // Merchant ID

        //declare paymentParam object
        PayUmoneySdkInitializer.PaymentParam paymentParam = null;
        try {
            paymentParam = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        paymentParam.setMerchantHash(hash_key);
        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
                this, R.style.AppTheme_default, false);
    }

    private String getHashCode() {
//        String hashString = "BucIiJPg" | "sdadadadasd" | paymentRequest.getBill_amount()| "TEST" | userDetails.get(KEY_USER_NAME) |  userDetails.get(KEY_USER_EMAIL) | "cKdGMuJUOl";
        String hashString = " ";
        String type = "SHA-512";
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result Code is -1 send from Payumoney activity
        Log.d("PaymentDetailActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Log.e(TAG, "onActivityResult: TransactionStatus.SUCCESSFUL");
                } else {
                    //Failure Transaction
                    Log.e(TAG, "onActivityResult: TransactionStatus.FAILURE");
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

            } *//* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*//*
        } else {
            Log.e("Tag onActivityResult", "" + resultCode);
            paymentBtn.setVisibility(View.VISIBLE);
        }
    }*/

}
