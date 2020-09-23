package com.speant.user.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speant.user.BuildConfig;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.Global;
import com.speant.user.Common.Interface.IGPSActivity;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.CancelAlert;
import com.speant.user.Common.callBacks.DeleteAlert;
import com.speant.user.Common.callBacks.DeliveryTypeCallback;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.OfferClickCallBack;
import com.speant.user.Common.callBacks.onCartUpdate;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.DbStorage;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.Common.localDb.FoodItemDbDao;
import com.speant.user.Common.localDb.GroupDb;
import com.speant.user.Common.localDb.QuantityDb;
import com.speant.user.Common.locationCheck.GPSChangeNotifyEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.CheckAddressResponse;
import com.speant.user.Models.CheckOfferResponse;
import com.speant.user.Models.CheckoutPojo;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.PaymentRequest;
import com.speant.user.Models.PlacesPojo;
import com.speant.user.Models.RestaurantData;
import com.speant.user.Models.SavedAddressPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.Models.WalletBalanceResponse;
import com.speant.user.R;
import com.speant.user.ui.adapter.CartCheckoutAdapter;
import com.speant.user.ui.adapter.GooglePlacesAdapter;
import com.speant.user.ui.adapter.SavedAddressAdapter;
import com.speant.user.ui.dialogs.DiningDialog;
import com.speant.user.ui.dialogs.OfferDialog;
import com.speant.user.ui.fragment.LoginBottomFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.APIKEY;
import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.FLAT_OFFER;
import static com.speant.user.Common.CONST.N0_ADDON;
import static com.speant.user.Common.CONST.PAYMENT_REQUEST;
import static com.speant.user.Common.CONST.PERCENTAGE_OFFER;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;
import static com.speant.user.Common.SessionManager.KEY_USER_ID;

