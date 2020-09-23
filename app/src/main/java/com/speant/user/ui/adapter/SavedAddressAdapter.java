package com.speant.user.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.speant.user.Common.CONST;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.CancelAlert;
import com.speant.user.Common.callBacks.DeleteAlert;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SavedAddressPojo;
import com.speant.user.R;
import com.speant.user.ui.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.ViewHolder> {

    private static final String TAG = "AddressAdapter";
    List<SavedAddressPojo.Data> addrList = new ArrayList<>();
    Activity context;
    APIInterface apiInterface;
    String activityName;

    private SessionManager sessionManager;
    private HomeFragment fragmentCommunicator;
    private CancelAlert cancelAlert;
    private DeleteAlert deleteAlert;

    //    GEOFIRE
    DatabaseReference mDatabaseReference;

    public SavedAddressAdapter(Activity homeFragment, String activityNames, ArrayList<SavedAddressPojo.Data> addressList, CancelAlert cancelAlert, DeleteAlert deleteAlert) {
        context = homeFragment;
        addrList = addressList;
        this.activityName = activityNames;
        this.cancelAlert = cancelAlert;
        this.deleteAlert = deleteAlert;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Log.e(TAG, "SavedAddressAdapter: " + addressList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        if (addrList.get(position).getType() == 1) {
            holder.addressTypeImg.setImageResource(R.drawable.ic_home);
            holder.addressTypeTxt.setText("Home");
        } else if (addrList.get(position).getType() == 2) {
            holder.addressTypeImg.setImageResource(R.drawable.ic_work_addr);
            holder.addressTypeTxt.setText("Work");
        } else if (addrList.get(position).getType() == 3) {
            holder.addressTypeImg.setImageResource(R.drawable.ic_work_addr);
            holder.addressTypeTxt.setText("Other");
        }

        holder.addressTxt.setText(addrList.get(position).getAddress());

        holder.addressLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(sessionManager.getUserDetails().get(KEY_USER_ID));
                mDatabaseReference.child(CONST.Params.lat).setValue(String.valueOf(addrList.get(position).getLat()));
                mDatabaseReference.child(CONST.Params.lng).setValue(String.valueOf(addrList.get(position).getLng()));
                mDatabaseReference.child(CONST.Params.current_address).setValue(addrList.get(position).getAddress());*/
                Log.e(TAG, "onClick:addrList.get(position).getAddress() " + addrList.get(position).getAddress());

                /*if (activityName.equalsIgnoreCase("ViewCartActivity")){
                    ((ViewCartActivity)context).cancelAlert();
                }else if (activityName.equalsIgnoreCase("HomeFragment")){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(CONST.CURRENT_ADDRESS,""+addrList.get(position).getAddress());
                    map.put(CONST.CURRENT_LATITUDE,""+addrList.get(position).getLat());
                    map.put(CONST.CURRENT_LONGITUDE,""+addrList.get(position).getLng());
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra(CONST.HASHMAP_VALUES,(Serializable) map);
                    context.startActivity(intent);
                    context.finishAffinity();
                }*/
                LatLng latLng = new LatLng(addrList.get(position).getLat(), addrList.get(position).getLng());
                String city = Global.getCity(context, latLng);
                HashMap<String, Object> map = new HashMap<>();
                map.put(CONST.CURRENT_ADDRESS, addrList.get(position).getAddress());
                map.put(CONST.CURRENT_LATITUDE, String.valueOf(addrList.get(position).getLat()));
                map.put(CONST.CURRENT_LONGITUDE, String.valueOf(addrList.get(position).getLng()));
                map.put(CONST.CURRENT_CITY, city);

                cancelAlert.cancelAlert(map);

            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlert.deleteAlert(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addrList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.address_type_img)
        ImageView addressTypeImg;
        @BindView(R.id.address_type_txt)
        TextView addressTypeTxt;
        @BindView(R.id.address_txt)
        TextView addressTxt;
        @BindView(R.id.address_linear)
        LinearLayout addressLinear;
        @BindView(R.id.img_delete)
        ImageView imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
