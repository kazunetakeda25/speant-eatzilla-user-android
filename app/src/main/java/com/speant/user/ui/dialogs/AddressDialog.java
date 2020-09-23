package com.speant.user.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.CancelAlert;
import com.speant.user.Common.callBacks.DeleteAlert;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.PlacesPojo;
import com.speant.user.Models.SavedAddressPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.AddAddressActivity;
import com.speant.user.ui.adapter.GooglePlacesAdapter;
import com.speant.user.ui.adapter.SavedAddressAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.APIKEY;
import static com.speant.user.Common.SessionManager.KEY_USER_ID;

public class AddressDialog implements CancelAlert, DeleteAlert {

    private final APIInterface apiService;
    private final APIInterface apiInterface;
    private final LocationUpdate locationUpdate;
    Activity activity;
    public AlertDialog alertDialog;
    private ArrayList<PlacesPojo.Predictions> places_type_list = new ArrayList<>();
    private LinearLayout alert_address_linear;
    private RecyclerView google_place_search_rv;
    private RecyclerView home_address_rv;
    private TextView no_saved_address_txt;
    private LinearLayoutManager MyLayoutManager;
    private GooglePlacesAdapter googlePlacesAdapter;
    SessionManager sessionManager;
    ArrayList<SavedAddressPojo.Data> addressList = new ArrayList();
    private SavedAddressAdapter savedAddressAdapter;
    private DatabaseReference mDatabaseReference;

    public AddressDialog(Activity activity) {
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        locationUpdate = new LocationUpdate(activity);
        this.activity = activity;
        init();
    }

    public void init() {
        Log.e("Giri ", "alertAddress: ");
        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogSlideAnim_filter);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // Get custom login form view.
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_delivery_address, null);
        // Set above view in alert dialog.
        builder.setView(dialogView);

        alert_address_linear = dialogView.findViewById(R.id.alert_address_linear);
        ImageView home_back_img = dialogView.findViewById(R.id.home_back_img);
        ImageView address_search_img = dialogView.findViewById(R.id.address_search_img);
        final EditText home_address_edt = dialogView.findViewById(R.id.home_address_edt);
        TextView home_current_loc_txt = dialogView.findViewById(R.id.home_current_loc_txt);
        TextView add_address_txt = dialogView.findViewById(R.id.add_address_txt);
        no_saved_address_txt = dialogView.findViewById(R.id.no_saved_address_txt);
        home_address_rv = dialogView.findViewById(R.id.home_address_rv);
        google_place_search_rv = dialogView.findViewById(R.id.google_place_search_rv);

        MyLayoutManager = new LinearLayoutManager(activity);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_address_rv.setLayoutManager(MyLayoutManager);


        MyLayoutManager = new LinearLayoutManager(activity);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        google_place_search_rv.setLayoutManager(MyLayoutManager);
        googlePlacesAdapter = new GooglePlacesAdapter(activity, "HomeFragment", places_type_list, AddressDialog.this);
        google_place_search_rv.setAdapter(googlePlacesAdapter);

        home_address_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() > 3) {
                    placeJson(String.valueOf(s).trim(), dialogView);
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
                    placeJson(String.valueOf(home_address_edt.getText().toString()).trim(), dialogView);
                    alert_address_linear.setVisibility(View.GONE);
                    google_place_search_rv.setVisibility(View.VISIBLE);
                }
            }
        });

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
                Log.e("Giri ", "onClick: home_current_loc_txt isGPSEnabled ");
               /* if (CommonFunctions.isGPSEnabled(activity)) {
                    ((HomeActivity) activity).getCurrentLocation();
                    alertDialog.cancel();
                }*/
                locationUpdate.locationPermissionCheck(true);
                alertDialog.cancel();
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
    }

    public void showAddressDialog() {
        jsonGetSavedAddress();//To save users delivery address
        alertDialog.show();
    }

    public void hideAddressDialog() {
        alertDialog.cancel();
    }


    public void placeJson(String input, View view) {
        Log.e("Giri ", "placeJson: ");
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

    private void jsonGetSavedAddress() {
        Log.e("Giri ", "jsonGetSavedAddress: ");
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
                        savedAddressAdapter = new SavedAddressAdapter(activity, "HomeFragment", addressList, AddressDialog.this,AddressDialog.this);
                        home_address_rv.setAdapter(savedAddressAdapter);

                    } else
                        CommonFunctions.shortToast(activity, response.body().getMessage());

                }

            }

            @Override
            public void onFailure(Call<SavedAddressPojo> call, Throwable t) {

            }
        });

    }

    @Override
    public void cancelAlert(HashMap<String, Object> map) {
        Log.e("TAG", "cancelAlert:");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
        mDatabaseReference.setValue(map);
        if (alertDialog != null) {
            Log.e("TAG", "cancelAlert:alertDialog ");
            alertDialog.cancel();
        }
    }

    @Override
    public void deleteAlert(int position) {
        Call<SuccessPojo> call = apiInterface.deleteAddress(addressList.get(position).getId());
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if(response.code() == 200){
                    if(home_address_rv.getAdapter() != null){
                        addressList.remove(position);
                        home_address_rv.getAdapter().notifyDataSetChanged();
                        CommonFunctions.shortToast(activity, response.body().getMessage());
                    }
                }else if(response.code() != 404){
                    CommonFunctions.shortToast(activity, response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                CommonFunctions.shortToast(activity, t.getMessage());
            }
        });

    }
}