public class ViewCartActivity extends CurrentLocation implements IGPSActivity, onCartUpdate,
        LoginSuccessCallBack, CancelAlert, DeliveryTypeCallback,
        RadioGroup.OnCheckedChangeListener, OfferClickCallBack, DeleteAlert, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ViewCartActivity";
    SessionManager sessionManager;
    APIInterface apiInterface;
    APIInterface apiService;
    CartCheckoutAdapter cartCheckoutAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cart_hotel_img)
    ImageView cartHotelImg;
    @BindView(R.id.cart_hotel_name_txt)
    TextView cartHotelNameTxt;
    @BindView(R.id.cart_hotel_place_txt)
    TextView cartHotelPlaceTxt;
    @BindView(R.id.cart_hotel_delivery_txt)
    TextView cartHotelDeliveryTxt;
    @BindView(R.id.cart_address_change_txt)
    TextView cartAddressChangeTxt;
    @BindView(R.id.cart_address_txt)
    TextView cartAddressTxt;
    @BindView(R.id.cart_cupon_code_aply_txt)
    TextView cartCuponCodeAplyTxt;
    @BindView(R.id.apply_code_edt)
    TextView applyCodeEdt;
    @BindView(R.id.cart_food_list_rv)
    RecyclerView cartFoodListRv;
    @BindView(R.id.item_total_amount_txt)
    TextView itemTotalAmountTxt;
    @BindView(R.id.offer_discount_amount_txt)
    TextView offerDiscountAmountTxt;
    @BindView(R.id.packaging_charge_amount_txt)
    TextView packagingChargeAmountTxt;
    @BindView(R.id.gst_amount_txt)
    TextView gstAmountTxt;
    @BindView(R.id.delivery_charge_amount_txt)
    TextView deliveryChargeAmountTxt;
    @BindView(R.id.total_to_pay_amount_txt)
    TextView totalToPayAmountTxt;
    @BindView(R.id.place_order_txt)
    TextView placeOrderTxt;
    @BindView(R.id.lay_address)
    RelativeLayout layAddress;
    @BindView(R.id.txt_not_deliverable)
    TextView txtNotDeliverable;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.rad_door)
    AppCompatRadioButton radDoor;
    @BindView(R.id.rad_pickup)
    AppCompatRadioButton radPickup;
    @BindView(R.id.rad_dine)
    AppCompatRadioButton radDine;
    @BindView(R.id.rad_grp)
    RadioGroup radGrp;
    @BindView(R.id.lay_coup_cart)
    LinearLayout layCoupCart;
    @BindView(R.id.lay_packaging)
    RelativeLayout layPackaging;
    @BindView(R.id.lay_delivery)
    RelativeLayout layDelivery;
    @BindView(R.id.item_total_discount_txt)
    TextView itemTotalDiscountTxt;
    @BindView(R.id.txt_wallet)
    AppCompatCheckBox txtWallet;
    private LinearLayoutManager MyLayoutManager;
    String currencyStr;
    double walletAmount = 0;

    EditText home_address_edt;

    ArrayList<SavedAddressPojo.Data> addressList = new ArrayList();
    private SavedAddressAdapter savedAddressAdapter;
    private GooglePlacesAdapter googlePlacesAdapter;
    private ArrayList<PlacesPojo.Predictions> places_type_list = new ArrayList<>();
    private LinearLayout alert_address_linear;
    private RecyclerView google_place_search_rv;
    private RecyclerView home_address_rv;
    private TextView no_saved_address_txt;
    private Activity activity;
    private AlertDialog alertDialog;
    private double latitude;
    private double longitude;
    private String address;
    DatabaseReference mDatabaseReference;//User update delivery address in firebase
    private ValueEventListener valueEventListener;
    private List<FoodItemDb> foodDetailList = new ArrayList<>();
    private LoginBottomFragment loginBottomFragment;
    double totalTax, billAmount, finalBillAmount, totalDiscount;
    private CheckoutPojo checkoutPojo;
    private String currentAddress;
    private DbStorage dbStorage;
    double lat, lng;
    private LatLng latLng;
    List<GroupDb> groupDbList = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("##.00");
    private LocationUpdate locationUpdate;
    private String restaurantId;
    private boolean isFromAlert = false;//to check currentLocation is Called from Activity or AddressDailog
    List<String> deliveryType = new ArrayList<>();

    public String selectedDeliveryType = "";
    private String selectedTime = "";
    private double totalAmount;
    private String selectedMembers = "";

    NearRestarentPojo.Restaurants restaurantdataSingle;
    private double packagingCharge;
    private double deliveryCharge;
    private double finalDeliveryCharge;
    private double finalPackagingCharge;
    private DiningDialog diningDialog;
    private OfferDialog offerDialog;
    RestaurantData restaurantData;
    double walletReducedBalance;

    HashMap<String, String> paymentMap = new HashMap();
    ArrayList<String> foodIdList = new ArrayList<>();
    ArrayList<String> foodQtyList = new ArrayList<>();
    ArrayList<String> foodQuantityIdList = new ArrayList<>();
    ArrayList<String> foodQuantityPriceList = new ArrayList<>();
    ArrayList<String> addOnsIdList = new ArrayList<>();
    String paymentTypeSelected = "1";
    private boolean isServiceCalled = false;
    private PaymentRequest paymentRequest;
    Boolean alertOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_cart);
        ButterKnife.bind(this);
        toolbar.setTitle("View Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = ViewCartActivity.this;
        dbStorage = new DbStorage();
        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        locationUpdate = new LocationUpdate(this);

        currencyStr = sessionManager.getCurrency();
        radGrp.setOnCheckedChangeListener(this::onCheckedChanged);
        txtWallet.setOnCheckedChangeListener(this::onCheckedChanged);
        restaurantData = dbStorage.getRestaurantDetails();
        if (restaurantData != null) {
            cartHotelNameTxt.setText(restaurantData.getRestaurant_name());
            cartHotelPlaceTxt.setText(restaurantData.getAddress());
            Glide.with(getApplicationContext())
                    .load(restaurantData.getImage())
                    .into(cartHotelImg);
        }

        MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        cartFoodListRv.setLayoutManager(MyLayoutManager);
        cartFoodListRv.setNestedScrollingEnabled(false);
        cartCheckoutAdapter = new CartCheckoutAdapter(this, groupDbList, this::onUpdateCart);
        cartFoodListRv.setAdapter(cartCheckoutAdapter);


    }

    private void setFirebaseListener() {
        // Attach a listener to read the data at our posts reference
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
            mDatabaseReference.addValueEventListener(seListener());
        }
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            setFirebaseListener();
            layAddress.setVisibility(View.VISIBLE);
            placeOrderTxt.setText(getString(R.string.place_order));
        } else {
            layAddress.setVisibility(View.GONE);
            placeOrderTxt.setText(getString(R.string.login));
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceived(ProviderLocation providerLocation) {
        if (!isEventStarted) {
            isEventStarted = true;
            Log.e("TAG", "onLocationReceived:mLastLocation " + providerLocation.getLng());
            location = providerLocation.getLat() + "/" + providerLocation.getLng();
            getCurrentLocation();
            isEventStarted = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSChangeNotify(GPSChangeNotifyEvent gpsChangeNotifyEvent) {
        if (!isDialogOpen) {
            Log.e(TAG, "onGPSNotify:EventCalled ");
            locationUpdate.locationPermissionCheck(false);
        }
    }

    public void getCurrentLocation() {

        //GetCurrentLocation String named as "location"

        if (location != null) {
            String[] currentLocation = location.split("/");
            lat = Double.parseDouble(currentLocation[0]);
            lng = Double.parseDouble(currentLocation[1]);
            latLng = new LatLng(lat, lng);
            Log.e("Giri ", "run:getCurrentLocation " + latLng);
            currentAddress = Global.getAddress(ViewCartActivity.this, latLng);
            String currentCity = Global.getCity(ViewCartActivity.this, latLng);
            HashMap<String, Object> map = new HashMap<>();
            map.put(CONST.CURRENT_ADDRESS, currentAddress);
            map.put(CONST.CURRENT_LATITUDE, lat);
            map.put(CONST.CURRENT_LONGITUDE, lng);
            map.put(CONST.CURRENT_CITY, currentCity);
            Log.e("Giri ", "run:getCurrentLocation map" + map);

            if (isFromAlert) {
                checkDeliveryAvailability(map, true);
            } else {
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
                mDatabaseReference.setValue(map);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");

        checkLoginStatus();
        updateFoodList();
    }


    private void updateFoodList() {
        Log.e(TAG, "updateFoodList:totalDiscount Before" + totalDiscount);
        totalTax = 0;
        billAmount = 0;
        totalDiscount = 0;
        totalAmount = 0;
        Log.e(TAG, "updateFoodList:totalDiscount After" + totalDiscount);
        foodDetailList.clear();
        foodDetailList.addAll(App.getInstance().getmDaoSession().getFoodItemDbDao().queryBuilder().orderAsc(FoodItemDbDao.Properties.Food_name).list());

        groupDbList.clear();
        groupDbList.addAll(App.getInstance().getmDaoSession().getGroupDbDao().loadAll());
        Log.e(TAG, "updateFoodList:groupDbList size " + groupDbList.size());

        if (groupDbList.size() > 0) {

            if (cartFoodListRv.getAdapter() != null) {
                cartFoodListRv.getAdapter().notifyDataSetChanged();
            }

            for (FoodItemDb foodItemDb : foodDetailList) {
                double singleFoodTax = foodItemDb.getFood_cost() * (foodItemDb.getFood_tax() / 100);
                totalTax += (foodItemDb.getFood_qty() * singleFoodTax);

                if (foodItemDb.getOffer_amount() != null &&
                        Double.parseDouble(foodItemDb.getOffer_amount()) > 0) {
                    if (foodItemDb.getDiscount_type().equals(FLAT_OFFER)) {
                        totalDiscount += (Double.parseDouble(foodItemDb.getOffer_amount()));
                        Log.e(TAG, "updateFoodList:totalDiscount FLAT_OFFER " + foodItemDb.getOffer_amount());
                    } else if (foodItemDb.getDiscount_type().equals(PERCENTAGE_OFFER)) {
                        double singleFoodDiscount = foodItemDb.getFood_cost() * (Double.parseDouble(foodItemDb.getOffer_amount()) / 100);
                        totalDiscount += (foodItemDb.getFood_qty() * singleFoodDiscount);
                        Log.e(TAG, "updateFoodList:totalDiscount PERCENTAGE_OFFER " + singleFoodDiscount);
                    }

                    Log.e(TAG, "updateFoodList:totalDiscount " + totalDiscount);
                }
            }

            List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
            if (cartDetailsDbList.size() > 0) {
                restaurantId = cartDetailsDbList.get(0).getRestaurant_id();
                Log.e(TAG, "updateFoodList:cartDetailsDbList " + cartDetailsDbList.size());
                Log.e(TAG, "updateFoodList:cartDetailsDbList " + cartDetailsDbList.get(0).getTotalAmount());
                totalTax = Double.parseDouble(decimalFormat.format(totalTax));
                totalAmount = Double.parseDouble(cartDetailsDbList.get(0).getTotalAmount());
                itemTotalAmountTxt.setText(currencyStr + " " + cartDetailsDbList.get(0).getTotalAmount());
                gstAmountTxt.setText(currencyStr + " " + totalTax);

                if (cartDetailsDbList.get(0).getTarget_amount() != null &&
                        Double.parseDouble(cartDetailsDbList.get(0).getTotalAmount()) >= Double.parseDouble(cartDetailsDbList.get(0).getTarget_amount())) {

                    if (cartDetailsDbList.get(0).getOffer_amount() != null &&
                            Double.parseDouble(cartDetailsDbList.get(0).getOffer_amount()) > 0) {
                        Log.e(TAG, "updateFoodList:totalDiscount Restaurant " + cartDetailsDbList.get(0).getOffer_amount());
                        if (cartDetailsDbList.get(0).getDiscount_type().equals(FLAT_OFFER)) {
                            totalDiscount += (Double.parseDouble(cartDetailsDbList.get(0).getOffer_amount()));
                            Log.e(TAG, "updateFoodList:totalDiscount Restaurant FLAT_OFFER " + cartDetailsDbList.get(0).getOffer_amount());
                        } else if (cartDetailsDbList.get(0).getDiscount_type().equals(PERCENTAGE_OFFER)) {
                            double singleFoodDiscount = Double.parseDouble(cartDetailsDbList.get(0).getTotalAmount()) * (Double.parseDouble(cartDetailsDbList.get(0).getOffer_amount()) / 100);
//                            totalDiscount += (Double.parseDouble(cartDetailsDbList.get(0).getTotalCount()) * singleFoodDiscount);
                            totalDiscount += singleFoodDiscount;
                            Log.e(TAG, "updateFoodList:totalDiscount Restaurant PERCENTAGE_OFFER " + singleFoodDiscount);
                        }
                    }

                }

                itemTotalDiscountTxt.setText("- " + currencyStr + " " + totalDiscount);
                billAmount += (Double.parseDouble(cartDetailsDbList.get(0).getTotalAmount()) + totalTax - totalDiscount);
                Log.e(TAG, "updateFoodList:billAmount " + billAmount);

                getWalletBalance();

                if (checkoutPojo == null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.coupon_code, applyCodeEdt.getText().toString().trim());
                    map.put(CONST.Params.restaurant_id, cartDetailsDbList.get(0).getRestaurant_id());
                    jsonCartDetails(map);
                } else {
                    CheckoutPojo.Invoice invoice = checkoutPojo.getInvoice().get(0);
                    packagingCharge = invoice.getRestaurantPackagingCharge();
                    deliveryCharge = invoice.getDeliveryCharge();

                    setDeliveryCharge();
                   /* billAmount = (invoice.getOfferDiscount() + invoice.getRestaurantPackagingCharge() + invoice.getDeliveryCharge());
                    String finalBillAmount = decimalFormat.format(billAmount);
                    totalToPayAmountTxt.setText(currencyStr + " " + finalBillAmount);*/

                   /* double tempBillAmount = billAmount + invoice.getRestaurantPackagingCharge() + invoice.getDeliveryCharge() - invoice.getOfferDiscount();
                    if (tempBillAmount <= 0) {
                        billAmount = 0.0;
                    } else {
                        billAmount = tempBillAmount;
                    }
                    String finalBillAmount = decimalFormat.format(billAmount);
                    totalToPayAmountTxt.setText(currencyStr + " " + finalBillAmount);*/
                }

            }
            Log.e(TAG, "updateFoodList:gstAmountTxt " + totalTax);
        } else {
            //clear cart data
            App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
            App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
            App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
            App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
            App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
            App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
            finish();
        }
    }

    private void getWalletBalance() {
        Call<WalletBalanceResponse> call = apiInterface.getwalletBalance(sessionManager.getHeader());
        call.enqueue(new Callback<WalletBalanceResponse>() {
            @Override
            public void onResponse(Call<WalletBalanceResponse> call, Response<WalletBalanceResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        if (response.body().getWallet_balance() != null) {
                            walletAmount = Double.parseDouble(response.body().getWallet_balance());
                            txtWallet.setVisibility(View.VISIBLE);
                            txtWallet.setText(sessionManager.getCurrency() + " " + response.body().getWallet_balance() + " wallet credits");
                        } else {
                            txtWallet.setVisibility(View.GONE);
                            txtWallet.setText("0.00");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletBalanceResponse> call, Throwable t) {
                CommonFunctions.shortToast(ViewCartActivity.this, "Service Failed");
            }
        });

    }

    private ValueEventListener seListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null && map.get(CONST.Params.current_address) != null) {
                    Log.e(TAG, "onDataChange:map.get(CONST.Params.current_address) != null ");
                    Log.e(TAG, "onDataChange:map.get(CONST.Params.current_address) != null " + (String) map.get(CONST.Params.current_address));

                    cartAddressTxt.setText((String) map.get(CONST.Params.current_address));
                    isFromAlert = false;
                    checkDeliveryAvailability(map, false);
                } else {
                    if (sessionManager.isLoggedIn()) {
                        isFromAlert = false;
                        locationUpdate.locationPermissionCheck(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        return valueEventListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseReference != null) {
            mDatabaseReference.removeEventListener(valueEventListener);
        }
    }

    public void jsonCartDetails(HashMap<String, String> map) {
        if (!isFromAlert) {
            CommonFunctions.showSimpleProgressDialog(this, "Updating", false);
        }
        Call<CheckoutPojo> call = apiInterface.checkout(sessionManager.getHeader(), map, BuildConfig.VERSION_CODE);
        call.enqueue(new Callback<CheckoutPojo>() {
            @Override
            public void onResponse(Call<CheckoutPojo> call, Response<CheckoutPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        checkoutPojo = response.body();
                        CheckoutPojo.RestaurantDetail restaurantDetail = response.body().getRestaurantDetail().get(0);
                        Log.e(TAG, "onResponse:Max_dining_count " + restaurantDetail.getMax_dining_count());
                        restaurantdataSingle = new NearRestarentPojo.Restaurants();
                        restaurantdataSingle.setId(restaurantDetail.getRestaurantId());
                        restaurantdataSingle.setWeekday_closing_time(restaurantDetail.getWeekday_closing_time());
                        restaurantdataSingle.setWeekday_opening_time(restaurantDetail.getWeekday_opening_time());
                        restaurantdataSingle.setWeekend_closing_time(restaurantDetail.getWeekend_closing_time());
                        restaurantdataSingle.setWeekend_opening_time(restaurantDetail.getWeekend_opening_time());
                        restaurantdataSingle.setMax_dining_count(Integer.parseInt(restaurantDetail.getMax_dining_count()));
                        restaurantdataSingle.setRestaurant_timing(restaurantDetail.getRestaurant_timing());

                        deliveryType.clear();
                        deliveryType.addAll(restaurantDetail.getDeliveryType());
                        Log.e(TAG, "onResponse:deliveryType size " + deliveryType.size());
                        setDeliveryTypeVisibility();

                        cartHotelDeliveryTxt.setText(restaurantDetail.getEstimatedDeliveryTime().contains("mins") ? restaurantDetail.getEstimatedDeliveryTime() : restaurantDetail.getEstimatedDeliveryTime() + " Mins");


                        CheckoutPojo.Invoice invoice = response.body().getInvoice().get(0);
                        Log.e(TAG, "onResponse:getOfferDiscount " + invoice.getOfferDiscount());
                        offerDiscountAmountTxt.setText(currencyStr + " " + invoice.getOfferDiscount());

                        packagingCharge = invoice.getRestaurantPackagingCharge();
                        deliveryCharge = invoice.getDeliveryCharge();

                        setDeliveryCharge();

                       /* packagingChargeAmountTxt.setText(currencyStr + " " + invoice.getRestaurantPackagingCharge());

                        deliveryChargeAmountTxt.setText(currencyStr + " " + invoice.getDeliveryCharge());

                        billAmount += (invoice.getRestaurantPackagingCharge() + invoice.getDeliveryCharge() - invoice.getOfferDiscount());
                        String finalBillAmount = decimalFormat.format(billAmount);
                        totalToPayAmountTxt.setText(currencyStr + " " + finalBillAmount);*/

                       /* itemTotalAmountTxt.setText(currencyStr + " " + invoice.getItemTotal());
                        totalToPayAmountTxt.setText(currencyStr + " " + invoice.getBillAmount());*/

                    } else if (response.body().getMessage().equalsIgnoreCase("No Items in your cart")) {
                        Intent intent = new Intent(ViewCartActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {

                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                       /* applyCodeEdt.setText("");
                        HashMap<String,String> map = new HashMap<>();
                        map.put(CONST.Params.coupon_code,applyCodeEdt.getText().toString().trim());
                        jsonCartDetails(map);*/

                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }
                CommonFunctions.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<CheckoutPojo> call, Throwable t) {
                CommonFunctions.removeProgressDialog();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                //do whatever
//                startActivity(new Intent(ViewCartActivity.this, HotelDetailActivity.class))
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.cart_address_change_txt, R.id.cart_cupon_code_aply_txt, R.id.place_order_txt, R.id.lay_coup_cart, R.id.apply_code_edt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cart_address_change_txt:
                alertAddress();
                break;
            case R.id.apply_code_edt:
                offerDialog = new OfferDialog(this, this, decimalFormat.format(finalBillAmount), selectedDeliveryType);
                break;
            case R.id.cart_cupon_code_aply_txt:
                if (cartCuponCodeAplyTxt.getText().toString().equalsIgnoreCase("Apply")) {
                    /* if (!applyCodeEdt.getText().toString().isEmpty()) {
                     *//* HashMap<String, String> map = new HashMap<>();
                        map.put(CONST.Params.bill_amount, decimalFormat.format(billAmount));
                        map.put(CONST.Params.promocode, applyCodeEdt.getText().toString().trim());
                        jsonCheckOffers(map);*//*
                    } else {
                        CommonFunctions.shortToast(ViewCartActivity.this, "Enter Valid Coupon Code");
                    }*/
                    offerDialog = new OfferDialog(this, this, decimalFormat.format(finalBillAmount), selectedDeliveryType);
                } else {

                    removeAppliedOffer();
                }
                break;
            case R.id.place_order_txt:
                if (placeOrderTxt.getText().toString().equals(getString(R.string.place_order))) {
                    if (radioSelectValidation()) {
                        if (walletReducedBalance > 0) {
                            if (selectedDeliveryType.equals(DINING)) {
                                diningPayment(selectedDeliveryType, selectedMembers, selectedTime);
                            } else {
                                if (applyCodeEdt.getText().toString().trim().isEmpty()) {
                                    redirectToPayment();
                                } else {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(CONST.Params.bill_amount, decimalFormat.format(finalBillAmount + checkoutPojo.getInvoice().get(0).getOfferDiscount()));
                                    map.put(CONST.Params.promocode, applyCodeEdt.getText().toString().trim());
                                    map.put(CONST.Params.delivery_type, selectedDeliveryType);
                                    jsonCheckOffers(map);
                                }
                            }
                        } else {
                            //call payment service
                            callPaymentapi();
                        }
                    } else {
                        CommonFunctions.shortToast(activity, "Select a delivery Type");
                    }
                } else if (placeOrderTxt.getText().toString().equals(getString(R.string.login))) {
                    loginBottomFragment = new LoginBottomFragment(this, this::onDismissFragment);
                    loginBottomFragment.showNow(getSupportFragmentManager(), "loginFragment");
                }
                break;
        }
    }


    private void jsonCheckOffers(HashMap<String, String> map) {
        Call<CheckOfferResponse> call = apiInterface.checkOffers(sessionManager.getHeader(), map);
        call.enqueue(new Callback<CheckOfferResponse>() {
            @Override
            public void onResponse(Call<CheckOfferResponse> call, Response<CheckOfferResponse> response) {

                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        redirectToPayment();
                    } else {
                        CommonFunctions.shortToast(activity, response.body().getMessage());
                        removeAppliedOffer();
                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CheckOfferResponse> call, Throwable t) {

                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void removeAppliedOffer() {
        checkoutPojo.getInvoice().get(0).setOfferDiscount(Double.parseDouble("0"));
        offerDiscountAmountTxt.setText(currencyStr + " " + checkoutPojo.getInvoice().get(0).getOfferDiscount());
        cartCuponCodeAplyTxt.setText("Apply");
        cartCuponCodeAplyTxt.setTextColor(getResources().getColor(R.color.colorAccent));
        applyCodeEdt.setText("");
        updateFoodList();
    }

    private void addAppliedOffer(String offerAmount, String couponCode) {
        checkoutPojo.getInvoice().get(0).setOfferDiscount(Double.parseDouble(offerAmount));
        offerDiscountAmountTxt.setText(currencyStr + " " + checkoutPojo.getInvoice().get(0).getOfferDiscount());
        cartCuponCodeAplyTxt.setText("Remove");
        cartCuponCodeAplyTxt.setTextColor(getResources().getColor(R.color.red));
        applyCodeEdt.setText(couponCode);
        updateFoodList();
    }


    /*private void jsonCheckOffers(HashMap<String, String> map) {
        Call<CheckOfferResponse> call = apiInterface.checkOffers(sessionManager.getHeader(), map,sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<CheckOfferResponse>() {
            @Override
            public void onResponse(Call<CheckOfferResponse> call, Response<CheckOfferResponse> response) {

                if (response.code() == 200) {

                    if (response.body().isStatus()) {
                        addAppliedOffer(response.body().getOffer_amount());
                    } else {
                        CommonFunctions.shortToast(activity, response.body().getMessage());
                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CheckOfferResponse> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }*/


    public void alertAddress() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        final View view = getLayoutInflater().inflate(R.layout.alert_delivery_address, null);
        // Set above view in alert dialog.
        builder.setView(view);

        alert_address_linear = view.findViewById(R.id.alert_address_linear);
        ImageView home_back_img = view.findViewById(R.id.home_back_img);
        ImageView address_search_img = view.findViewById(R.id.address_search_img);
        home_address_edt = view.findViewById(R.id.home_address_edt);
        TextView home_current_loc_txt = view.findViewById(R.id.home_current_loc_txt);
        TextView add_address_txt = view.findViewById(R.id.add_address_txt);
        no_saved_address_txt = view.findViewById(R.id.no_saved_address_txt);
        home_address_rv = view.findViewById(R.id.home_address_rv);
        google_place_search_rv = view.findViewById(R.id.google_place_search_rv);

        add_address_txt.setVisibility(View.GONE);

        MyLayoutManager = new LinearLayoutManager(activity);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_address_rv.setLayoutManager(MyLayoutManager);


        MyLayoutManager = new LinearLayoutManager(activity);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        google_place_search_rv.setLayoutManager(MyLayoutManager);
        googlePlacesAdapter = new GooglePlacesAdapter(ViewCartActivity.this, "ViewCartActivity", places_type_list, this::cancelAlert);
        google_place_search_rv.setAdapter(googlePlacesAdapter);

        home_address_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 3) {
                    placeJson(String.valueOf(s).trim(), view);
                    alert_address_linear.setVisibility(View.GONE);
                    google_place_search_rv.setVisibility(View.VISIBLE);
                } else {
                    alert_address_linear.setVisibility(View.VISIBLE);
                    google_place_search_rv.setVisibility(View.GONE);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        address_search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (home_address_edt.getText().toString().length() > 3) {
                    placeJson(String.valueOf(home_address_edt.getText().toString()).trim(), view);
                    alert_address_linear.setVisibility(View.GONE);
                    google_place_search_rv.setVisibility(View.VISIBLE);
                }
            }
        });

        jsonGetSavedAddress();//To save users delivery address

        no_saved_address_txt.setVisibility(View.VISIBLE);

        home_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        home_current_loc_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromAlert = true;
                locationUpdate.locationPermissionCheck(true);
            }
        });

        add_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                activity.startActivity(intent);
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void jsonGetSavedAddress() {

        Call<SavedAddressPojo> call = apiInterface.getSavedAddress(sessionManager.getHeader());
        call.enqueue(new Callback<SavedAddressPojo>() {
            @Override
            public void onResponse(Call<SavedAddressPojo> call, Response<SavedAddressPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        addressList.clear();
                        home_address_rv.setVisibility(View.VISIBLE);
                        no_saved_address_txt.setVisibility(View.GONE);
                        addressList.addAll(response.body().getData());
                        savedAddressAdapter = new SavedAddressAdapter(activity, "ViewCartActivity", addressList, ViewCartActivity.this::cancelAlert, ViewCartActivity.this::deleteAlert);
                        home_address_rv.setAdapter(savedAddressAdapter);

                    } else
                        CommonFunctions.shortToast(activity, response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<SavedAddressPojo> call, Throwable t) {

            }
        });

    }


    public void placeJson(String input, View view) {

        places_type_list.clear();

        Call<PlacesPojo> call = apiService.doPlaces(APIKEY, input);
        call.enqueue(new Callback<PlacesPojo>() {

            @Override
            public void onResponse(Call<PlacesPojo> call, Response<PlacesPojo> response) {
                PlacesPojo root = response.body();

                if (response.code() == 200) {

                    places_type_list.addAll(root.getPredictions());
                    alert_address_linear.setVisibility(View.GONE);
                    google_place_search_rv.setVisibility(View.VISIBLE);
                    googlePlacesAdapter.notifyDataSetChanged();

                } else if (response.code() != 200) {
                    Toast.makeText(activity, "Error " + response.code() + "found.", Toast.LENGTH_SHORT).show();
                    alert_address_linear.setVisibility(View.VISIBLE);
                    google_place_search_rv.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<PlacesPojo> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(activity, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Retro Error" + t.getMessage().toString());
                call.cancel();
            }
        });

    }

    @Override
    public void locationChanged(Location location) {
        //Place current location marker
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void displayGPSSettingsDialog() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
        //Refreshing the activity with new selected Video
        Intent intent = new Intent(ViewCartActivity.this, ViewCartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onUpdateCart() {
        updateFoodList();
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        checkLoginStatus();
    }

    @Override
    public void cancelAlert(HashMap<String, Object> map) {
        isFromAlert = true;
        checkDeliveryAvailability(map, true);
    }

    private void checkDeliveryAvailability(HashMap<String, Object> map, boolean isFromAlert) {
        if (isFromAlert) {
            CommonFunctions.showSimpleProgressDialog(this, "Updating", false);
        }
        String latitude = String.valueOf((map.get(CONST.CURRENT_LATITUDE)));
        String longitude = String.valueOf((map.get(CONST.CURRENT_LONGITUDE)));
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", latitude);
        locationMap.put("lng", longitude);
        Call<CheckAddressResponse> call = apiInterface.checkAddress(sessionManager.getHeader(), locationMap, restaurantId, BuildConfig.VERSION_CODE);
        call.enqueue(new Callback<CheckAddressResponse>() {
            @Override
            public void onResponse(Call<CheckAddressResponse> call, Response<CheckAddressResponse> response) {
                if (response.code() == 200) {
                    //address is from alert
                    if (isFromAlert) {
                        if (response.body().isStatus()) {
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
                            mDatabaseReference.setValue(map);
                            if (alertDialog != null) {
                                alertDialog.cancel();
                            }
                        } else {
                            Toast.makeText(ViewCartActivity.this, "Not Deliverable to this address", Toast.LENGTH_LONG).show();
                        }
                        checkoutPojo = null;
                        updateFoodList();
                    }

                    //address is fom activity
                    else {
                        if (response.body().isStatus()) {
                            setPlaceOrderVisibility(true);
                        } else {
                            setPlaceOrderVisibility(false);
                        }

                    }

                }
                CommonFunctions.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<CheckAddressResponse> call, Throwable t) {
                CommonFunctions.removeProgressDialog();
                CommonFunctions.shortToast(ViewCartActivity.this, "Service Failed");
            }
        });
    }

    private void setPlaceOrderVisibility(boolean isDeliverable) {
        if (isDeliverable) {
            placeOrderTxt.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            placeOrderTxt.setEnabled(true);
            txtNotDeliverable.setVisibility(View.GONE);
        } else {
            placeOrderTxt.setBackgroundColor(getResources().getColor(R.color.grey));
            placeOrderTxt.setEnabled(false);
            txtNotDeliverable.setVisibility(View.VISIBLE);

        }
    }

    private void redirectToPayment() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setBill_amount(finalBillAmount);
        paymentRequest.setWallet_amount(walletReducedBalance);
        paymentRequest.setCoupon_code(applyCodeEdt.getText().toString().isEmpty() ? "NA" : applyCodeEdt.getText().toString());
        paymentRequest.setDelivery_charge(finalDeliveryCharge);
        paymentRequest.setGst(totalTax);
        paymentRequest.setRestaurant_discount(totalDiscount);
        paymentRequest.setDelivery_type(selectedDeliveryType);
        paymentRequest.setTotal_members(selectedMembers);
        paymentRequest.setPickup_dining_time(selectedTime);
        paymentRequest.setItem_total(App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0).getTotalAmount());
        paymentRequest.setOffer_discount(checkoutPojo.getInvoice().get(0).getOfferDiscount());
        paymentRequest.setRestaurant_id(App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0).getRestaurant_id());
        paymentRequest.setRestaurant_packaging_charge(finalPackagingCharge);
        paymentRequest.setIs_wallet(txtWallet.isChecked() ? "1" : "0");
        Intent intent = new Intent(ViewCartActivity.this, PaymentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_REQUEST, paymentRequest);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void setDeliveryTypeVisibility() {
        resetValues();
        for (String data : deliveryType) {
            switch (data) {
                case DOOR_DELIVERY:
                    //set default selection as Door Delivery
                    radDoor.setVisibility(View.VISIBLE);
                    radDoor.setChecked(true);
                    setDoorDeliveryValues();
                    break;
                case PICKUP_RESTAURANT:
                    radPickup.setVisibility(View.VISIBLE);
                    radPickup.setChecked(false);
                    break;
                case DINING:
                    radDine.setVisibility(View.VISIBLE);
                    radDine.setChecked(false);
                    break;
            }
        }
    }

    private void resetValues() {
        selectedDeliveryType = "";
        selectedTime = "";
        selectedMembers = "";
    }

    private boolean radioSelectValidation() {
        if (radDine.isChecked()) {
            return true;
        } else if (radDoor.isChecked()) {
            return true;
        } else if (radPickup.isChecked()) {
            return true;
        }
        return false;
    }


    @Override
    public void OnDeliveryTypeConfirm(String deliveryType, String members, String time) {
        selectedDeliveryType = deliveryType;
        selectedTime = time;
        selectedMembers = members;
        setDeliveryCharge();
        diningDialog.dismissDialog();
    }

    private void setDeliveryCharge() {
        if (selectedDeliveryType.equals(DINING)) {
            layCoupCart.setVisibility(View.GONE);
            //dining service need to be called by hiding cart item,bills and Offers
            finalPackagingCharge = 0.0;
            finalDeliveryCharge = 0.0;
            packagingChargeAmountTxt.setText(currencyStr + " " + 0.0);
            deliveryChargeAmountTxt.setText(currencyStr + " " + 0.0);
            layPackaging.setVisibility(View.GONE);
            layDelivery.setVisibility(View.GONE);
            double tempBillAmount = billAmount - checkoutPojo.getInvoice().get(0).getOfferDiscount();
            if (tempBillAmount <= 0) {
                finalBillAmount = 0.0;
            } else {
                finalBillAmount = tempBillAmount;
            }
            //edited nov 8
            /*String BillAmountTxt = decimalFormat.format(tempBillAmount);
            totalToPayAmountTxt.setText(currencyStr + " " + BillAmountTxt);*/
            setWalletFinalBill(txtWallet.isChecked());

        } else if (selectedDeliveryType.equals(PICKUP_RESTAURANT)) {
            layCoupCart.setVisibility(View.VISIBLE);
            finalPackagingCharge = packagingCharge;
            finalDeliveryCharge = 0.0;
            packagingChargeAmountTxt.setText(currencyStr + " " + packagingCharge);
            deliveryChargeAmountTxt.setText(currencyStr + " " + 0.0);
            layPackaging.setVisibility(View.VISIBLE);
            layDelivery.setVisibility(View.GONE);
            double tempBillAmount = billAmount + packagingCharge - checkoutPojo.getInvoice().get(0).getOfferDiscount();
            if (tempBillAmount <= 0) {
                finalBillAmount = 0.0;
            } else {
                finalBillAmount = tempBillAmount;
            }
            //edited nov 8
            /*String BillAmountTxt = decimalFormat.format(tempBillAmount);
            totalToPayAmountTxt.setText(currencyStr + " " + BillAmountTxt);*/
            setWalletFinalBill(txtWallet.isChecked());

        } /*else if (selectedDeliveryType.equals(DOOR_DELIVERY)) {
            layCoupCart.setVisibility(View.VISIBLE);
            layPackaging.setVisibility(View.VISIBLE);
            layDelivery.setVisibility(View.VISIBLE);
            packagingChargeAmountTxt.setText(currencyStr + " " + packagingCharge);
            deliveryChargeAmountTxt.setText(currencyStr + " " + deliveryCharge);
            double tempBillAmount = billAmount + packagingCharge + deliveryCharge - checkoutPojo.getInvoice().get(0).getOfferDiscount();
            if (tempBillAmount <= 0) {
                finalBillAmount = 0.0;
            } else {
                finalBillAmount = tempBillAmount;
            }
            String BillAmountTxt = decimalFormat.format(tempBillAmount);
            totalToPayAmountTxt.setText(currencyStr + " " + BillAmountTxt);
        }*/ else {
            layCoupCart.setVisibility(View.VISIBLE);
            layPackaging.setVisibility(View.VISIBLE);
            layDelivery.setVisibility(View.VISIBLE);
            finalPackagingCharge = packagingCharge;
            finalDeliveryCharge = deliveryCharge;
            packagingChargeAmountTxt.setText(currencyStr + " " + packagingCharge);
            deliveryChargeAmountTxt.setText(currencyStr + " " + deliveryCharge);
            double tempBillAmount = billAmount + packagingCharge + deliveryCharge - checkoutPojo.getInvoice().get(0).getOfferDiscount();
            if (tempBillAmount <= 0) {
                finalBillAmount = 0.0;
            } else {
                finalBillAmount = tempBillAmount;
            }
            //edited nov 8
            /*String BillAmountTxt = decimalFormat.format(tempBillAmount);
            totalToPayAmountTxt.setText(currencyStr + " " + BillAmountTxt);*/
            setWalletFinalBill(txtWallet.isChecked());
        }
    }

    @Override
    public void OnDeliveryTypeCancel() {
        if (selectedDeliveryType.isEmpty() || selectedDeliveryType.equals(DOOR_DELIVERY)) {
            setDeliveryTypeVisibility();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        if (radioButton.isPressed()) {
            Log.e(TAG, "onCheckedChanged: checkedId" + checkedId);
            switch (checkedId) {
                case R.id.rad_door:
                    resetValues();
                    setDoorDeliveryValues();
                    break;
                case R.id.rad_pickup:
                    resetValues();
                    diningDialog = new DiningDialog(ViewCartActivity.this, restaurantdataSingle, deliveryType, PICKUP_RESTAURANT, this);
                    break;

                case R.id.rad_dine:
                    resetValues();
                    diningDialog = new DiningDialog(ViewCartActivity.this, restaurantdataSingle, deliveryType, DINING, this);
                    break;
            }
        } else {
            Log.e(TAG, "onCheckedChanged: not pressed" + checkedId);
        }
    }

    private void setDoorDeliveryValues() {
        selectedDeliveryType = DOOR_DELIVERY;
        setDeliveryCharge();
    }

    private void diningPayment(String deliveryType, String members, String time) {

        Call<SuccessPojo> call = apiInterface.finalGoPayment(sessionManager.getHeader(), "" + restaurantdataSingle.getId(), deliveryType, time, members);
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
                        CommonFunctions.shortToast(activity, "Dining Request Sent to Restaurant");
                        Intent i = new Intent(ViewCartActivity.this, HomeActivity.class);
                        startActivity(i);
                        finishAffinity();
                    } else
                        CommonFunctions.shortToast(activity, response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(activity, "Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
            }
        });
    }

    @Override
    public void onApplyClick(String offerAmount, String CouponCode) {
        addAppliedOffer(offerAmount, CouponCode);
    }

    @Override
    public void deleteAlert(int position) {
        Call<SuccessPojo> call = apiInterface.deleteAddress(addressList.get(position).getId());
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {
                    if (home_address_rv.getAdapter() != null) {
                        addressList.remove(position);
                        home_address_rv.getAdapter().notifyDataSetChanged();
                        CommonFunctions.shortToast(activity, response.body().getMessage());
                    }
                } else if (response.code() != 404) {
                    CommonFunctions.shortToast(activity, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                CommonFunctions.shortToast(activity, t.getMessage());
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.txt_wallet:
                setWalletFinalBill(isChecked);
                break;
        }

    }

    private void setWalletFinalBill(boolean isChecked) {
        if (isChecked) {
            if (finalBillAmount > walletAmount) {
                walletReducedBalance = finalBillAmount - walletAmount;
            } else {
                walletReducedBalance = 0;
            }
        } else {
            walletReducedBalance = finalBillAmount;
        }
        String BillAmountTxt = decimalFormat.format(walletReducedBalance);
        totalToPayAmountTxt.setText(currencyStr + " " + BillAmountTxt);
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
                            startActivity(new Intent(ViewCartActivity.this, AddCardActivity.class));
                        }
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

            }
        });

    }

    private void callPaymentapi() {
        paymentMap.put(CONST.Params.restaurant_id, App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0).getRestaurant_id());
        paymentMap.put(CONST.Params.coupon_code, applyCodeEdt.getText().toString().isEmpty() ? "NA" : applyCodeEdt.getText().toString());
        paymentMap.put(CONST.Params.item_total, App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0).getTotalAmount());
        paymentMap.put(CONST.Params.offer_discount, String.valueOf(checkoutPojo.getInvoice().get(0).getOfferDiscount()));
        paymentMap.put(CONST.Params.restaurant_packaging_charge, String.valueOf(finalPackagingCharge));
        paymentMap.put(CONST.Params.gst, String.valueOf(totalTax));
        paymentMap.put(CONST.Params.delivery_charge, String.valueOf(finalDeliveryCharge));
        paymentMap.put(CONST.Params.bill_amount, String.valueOf(finalBillAmount));
        paymentMap.put(CONST.Params.delivery_type, String.valueOf(selectedDeliveryType));
        paymentMap.put(CONST.Params.total_members, selectedMembers);
        paymentMap.put(CONST.Params.pickup_dining_time, selectedTime);
        paymentMap.put(CONST.Params.restaurant_discount, String.valueOf(totalDiscount));
        paymentMap.put(CONST.Params.is_wallet, txtWallet.isChecked() ? "1" : "0");

        foodIdList.clear();
        foodQtyList.clear();
        foodQuantityIdList.clear();
        foodQuantityPriceList.clear();
        addOnsIdList.clear();

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

        jsonPayment("1");

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
                Intent i = new Intent(ViewCartActivity.this, HistoryActivity.class);
                startActivity(i);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                alertDialog.cancel();
                Intent i = new Intent(ViewCartActivity.this, HomeActivity.class);
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
}
