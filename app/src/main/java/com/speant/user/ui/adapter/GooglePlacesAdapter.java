package com.speant.user.ui.adapter;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.Global;
import com.speant.user.Common.callBacks.CancelAlert;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.PlacesPojo;
import com.speant.user.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GooglePlacesAdapter extends RecyclerView.Adapter<GooglePlacesAdapter.ViewHolder> {

    private static final String TAG = "GooglePlaces";

    private ArrayList<PlacesPojo.Predictions> places_api_list;
    private Activity activity;
    SessionManager sessionManager;
    APIInterface apiInterface;
    String activityName;
    CancelAlert cancelAlert;
    //    GEOFIRE
    DatabaseReference mDatabaseReference;

    public GooglePlacesAdapter(Activity activity, String activityName, ArrayList<PlacesPojo.Predictions> pojoPosition, CancelAlert cancelAlert) {
        this.places_api_list = pojoPosition;
        this.activity = activity;
        this.activityName = activityName;
        this.cancelAlert = cancelAlert;
        sessionManager = new SessionManager(activity);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_places, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.places_pojo = places_api_list.get(i);
        viewHolder.locHeadTxt.setText(places_api_list.get(i).getStructuredFormatting().getMainText());
        viewHolder.locAddrTxt.setText(places_api_list.get(i).getStructuredFormatting().getSecondaryText());

        if (i == places_api_list.size() - 1) {
            viewHolder.locView.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.preventTwoClick(v);
                String address = places_api_list.get(i).getDescription();
                LatLng to_LatLng = null;

                Geocoder geoCoder = new Geocoder(activity);
                if (address != null && !address.isEmpty()) {
                    try {
                        List<Address> addressList = geoCoder.getFromLocationName(address, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Double latitude = addressList.get(0).getLatitude();
                            Double longitude = addressList.get(0).getLongitude();

                            /*HashMap<String, Object> map = new HashMap<>();
                            map.put(CONST.CURRENT_ADDRESS,address);
                            map.put(CONST.CURRENT_LATITUDE,latitude);
                            map.put(CONST.CURRENT_LONGITUDE,longitude);
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
                            mDatabaseReference.setValue(map);*/

                            /*if (activityName.equalsIgnoreCase("ViewCartActivity")){
                                ((ViewCartActivity)activity).cancelAlert();
                            }else {
                                Intent intent = new Intent(activity,HomeActivity.class);
                                intent.putExtra(CONST.HASHMAP_VALUES,(Serializable) map);
                                activity.startActivity(intent);
                                activity.finishAffinity();
                            }*/
                            LatLng latLng = new LatLng(latitude,longitude);
                            String city = Global.getCity(activity, latLng);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(CONST.CURRENT_ADDRESS,address);
                            map.put(CONST.CURRENT_LATITUDE,latitude);
                            map.put(CONST.CURRENT_LONGITUDE,longitude);
                            map.put(CONST.CURRENT_CITY, city);
                            cancelAlert.cancelAlert(map);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*((ActivityPlaceSearch) mContext).updatePlace(pojoPosition,
                        places_api_list.get(i).getDescription(),
                        places_api_list.get(i).getDescription(),to_LatLng);*/
            }
        });

    }



    @Override
    public int getItemCount() {
        return places_api_list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loc_head_txt)
        TextView locHeadTxt;
        @BindView(R.id.loc_addr_txt)
        TextView locAddrTxt;
        @BindView(R.id.loc_view)
        View locView;
        @BindView(R.id.place_linear)
        LinearLayout placeLinear;
        PlacesPojo.Predictions places_pojo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();

    }



}
