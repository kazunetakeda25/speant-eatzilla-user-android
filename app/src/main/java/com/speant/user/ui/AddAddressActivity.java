package com.speant.user.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.locationCheck.GPSChangeNotifyEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.customViews.CustomMapFragment;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.SessionManager.KEY_USER_ID;

public class AddAddressActivity extends CurrentLocation implements OnMapReadyCallback {

    /*@BindView(R.id.map_view)
    MapView mapView;*/
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar_lay)
    AppBarLayout appbarLay;
    @BindView(R.id.signup_welcon_txt)
    TextView signupWelconTxt;
    @BindView(R.id.addr_location_edt)
    AppCompatEditText addrLocationEdt;
    @BindView(R.id.addr_flat_no_edt)
    EditText addrFlatNoEdt;
    @BindView(R.id.addr_landmark_edt)
    EditText addrLandmarkEdt;
    @BindView(R.id.address_type_home_txt)
    TextView addressTypeHomeTxt;
    @BindView(R.id.address_type_work_txt)
    TextView addressTypeWorkTxt;
    @BindView(R.id.address_type_other_txt)
    TextView addressTypeOtherTxt;
    @BindView(R.id.save_addr_txt)
    TextView saveAddrTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    String selectedPlaceStr = "";
    private GoogleMap mGoogleMap;
    private static final String TAG = "AddAddressActivity";
    String latitude = "", longitude = "", finalAddressStr = "";
    SessionManager sessionManager;
    APIInterface apiInterface;
    private String currentAddress;
    private String currentCity;
    private LatLng currentlatLng;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3321;
    private Place place;
    private DatabaseReference mDatabaseReference;
    private LocationUpdate locationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
        Places.initialize(getApplicationContext(), CONST.APIKEY);
        PlacesClient placesClient = Places.createClient(this);
        setMap();
        setToolBarScroll();
        actionBar();
        locationUpdate = new LocationUpdate(this);
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
            locationUpdate.locationPermissionCheck(true);
        }
    }


    private void setToolBarScroll() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarLay.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        new AppBarLayout.Behavior().setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);
    }

    private void setMap() {
        //initiate the map
        SupportMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void getCurrentLocation() {
        //GetCurrentLocation from AgentCurrentLocation Activity's String named as "location"
        if (location != null && mGoogleMap != null) {
            String[] currentLocation = location.split("/");
            double lat = Double.parseDouble(currentLocation[0]);
            double lng = Double.parseDouble(currentLocation[1]);
            latitude = String.valueOf(lat);
            longitude = String.valueOf(lng);
            currentlatLng = new LatLng(lat, lng);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
            Log.e("Giri ", "run:getCurrentLocation " + currentlatLng);
            currentAddress = Global.getAddress(AddAddressActivity.this, currentlatLng);
            currentCity = Global.getCity(AddAddressActivity.this, currentlatLng);
            addrLocationEdt.setText(currentAddress);
            setIdleListener();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new CustomMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });
        locationUpdate.locationPermissionCheck(true);
    }

    private void setIdleListener() {
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                LatLng midLatLng = mGoogleMap.getCameraPosition().target;
                Log.e(TAG, "onCameraIdle: " + midLatLng);
                latitude = String.valueOf(midLatLng.latitude);
                longitude = String.valueOf(midLatLng.longitude);
                currentlatLng = new LatLng(midLatLng.latitude, midLatLng.longitude);
                currentAddress = Global.getAddress(AddAddressActivity.this, currentlatLng);
                currentCity = Global.getCity(AddAddressActivity.this, currentlatLng);
                addrLocationEdt.setText(currentAddress);
            }
        });
    }

    public void actionBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.lit_grey));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        collapsingToolbar.setContentScrimColor(Color.TRANSPARENT);
        collapsingToolbar.setStatusBarScrimColor(Color.TRANSPARENT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.addr_location_edt, R.id.address_type_home_txt, R.id.address_type_work_txt, R.id.address_type_other_txt, R.id.save_addr_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_type_home_txt:
                addressTypeHomeTxt.setText(Html.fromHtml("<b>Home</b>"));
//                addressTypeHomeTxt.setBackground(getResources().getDrawable(R.drawable.underline));
                addressTypeHomeTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                addressTypeWorkTxt.setText("Work");
                addressTypeOtherTxt.setText("Other");
                selectedPlaceStr = "1";
                addressTypeWorkTxt.setTextColor(getResources().getColor(R.color.grey));
                addressTypeOtherTxt.setTextColor(getResources().getColor(R.color.grey));
                break;

            case R.id.address_type_work_txt:
                addressTypeWorkTxt.setText(Html.fromHtml("<b>Work</b>"));
//                addressTypeWorkTxt.setBackground(getResources().getDrawable(R.drawable.underline));
                addressTypeWorkTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                addressTypeHomeTxt.setText("Home");
                addressTypeOtherTxt.setText("Other");
                selectedPlaceStr = "2";
                addressTypeHomeTxt.setTextColor(getResources().getColor(R.color.grey));
                addressTypeOtherTxt.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.address_type_other_txt:
                addressTypeOtherTxt.setText(Html.fromHtml("<b>Other</b>"));
//                addressTypeOtherTxt.setBackground(getResources().getDrawable(R.drawable.underline));
                addressTypeOtherTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                addressTypeWorkTxt.setText("Work");
                addressTypeHomeTxt.setText("Home");
                selectedPlaceStr = "3";
                addressTypeWorkTxt.setTextColor(getResources().getColor(R.color.grey));
                addressTypeHomeTxt.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.save_addr_txt:

                if (addrLocationEdt.getText().toString().isEmpty()) {
                    Toast.makeText(AddAddressActivity.this, "Set Address", Toast.LENGTH_LONG).show();
                } else if (addrFlatNoEdt.getText().toString().isEmpty()) {
                    Toast.makeText(AddAddressActivity.this, "Set Flat Number", Toast.LENGTH_LONG).show();
                } else if (addrLandmarkEdt.getText().toString().isEmpty()) {
                    Toast.makeText(AddAddressActivity.this, "Set Land Mark", Toast.LENGTH_LONG).show();
                } else if (selectedPlaceStr.isEmpty()) {
                    Toast.makeText(AddAddressActivity.this, "Select Save As Type", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "onViewClicked: ");
                    if (latitude.length() > 3 && longitude.length() > 3) {
//                if (latitude.length() > 3 && longitude.length() > 3 && finalAddressStr.length() > 3) {

                        HashMap<String, String> map = new HashMap<>();
                        map.put(CONST.Params.lat, latitude);
                        map.put(CONST.Params.lng, longitude);
                        map.put(CONST.Params.type, selectedPlaceStr);
                        map.put(CONST.Params.address, addrLocationEdt.getText().toString());
                        map.put(CONST.Params.flat_no, addrFlatNoEdt.getText().toString());
                        map.put(CONST.Params.landmark, addrLandmarkEdt.getText().toString());
                        jsonSetAddress(map);

                    }
                }


                break;
            case R.id.addr_location_edt:
                openAutoComplete();
                break;
        }
    }

    private void openAutoComplete() {
        try {
            /*AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
                    .build(AddAddressActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            Log.e(TAG, "openAutoComplete:Exception " + e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = Autocomplete.getPlaceFromIntent(data);
                currentAddress = place.getAddress();
                currentlatLng = place.getLatLng();
                currentCity = Global.getCity(AddAddressActivity.this, currentlatLng);
                addrLocationEdt.setText(currentAddress);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatLng, 15.0f));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(AddAddressActivity.this, data);
                Log.e("TAG", "onActivityResult:Status " + status.getStatusMessage());
                toast(AddAddressActivity.this, "Google Error :" + status);
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("TAG", "onActivityResult:Status RESULT_CANCELED ");
            }
        }
    }

    public void jsonSetAddress(HashMap<String, String> map) {
        Call<SuccessPojo> call = apiInterface.addAddress(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(CONST.CURRENT_ADDRESS, currentAddress);
                        map.put(CONST.CURRENT_LATITUDE, currentlatLng.latitude);
                        map.put(CONST.CURRENT_LONGITUDE, currentlatLng.longitude);
                        map.put(CONST.CURRENT_CITY, currentCity);
                        mDatabaseReference.setValue(map);
                        Intent intent = new Intent(AddAddressActivity.this, HomeActivity.class);
                        intent.putExtra(CONST.HASHMAP_VALUES, (Serializable) map);
                        startActivity(intent);
                        finishAffinity();
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                Log.e("Nive ", "onFailure: " + t.toString());
            }
        });

    }
}
