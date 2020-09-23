package com.speant.user.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.FireBaseModels.CurrentRequestFirebase;
import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.Global;
import com.speant.user.Common.RouteDecode;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.DirectionResults;
import com.speant.user.Models.TrackingDetailPojo;
import com.speant.user.R;
import com.speant.user.ui.adapter.TrackingStatusAdapter;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class TrackingActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "TrackingActivity";
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar_lay)
    AppBarLayout appbarLay;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.td_food_prepared_txt)
    TextView tdFoodPreparedTxt;
    @BindView(R.id.td_time_rv)
    RecyclerView tdTimeRv;
    @BindView(R.id.td_prof_img)
    ImageView tdProfImg;
    @BindView(R.id.td_verify_img)
    ImageView tdVerifyImg;
    @BindView(R.id.td_track_desc_txt)
    TextView tdTrackDescTxt;
    @BindView(R.id.td_item_total_amount_txt)
    TextView tdItemTotalAmountTxt;
    @BindView(R.id.td_offer_discount_amount_txt)
    TextView tdOfferDiscountAmountTxt;
    @BindView(R.id.packaging_charge_amount_txt)
    TextView packagingChargeAmountTxt;
    @BindView(R.id.td_gst_amount_txt)
    TextView tdGstAmountTxt;
    @BindView(R.id.delivery_charge_amount_txt)
    TextView deliveryChargeAmountTxt;
    @BindView(R.id.total_to_pay_amount_txt)
    TextView totalToPayAmountTxt;
    @BindView(R.id.td_cancel_order_txt)
    TextView tdCancelOrderTxt;
    @BindView(R.id.td_track_time_txt)
    TextView tdTrackTimeTxt;
    @BindView(R.id.td_track_time_session_txt)
    TextView tdTrackTimeSessionTxt;
    @BindView(R.id.lay_driv_detail)
    LinearLayout layDrivDetail;
    @BindView(R.id.txt_direction)
    TextView txtDirection;
    @BindView(R.id.item_total_discount_txt)
    TextView itemTotalDiscountTxt;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_chat_admin)
    TextView tvChatAdmin;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private APIInterface apiService;
    private GoogleMap mGoogleMap;

    //      Array
    ArrayList<Integer> durationList = new ArrayList<Integer>();
    ArrayList<LatLng> routelist = new ArrayList<LatLng>();
    List<DirectionResults.Route.Steps> steps = new ArrayList<>();
    ArrayList<LatLng> decodelist = new ArrayList<>();

    Marker marker;
    MarkerOptions markerOptions;
    Polyline polyline = null;
    private int progressStatus = 45;
    private ImageView back_arrow_toolbar;
    private TextView track_toolbar_title, time_txt, item_txt, amt_txt, track_help_txt, tv_chat;
    String deliveryBoyPhone = "";

    //GEOFIRE
    DatabaseReference mDatabaseOrderRequest;//User update user request in firebase
    DatabaseReference mDatabaseCurrentRequest;//User update user status in firebase

    //Map Tracking
    LatLng deliverBoyLatLng;
    List<TrackingDetailPojo.TrackingDetail> trackList = new ArrayList<>();
    private LinearLayoutManager MyLayoutManager;
    private TrackingStatusAdapter trackingStatusAdapter;
    private LatLng restarentLatLng;
    private LatLng userLatLng;
    private String req_id_loc_str = "";
    String deliveryBoyImage, deliveryBoyName;
    int track_count = 10;
    boolean pauseBol;
    private boolean isFirst = true;

    private boolean isMarkerRotating = false;
    String requestId, providerId;
    String currentStatus;
    private DatabaseReference mDatabaseProvLocation;

    Handler handler = new Handler();
    public Runnable runnable;
    Marker bikeMarker;
    private int status;
    private String deliveryType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);
        getIntentValues();
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        progressBar.setProgress(progressStatus);

        actionBar();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();

        HashMap<String, String> userDetatils = sessionManager.getUserDetails();

        mDatabaseOrderRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_request).child(requestId);

        MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        tdTimeRv.setLayoutManager(MyLayoutManager);
        tdTimeRv.setNestedScrollingEnabled(false);
        Log.e("Nive ", "trackList: " + trackList);
        trackingStatusAdapter = new TrackingStatusAdapter(this, trackList);
        tdTimeRv.setAdapter(trackingStatusAdapter);
        tdTimeRv.setVisibility(View.GONE);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarLay.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);
    }

    private void getIntentValues() {
        if (getIntent() != null) {
            requestId = getIntent().getStringExtra(CONST.REQUEST_ID);
            deliveryType = getIntent().getStringExtra(CONST.DELIVERY_TYPE);
            Log.e("Giri ", "getIntentValues:requestId" + requestId);
        }

        if (deliveryType.equals(PICKUP_RESTAURANT)) {
            tdFoodPreparedTxt.setVisibility(View.GONE);
            layDrivDetail.setVisibility(View.GONE);
            tdTimeRv.setVisibility(View.GONE);
            txtDirection.setVisibility(View.VISIBLE);
        }
    }


    public void setText(String status) {
        tdFoodPreparedTxt.setText(status);
    }

    private void setProviderLocFirebaseListner() {
        if (mDatabaseProvLocation == null) {
            mDatabaseProvLocation = FirebaseDatabase.getInstance().getReference().child(CONST.Params.prov_location).child(providerId);
            mDatabaseProvLocation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ProviderLocation providerLocation = dataSnapshot.getValue(ProviderLocation.class);
                    if (providerLocation != null) {
                        if (bikeMarker != null) {
                            bikeMarker.remove();
                        }
                        if (status > 2 && status < 7) {

                            deliverBoyLatLng = new LatLng(Double.parseDouble(providerLocation.getLat()), Double.parseDouble(providerLocation.getLng()));
                            if (mGoogleMap != null) {
                                bikeMarker = mGoogleMap.addMarker(new MarkerOptions().position(deliverBoyLatLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_bike)));
                                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(deliverBoyLatLng, 14.0f));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed:mDatabaseProvLocation " + databaseError.getCode());
                }
            });
        }
    }

    private void setOrderFirebaseListeners() {

        // Attach a listener to read the data at our posts reference
        mDatabaseOrderRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentRequestFirebase currentRequestFirebase = dataSnapshot.getValue(CurrentRequestFirebase.class);
                if (currentRequestFirebase != null) {
                    currentStatus = "" + currentRequestFirebase.getStatus();
                    Log.e("Giri ", "onDataChange:currentStatus " + currentStatus);
                    status = Integer.parseInt(currentStatus);

                    if (status > 2 && status < 7) {
                        providerId = currentRequestFirebase.getProvider_id();
                        setProviderLocFirebaseListner();
                    }

                    HashMap<String, String> trackMap = new HashMap<>();
                    trackMap.put(CONST.Params.request_id, requestId);
                    jsonGetTrackingStatus(trackMap);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed:mDatabaseOrderRequest " + databaseError.getCode());
            }
        });
    }

    @OnClick({R.id.td_food_prepared_txt, R.id.td_cancel_order_txt, R.id.txt_direction, R.id.tv_chat, R.id.tv_chat_admin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.td_food_prepared_txt:
                if (tdTimeRv.getVisibility() == View.VISIBLE) {
                    tdTimeRv.setVisibility(View.GONE);
                    tdFoodPreparedTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right, 0);
                } else {
                    tdTimeRv.setVisibility(View.VISIBLE);
                    tdFoodPreparedTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                }
                break;
            case R.id.td_cancel_order_txt:
                break;
            case R.id.tv_chat_admin:
                // INFO TODO implement Chat
                if (requestId != null) {
                    Intent intent = new Intent(TrackingActivity.this, InboxActivity.class);
                    intent.putExtra(Global.FROM_TYPE, "Admin");
                    intent.putExtra(Global.DELIVERYBOY_ID, providerId);
                    intent.putExtra(Global.ORDER_ID, requestId);
                    startActivity(intent);
                }


                break;
            case R.id.tv_chat:
                // INFO TODO implement Chat
                Log.e(TAG, "onClick:deliverBoyId " + providerId);
                Log.e(TAG, "onClick:orderId " + requestId);


                if (providerId != null && requestId != null) {
                    Intent intent = new Intent(TrackingActivity.this, InboxActivity.class);
                    intent.putExtra(Global.FROM_TYPE, "DeliveryBoy");
                    intent.putExtra(Global.DELIVERYBOY_ID, providerId);
                    intent.putExtra(Global.ORDER_ID, requestId);
                    startActivity(intent);
                }

                break;
            case R.id.txt_direction:
                try {
                    Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + restarentLatLng.latitude + "," + restarentLatLng.longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    CommonFunctions.shortToast(TrackingActivity.this, e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (!deliveryType.equals(PICKUP_RESTAURANT)) {
            setOrderFirebaseListeners();
        } else {
            Log.e("Giri ", "onMapReady:requestId " + requestId);
            if (requestId != null) {
                HashMap<String, String> trackMap = new HashMap<>();
                trackMap.put(CONST.Params.request_id, requestId);
                jsonGetTrackingStatus(trackMap);
            }
        }
    }

    public void deliveryTracking(String status) {
        if (status != null) {
            switch (status) {

                case CONST.DELIVERY_REQUEST_ACCEPTED:
                    if (deliverBoyLatLng != null && restarentLatLng != null) {
                        float bearing = (float) bearingBetweenLocations(deliverBoyLatLng, restarentLatLng);
                        directionJson(deliverBoyLatLng, restarentLatLng);
                        setMarker(bearing, deliverBoyLatLng, restarentLatLng, null);
                        progressBar.setProgress(25);
                    }
                    break;
                case CONST.REACHED_RESTAURANT:
                    if (deliverBoyLatLng != null && restarentLatLng != null) {
                        float bearing = (float) bearingBetweenLocations(deliverBoyLatLng, restarentLatLng);
                        directionJson(deliverBoyLatLng, restarentLatLng);
                        setMarker(bearing, deliverBoyLatLng, restarentLatLng, null);
                        progressBar.setProgress(50);
                    }

                    break;

                case CONST.FOOD_COLLECTED_ONWAY:
                    if (deliverBoyLatLng != null && userLatLng != null) {
                        float bearing = (float) bearingBetweenLocations(deliverBoyLatLng, userLatLng);
                        directionJson(deliverBoyLatLng, userLatLng);
                        setMarker(bearing, deliverBoyLatLng, null, userLatLng);
                        progressBar.setProgress(75);
                        tdCancelOrderTxt.setVisibility(View.GONE);
                    }
                    break;
                case CONST.FOOD_DELIVERED:
                    if (restarentLatLng != null && userLatLng != null) {
                        progressBar.setProgress(95);
                        float bearing = (float) bearingBetweenLocations(restarentLatLng, userLatLng);
                        directionJson(restarentLatLng, userLatLng);
                        setMarker(bearing, null, restarentLatLng, userLatLng);
                        tdCancelOrderTxt.setVisibility(View.GONE);
                    }
                    break;
                case CONST.ORDER_COMPLETE:
                    if (restarentLatLng != null && userLatLng != null) {
                        progressBar.setProgress(100);
                        float bearing = (float) bearingBetweenLocations(restarentLatLng, userLatLng);
                        directionJson(restarentLatLng, userLatLng);
                        setMarker(bearing, null, restarentLatLng, userLatLng);
                        tdCancelOrderTxt.setVisibility(View.GONE);
                    }
                    break;

                default:
                    if (restarentLatLng != null && userLatLng != null) {
                        progressBar.setProgress(10);
                        float bearing = (float) bearingBetweenLocations(restarentLatLng, userLatLng);
                        directionJson(restarentLatLng, userLatLng);
                        setMarker(bearing, null, restarentLatLng, userLatLng);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12.0f));
                    }
                    break;

            }
        }

    }

    private void setMarker(float bearing, LatLng trackLatLng, LatLng restLatLng, LatLng dropLatLng) {

        if (bikeMarker != null) {
            bikeMarker.remove();
        }
        if (trackLatLng != null) {
            bikeMarker = mGoogleMap.addMarker(new MarkerOptions().position(trackLatLng)
                    .rotation(bearing)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_bike)));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trackLatLng, 14.0f));
        }

        if (restLatLng != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(this.restarentLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_hotel)));
        }

        if (dropLatLng != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(dropLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_user)));
        }
    }

    public void actionBar() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_tracking_toolbar);
            View view = getSupportActionBar().getCustomView();

            back_arrow_toolbar = (ImageView) view.findViewById(R.id.back_arrow_toolbar);
            track_toolbar_title = (TextView) view.findViewById(R.id.track_toolbar_title);
            time_txt = (TextView) view.findViewById(R.id.time_txt);
            item_txt = (TextView) view.findViewById(R.id.item_txt);
            amt_txt = (TextView) view.findViewById(R.id.amt_txt);
            track_help_txt = (TextView) view.findViewById(R.id.track_help_txt);


            back_arrow_toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            track_help_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrackingActivity.this, HelpActivity.class));
                }
            });

        }
        collapsingToolbar.setContentScrimColor(Color.TRANSPARENT);
        collapsingToolbar.setStatusBarScrimColor(Color.TRANSPARENT);


    }

    public void jsonGetTrackingStatus(HashMap<String, String> trackMap) {
        CommonFunctions.showSimpleProgressDialog(TrackingActivity.this, "Fetching Details.Please Wait !", false);
        Call<TrackingDetailPojo> call = apiInterface.trackingDetail(sessionManager.getHeader(), trackMap);
        call.enqueue(new Callback<TrackingDetailPojo>() {
            @Override
            public void onResponse(Call<TrackingDetailPojo> call, Response<TrackingDetailPojo> response) {

                if (response.code() == 200) {

                    Log.e("Nive ", "onResponse:Track " + response.body());

                    if (response.body().getStatus()) {

                        TrackingDetailPojo.OrderStatus pojo = response.body().getOrderStatus().get(0);

                        trackList.clear();
                        trackList.addAll(response.body().getTrackingDetail());
                        trackingStatusAdapter.notifyDataChanged();

                        track_toolbar_title.setText(getString(R.string.order_hash) + pojo.getOrderId());

                        item_txt.setText("" + pojo.getItemCount() + "\tItem");
                        amt_txt.setText(sessionManager.getCurrency() + "\t" + pojo.getBillAmount());

                        deliveryBoyPhone = pojo.getDeliveryBoyPhone();

                        tdItemTotalAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getItemTotal());
                        packagingChargeAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getRestaurantPackagingCharge());
                        deliveryChargeAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getDeliveryCharge());
                        tdOfferDiscountAmountTxt.setText(sessionManager.getCurrency() + "-" + pojo.getOfferDiscount());
                        tdGstAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getTax());
                        totalToPayAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getBillAmount());
                        itemTotalDiscountTxt.setText(sessionManager.getCurrency() + "-" + pojo.getRestaurant_discount());
                        System.out.println("the value is:" + sessionManager.getCurrency() + "-" + pojo.getRestaurant_discount());

                        DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        DateFormat timeFormat = new SimpleDateFormat("hh:mm ", Locale.getDefault());
                        DateFormat sessionFormat = new SimpleDateFormat("a", Locale.getDefault());
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                        restarentLatLng = new LatLng(pojo.getRestaurantLat(), pojo.getRestaurantLng());
                        userLatLng = new LatLng(pojo.getUserLat(), pojo.getUserLng());

                       /* Glide.with(getApplicationContext())
                                .load(pojo.getDeliveryBoyImage())
                                .into(tdProfImg);*/
                        deliveryBoyImage = pojo.getDeliveryBoyImage();
                        deliveryBoyName = pojo.getDeliveryBoyName();
                        setDeliveryStatus("" + pojo.getStatus());

                        Date date = null;
                        try {
                            String inputText = " ";
                            if (deliveryType.equals(PICKUP_RESTAURANT)) {
                                inputText = pojo.getPickup_dining_time();
                            } else if (deliveryType.equals(DOOR_DELIVERY)) {
                                inputText = pojo.getOrderedTime();
                            }
                            date = inputFormat.parse(inputText);
                            time_txt.setText(outputFormat.format(date));
                            tdTrackTimeTxt.setText(timeFormat.format(date));
                            tdTrackTimeSessionTxt.setText(sessionFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (currentStatus == null) {
                            currentStatus = "" + pojo.getStatus();
                        }

                        deliveryTracking(currentStatus);

                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }
                CommonFunctions.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<TrackingDetailPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                CommonFunctions.removeProgressDialog();
            }
        });

    }

    private void setDeliveryStatus(String status) {
        switch (status) {
            case CONST.ORDER_CREATED:
                setDeliveryVisibility(false, "");
                break;
            case CONST.RESTAURANT_ACCEPTED:
                setDeliveryVisibility(false, "");
                break;
            case CONST.FOOD_PREPARED:
                setDeliveryVisibility(false, "");
                break;
            case CONST.DELIVERY_REQUEST_ACCEPTED:
                setDeliveryVisibility(true, getString(R.string.accepted_order_and_on_the_way));
                break;
            case CONST.REACHED_RESTAURANT:
                setDeliveryVisibility(true, getString(R.string.reached_restaurant));
                break;
            case CONST.FOOD_COLLECTED_ONWAY:
                setDeliveryVisibility(true, getString(R.string.collected_order));
                break;
            case CONST.FOOD_DELIVERED:
                setDeliveryVisibility(true, getString(R.string.delivered_order));
                break;
            case CONST.ORDER_COMPLETE:
                setDeliveryVisibility(true, getString(R.string.delivered_order));
                break;
            case CONST.ORDER_CANCELLED:
                setDeliveryVisibility(false, "");
                break;
            default:
                setDeliveryVisibility(false, "");
                break;

        }

    }

    private void setDeliveryVisibility(boolean visibility, String desc) {
        if (visibility) {
            layDrivDetail.setVisibility(View.VISIBLE);
            tdTrackDescTxt.setText(deliveryBoyName + desc);
            if (deliveryBoyImage != null && !deliveryBoyImage.isEmpty()) {
                Picasso
                        .get()
                        .load(deliveryBoyImage)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(tdProfImg);
            }

        } else {
            layDrivDetail.setVisibility(View.GONE);
        }


    }

    public void directionJson(final LatLng fromLatLng, final LatLng toLatLng) {

        Call<DirectionResults> call = apiService.polyLines(CONST.APIKEY, fromLatLng.latitude + "," + fromLatLng.longitude,
                toLatLng.latitude + "," + toLatLng.longitude);
        call.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                DirectionResults directionResults = response.body();

                if (response.code() == 200) {

                    //mMap.clear();
                    routelist.clear();
                    steps.clear();
                    decodelist.clear();
                    Log.i("zacharia", "inside on success" + directionResults.getRoutes().size());

                    if (directionResults.getRoutes().size() > 0) {

                        DirectionResults.Route routeA = directionResults.getRoutes().get(0);
                        Log.i("zacharia", "Legs length : " + routeA.getLegs().size());
                        if (routeA.getLegs().size() > 0) {

                            Log.i("Legs", "Distance: " + routeA.getLegs().get(0).getDistance().getText());
                            Log.i("Legs", "Duration: " + routeA.getLegs().get(0).getDuration().getText());

                            //For getting Distance and Duration
                            String distance_str = routeA.getLegs().get(0).getDistance().getText();
                            String duration_str = routeA.getLegs().get(0).getDuration().getText();

                            steps = routeA.getLegs().get(0).getSteps();
                            Log.i("zacharia", "Steps size :" + steps.size());
                            DirectionResults.Route.Steps step;
                            DirectionResults.Route.Steps.Location location;
                            String polyline;
                            for (int i = 0; i < steps.size(); i++) {
                                step = steps.get(i);
                                location = step.getStart_location();
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                                Log.i("zacharia", "Start Location :" + location.getLat() + ", " + location.getLng());
                                polyline = step.getPolyline().getPoints();
                                decodelist = RouteDecode.decodePoly(polyline);
                                routelist.addAll(decodelist);
                                location = step.getEnd_location();
                                routelist.add(new LatLng(location.getLat(), location.getLng()));
                                Log.i("zacharia", "End Location :" + location.getLat() + ", " + location.getLng());
                            }
                        }
                    }
                    Log.i("zacharia", "routelist size : " + routelist.size());
                    if (routelist.size() > 0) {
                        ValueAnimator tAnimator = ValueAnimator.ofFloat(0, 1);
                        tAnimator.setRepeatCount(ValueAnimator.INFINITE);
                        tAnimator.setRepeatMode(ValueAnimator.RESTART);
                        tAnimator.setInterpolator(new LinearInterpolator());
                        tAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                // animate here
                            }
                        });
                        PolylineOptions rectLine = new PolylineOptions().width(5)
                                .color(getResources().getColor(R.color.colorPrimary)).startCap(new SquareCap())
                                .endCap(new SquareCap()).jointType(ROUND);

                        for (int i = 0; i < routelist.size(); i++) {
                            rectLine.add(routelist.get(i));
                        }

                        if (polyline != null)
                            polyline.remove();

                        // Adding route on the map
                        markerOptions = new MarkerOptions();
                        polyline = mGoogleMap.addPolyline(rectLine);
                    }

                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 15.0f));
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Retro Error" + t.getMessage().toString());
                call.cancel();
            }
        });


    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 2000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    float bearing = -rot > 180 ? rot / 2 : rot;

                    marker.setRotation(bearing);

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
        //Refreshing the activity with new selected Video
        Intent intent = new Intent(TrackingActivity.this, TrackingActivity.class);
        intent.putExtra(CONST.REQUEST_ID, requestId);
        intent.putExtra(CONST.DELIVERY_TYPE, deliveryType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
