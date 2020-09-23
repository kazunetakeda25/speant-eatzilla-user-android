package com.speant.user.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.FragmentCommunicator;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.locationCheck.GPSChangeNotifyEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Models.OnLocationFetch;
import com.speant.user.Models.OnRequestIdFetch;
import com.speant.user.Models.UrlResponse;
import com.speant.user.ui.fragment.CreditFragment;
import com.speant.user.ui.fragment.GoOutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.fragment.CartFragment;
import com.speant.user.ui.fragment.HomeFragment;
import com.speant.user.ui.fragment.ProfileFragment;
import com.speant.user.ui.fragment.SearchFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.LOG_TYPE;
import static com.speant.user.Common.CONST.Params.request_id;
import static com.speant.user.Common.CONST.REGISTER;
import static com.speant.user.Common.SessionManager.KEY_USER_ID;

public class HomeActivity extends CurrentLocation {

    private static final String TAG = "HomeActivity";
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    int index = 0;
    double lat, lng;
    @BindView(R.id.progress)
    ProgressBar progress;

    View notificationBadge;
    AppCompatTextView notificationCount;
    //Social Logins in android
    private FirebaseAuth mAuth;

    APIInterface apiInterface;
    SessionManager sessionManager;
    Fragment selectedFragment;
    //GEOFIRE
    DatabaseReference mAddressDatabaseReference;//User update delivery address in firebase
    DatabaseReference mDatabaseOrderRequest;//User update user request in firebase
    DatabaseReference mDatabaseCurrentRequest;//User update user status in firebase
    private LatLng latLng;
    private Handler handler;
    private String currentAddress;
    HashMap<String, Object> intentHashMap;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;
    private HashMap<String, String> userDetails;
    private LocationUpdate locationUpdate;
    public FragmentCommunicator homeFragmentCommunicator;
    private WebSocketClient mWebSocketClient;
    private String logType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        locationUpdate = new LocationUpdate(this);
        handler = new Handler();
        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mAuth = FirebaseAuth.getInstance();
        setCreditDialog();
        setupNavigationView();
        getUrls();
        // INFO TODO implement Chat
        connectWebSocket();
    }

    private void setCreditDialog() {
        logType = getIntent().getStringExtra(LOG_TYPE);
        if (logType != null) {
            if (!logType.isEmpty() && logType.equalsIgnoreCase(REGISTER)) {
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.credit_credit_dialog))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("HomeActivity", "setCreditDialog Ok Click");
                                selectFragment(R.id.action_credit);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("HomeActivity", "setCreditDialog Cancel Click");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    // INFO TODO implement Chat
    private void connectWebSocket() {


        Log.e(TAG, "connectWebSocket:is true ");

        String url = Global.SOCKET_URL;

        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connectWebSocket: " + e.toString());
            return;
        }


        mWebSocketClient = new

                WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.i("Websocket", "Opened");


                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("type", "init");
                            jsonObject.put("id", "User_" + sessionManager.getUserDetails().get(KEY_USER_ID));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mWebSocketClient.send(String.valueOf(jsonObject));

                        sessionManager.putSocketUinqueId("User_" + sessionManager.getUserDetails().get(KEY_USER_ID));

                    }

                    @Override
                    public void onMessage(String s) {

                        Log.e(TAG, "message: " + s);

                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.i("Websocket", "Closed " + s);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Websocket", "Error " + e.getMessage());
                    }
                };


        mWebSocketClient.connect();


    }

    private void getUrls() {
        Call<UrlResponse> call = apiInterface.getSupportUrl(sessionManager.getHeader());
        call.enqueue(new Callback<UrlResponse>() {
            @Override
            public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                sessionManager.saveUrls(response.body().getDetails());
            }

            @Override
            public void onFailure(Call<UrlResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume:HomeActivity ");
        if (sessionManager.isLoggedIn()) {
            userDetails = sessionManager.getUserDetails();
            setFireBaseListener();
        }
        //this is done to update cart count if the screen is resumed after other activity finish
        if (bottomNavigationView != null) {
            setNotificationBadgeVisibility(bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()));
        }


        //check if GPS and Location Permission is Enabled
        Log.e(TAG, "onResume: HomeActivity" + isDialogOpen);
        if (!isDialogOpen) {
            Log.e(TAG, "onResume:locationPermissionCheck ");
            locationUpdate.locationPermissionCheck(false);
        }
    }


    private void removeFireBaseListener() {
        if (valueEventListener != null) {
            mDatabaseOrderRequest.removeEventListener(valueEventListener);
        }
        if (childEventListener != null) {
            mDatabaseCurrentRequest.removeEventListener(childEventListener);
        }
    }

    private void setFireBaseListener() {
        // Attach a listener to read the data at our posts reference
        Log.e(TAG, "setFireBaseListener: " + FirebaseDatabase.getInstance().getReference());
        mDatabaseOrderRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.new_user_request).child(userDetails.get(KEY_USER_ID));
        mDatabaseOrderRequest.addValueEventListener(setListener());
    }

    private ValueEventListener setListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    Log.e(TAG, "onDataChange: Order " + map);
                    mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().
                            child(CONST.Params.current_request).child(String.valueOf(map.get(CONST.Params.request_id)));
                    mDatabaseCurrentRequest.addChildEventListener(setChildListener());
                    mDatabaseCurrentRequest.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.e(TAG, "onDataChange: map " + map);
                            if (map != null) {
                                Log.e(TAG, "onDataChange: " + String.valueOf(map.get(request_id)) + "\t\t" + String.valueOf(map.get(CONST.Params.status)));
                                CONST.status_value_str = String.valueOf(map.get(CONST.Params.status));
                                Log.e(TAG, "onDataChange:Status " + CONST.status_value_str);
//                                homeFragmentCommunicator.passRequestIdToFragment(String.valueOf(map.get(request_id)), String.valueOf(map.get(CONST.Params.status)));
//                                homeFragmentCommunicator.passRequestIdToFragment(new OnRequestIdFetch(String.valueOf(map.get(request_id)), String.valueOf(map.get(CONST.Params.status))));
                                EventBus.getDefault().postSticky(new OnRequestIdFetch(String.valueOf(map.get(request_id)), String.valueOf(map.get(CONST.Params.status))));

                            } else {
                                CONST.status_value_str = CONST.NO_ORDER;
//                                homeFragmentCommunicator.passRequestIdToFragment(CONST.ORDER_UNAVAIL, CONST.NO_ORDER);
//                                homeFragmentCommunicator.passRequestIdToFragment(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
                                EventBus.getDefault().postSticky(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                } else {
                    CONST.status_value_str = CONST.NO_ORDER;
//                    homeFragmentCommunicator.passRequestIdToFragment(CONST.ORDER_UNAVAIL, CONST.NO_ORDER);
//                    homeFragmentCommunicator.passRequestIdToFragment(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
                    EventBus.getDefault().postSticky(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        return valueEventListener;
    }

    private ChildEventListener setChildListener() {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("Giri ", "onChildAdded: passRequestIdToFragment");
//                homeFragmentCommunicator.passRequestIdToFragment(CONST.ORDER_UNAVAIL, CONST.ORDER_CREATED);
//                homeFragmentCommunicator.passRequestIdToFragment(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.ORDER_CREATED));
                EventBus.getDefault().postSticky(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.ORDER_CREATED));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                homeFragmentCommunicator.passRequestIdToFragment(CONST.ORDER_UNAVAIL, CONST.NO_ORDER);
//                homeFragmentCommunicator.passRequestIdToFragment(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
                EventBus.getDefault().postSticky(new OnRequestIdFetch(CONST.ORDER_UNAVAIL, CONST.NO_ORDER));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        return childEventListener;
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intentHashMap != null) {
                intentHashMap.clear();
            }
            intentHashMap = (HashMap<String, Object>) intent.getSerializableExtra(CONST.HASHMAP_VALUES);
            if (intentHashMap != null && !intentHashMap.isEmpty()) {
                Log.e("Giri ", "run:getIntentValues intentHashMap " + intentHashMap);
                lat = Double.parseDouble(intentHashMap.get(CONST.CURRENT_LATITUDE).toString());
                lng = Double.parseDouble(intentHashMap.get(CONST.CURRENT_LONGITUDE).toString());
                currentAddress = intentHashMap.get(CONST.CURRENT_ADDRESS).toString();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceived(ProviderLocation providerLocation) {
        if (!isEventStarted) {
            isEventStarted = true;
            isDialogOpen = false;
            Log.e("TAG", "onLocationReceived:mLastLocation " + providerLocation.getLng());
            location = providerLocation.getLat() + "/" + providerLocation.getLng();
            getCurrentLocation();
            isEventStarted = false;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSChangeNotify(GPSChangeNotifyEvent gpsChangeNotifyEvent) {
        Log.e(TAG, "onGPSNotify:EventCalled");
        Log.e(TAG, "onGPSNotify:isDialogOpen " + isDialogOpen);
        if (!isDialogOpen) {
            Log.e(TAG, "onGPSNotify:EventCalled !isDialogOpen");
            if (sessionManager.isLoggedIn()) {
                locationUpdate.locationPermissionCheck(false);
            } else {
                locationUpdate.locationPermissionCheck(true);
            }
        }
    }

    public void getCurrentLocation() {

        //check if value is sent from some other frgament or activity
        getIntentValues();

        //GetCurrentLocation from AgentCurrentLocation Activity's String named as "location"

        if (intentHashMap != null && !intentHashMap.isEmpty()) {
            Log.e("Giri ", "run:getCurrentLocation intentHashMap " + intentHashMap);
//                homeFragmentCommunicator.passAddressListToFragment(intentHashMap);
//                homeFragmentCommunicator.passAddressToFragment(new OnLocationFetch(intentHashMap));
            EventBus.getDefault().postSticky(new OnLocationFetch(intentHashMap));
        } else {
            if (location != null) {
                String[] currentLocation = location.split("/");
                lat = Double.parseDouble(currentLocation[0]);
                lng = Double.parseDouble(currentLocation[1]);
                latLng = new LatLng(lat, lng);
                Log.e("Giri ", "run:getCurrentLocation " + latLng);
                currentAddress = Global.getAddress(HomeActivity.this, latLng);
                String city = Global.getCity(HomeActivity.this, latLng);

                HashMap<String, Object> map = new HashMap<>();
                map.put(CONST.CURRENT_ADDRESS, currentAddress);
                map.put(CONST.CURRENT_CITY, city);
                map.put(CONST.CURRENT_LATITUDE, lat);
                map.put(CONST.CURRENT_LONGITUDE, lng);
//                    homeFragmentCommunicator.passAddressListToFragment(map);
//                    homeFragmentCommunicator.passAddressToFragment(new OnLocationFetch(map));
                EventBus.getDefault().postSticky(new OnLocationFetch(map));
                if (sessionManager.isLoggedIn()) {
                    userDetails = sessionManager.getUserDetails();
                    mAddressDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(userDetails.get(KEY_USER_ID));
                    mAddressDatabaseReference.setValue(map);
                }

                Log.e("Giri ", "run:getCurrentLocation map" + map);

            }
        }
    }

    public void progressShow() {
        progress.setVisibility(View.VISIBLE);
    }

    public void progressHide() {
        progress.setVisibility(View.GONE);
    }

    private void setupNavigationView() {
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();

            //adding badge count to cart icon
            View v = bottomNavigationView.findViewById(R.id.action_cart);
            BottomNavigationItemView itemView = (BottomNavigationItemView) v;
            notificationBadge = LayoutInflater.from(this)
                    .inflate(R.layout.notify_badge, itemView, true);
            notificationCount = notificationBadge.findViewById(R.id.notifications_count);

            setNotificationBadgeVisibility(menu.getItem(index));
            selectFragment(menu.getItem(index).getItemId());


            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            item.setChecked(true);
                            selectedFragment = null;
                            setNotificationBadgeVisibility(item);
                            selectFragment(item.getItemId());
                            return false;
                        }
                    });
        }


    }


    private void setNotificationBadgeVisibility(MenuItem item) {
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        //DISPLAY THE NOTIFICATION BADGE on other screen except CART SCREEN
        if (item.getItemId() == R.id.action_cart) {
            Log.e(TAG, "onNavigationItemSelected:action_cart ");
            notificationCount.setVisibility(View.GONE);
        } else {
            if (cartDetailsDbList.size() > 0) {
                Log.e(TAG, "onNavigationItemSelected:other_views ");
                notificationCount.setVisibility(View.VISIBLE);
                notificationCount.setText(cartDetailsDbList.get(0).getTotalCount());
            } else {
                notificationCount.setVisibility(View.GONE);
            }
        }
    }


    protected void selectFragment(int itemId) {
        Log.e("Giri ", "selectFragment:itemId " + itemId);
        switch (itemId) {
            case R.id.action_home:
                selectedFragment = HomeFragment.newInstance();
                break;
            case R.id.action_go:
                selectedFragment = GoOutFragment.newInstance();
                break;
            /*case R.id.action_search:
                selectedFragment = SearchFragment.newInstance();
                break;*/
            case R.id.action_credit:
                selectedFragment = CreditFragment.newInstance();
                break;
            case R.id.action_cart:
                selectedFragment = CartFragment.newInstance();
                break;
            case R.id.action_profile:
                selectedFragment = ProfileFragment.newInstance();
                break;

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();

    }


    public void logout() {
        if (mAuth != null) {
            mAuth.getInstance().signOut();
        } else {

        }
        jsonLogout("logout");

    }

    public void jsonLogout(String url) {

        Call<SuccessPojo> call = apiInterface.logout(url, sessionManager.getHeader());
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        sessionManager.logoutUser();

                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFireBaseListener();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
       /* //Refreshing the activity with new selected Video
        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
        startActivity(intent);
        HomeActivity.this.finish();*/
        selectFragment(bottomNavigationView.getSelectedItemId());
    }


    public void setIsDialogOpen(boolean isDialogOpen) {
        this.isDialogOpen = isDialogOpen;
    }
}
