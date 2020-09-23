package com.speant.user.ui.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.DeliveryTypeCallback;
import com.speant.user.Common.callBacks.DiningCallBack;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.DateData;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.RestaurantTiming;
import com.speant.user.Models.TimeData;
import com.speant.user.R;
import com.speant.user.ui.adapter.DiningDayAdapter;
import com.speant.user.ui.adapter.DiningMemberAdapter;
import com.speant.user.ui.adapter.DiningTimeAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class DiningDialog implements RadioGroup.OnCheckedChangeListener, DiningCallBack, DialogInterface.OnDismissListener {

    private static final String TAG = "DiningDialog";
    private final APIInterface apiInterface;
    private final APIInterface apiService;
    private final SessionManager sessionManager;
    private AlertDialog alertDialog;
    Activity activity;
    AppCompatRadioButton radOnline;
    AppCompatRadioButton radPickup;
    AppCompatRadioButton radDine;
    AppCompatTextView title;

    LinearLayout layDate;
    LinearLayout layTime;
    LinearLayout layMember;

    public String selectedDeliveryType = "";
    private String selectedTime = "";
    private String selectedMembers = "";
    private String selectedDate = "";

    List<String> deliveryTypeList = new ArrayList<>();
    RadioGroup radioGroup;

    public static final int TIME_PICKER_REQUEST = 1;

    NearRestarentPojo.Restaurants selectedRestaurant;

    RecyclerView totalMemberRecycler;
    RecyclerView timeRecycler;
    RecyclerView dayRecycler;

    DiningDayAdapter diningDayAdapter;
    DiningTimeAdapter diningTimeAdapter;
    DiningMemberAdapter diningMemberAdapter;
    String deliveryType;
    DeliveryTypeCallback deliveryTypeCallback;

    List<TimeData> timeList = new ArrayList<>();
    List<DateData> dateList = new ArrayList<>();
    List<String> memberList = new ArrayList<>();
    List<RestaurantTiming> restaurantTimingList = new ArrayList<>();

    public DiningDialog(Activity activity, NearRestarentPojo.Restaurants selectedRestaurant, List<String> deliveryTypeList, String deliveryType, DeliveryTypeCallback deliveryTypeCallback) {
        this.activity = activity;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        this.deliveryTypeCallback = deliveryTypeCallback;
        this.selectedRestaurant = selectedRestaurant;
        this.restaurantTimingList = selectedRestaurant.getRestaurant_timing();
        this.deliveryType = deliveryType;
        this.deliveryTypeList.clear();
        this.deliveryTypeList.addAll(deliveryTypeList);
        setView();
        setData();
    }

    private void setView() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        final View view = activity.getLayoutInflater().inflate(R.layout.alert_delivery_type, null);
        // Set above view in alert dialog.
        builder.setView(view);
        layDate = view.findViewById(R.id.lay_date);
        layMember = view.findViewById(R.id.lay_member);
        layTime = view.findViewById(R.id.lay_time);
        radOnline = view.findViewById(R.id.rad_door);
        radPickup = view.findViewById(R.id.rad_pickup);
        radDine = view.findViewById(R.id.rad_dine);
        AppCompatTextView txtCancel = view.findViewById(R.id.btn_cancel);
        AppCompatTextView txtOk = view.findViewById(R.id.btn_ok);
        radioGroup = view.findViewById(R.id.rad_grp);
        title = view.findViewById(R.id.title);

        /*layEdt = view.findViewById(R.id.lay_edt);
        layInpTime = view.findViewById(R.id.layinp_time);
        edtTime = view.findViewById(R.id.edt_time);
        layInpMember = view.findViewById(R.id.layinp_members);
        edtMembers = view.findViewById(R.id.edt_member);*/

        dayRecycler = view.findViewById(R.id.day_recycler);
        totalMemberRecycler = view.findViewById(R.id.total_member_recycler);
        timeRecycler = view.findViewById(R.id.time_recycler);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        dayRecycler.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        timeRecycler.setLayoutManager(linearLayoutManager2);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        totalMemberRecycler.setLayoutManager(linearLayoutManager3);

         /*Log.e(TAG, "openDeliveryTypeDialog: " + deliveryTypeList.size());
        for (String data : deliveryTypeList) {
            switch (data) {
                case DOOR_DELIVERY:
                    radOnline.setVisibility(View.VISIBLE);
                    break;
                case PICKUP_RESTAURANT:
                    radPickup.setVisibility(View.VISIBLE);
                    break;
                case DINING:
                    radDine.setVisibility(View.VISIBLE);
                    break;
            }
        }

        //only for Dining Restaurants
        if (deliveryType.equals(DINING)) {
            radDine.setChecked(true);
            selectedDeliveryType = DINING;
            title.setText(activity.getString(R.string.dine_date_time));
            layTime.setVisibility(View.VISIBLE);
            layMember.setVisibility(View.VISIBLE);
            layDate.setVisibility(View.VISIBLE);
        } else {
            title.setText(activity.getResources().getString(R.string.select_delivery));
        }*/

        switch (deliveryType) {
            case DOOR_DELIVERY:
                selectedDeliveryType = DOOR_DELIVERY;
                title.setText(activity.getString(R.string.dine_date_time));
                layTime.setVisibility(View.GONE);
                layMember.setVisibility(View.GONE);
                layDate.setVisibility(View.GONE);
                break;
            case PICKUP_RESTAURANT:
                selectedDeliveryType = PICKUP_RESTAURANT;
                title.setText(activity.getString(R.string.dine_date_time));
                layTime.setVisibility(View.VISIBLE);
                layMember.setVisibility(View.GONE);
                layDate.setVisibility(View.GONE);
                break;
            case DINING:
                selectedDeliveryType = DINING;
                title.setText(activity.getString(R.string.dine_date_time));
                layTime.setVisibility(View.VISIBLE);
                layMember.setVisibility(View.VISIBLE);
                layDate.setVisibility(View.VISIBLE);
                break;
        }



       /* layEdt.setVisibility(View.VISIBLE);
        layInpTime.setVisibility(View.VISIBLE);
        layInpMember.setVisibility(View.VISIBLE);*/


        radioGroup.setOnCheckedChangeListener(this::onCheckedChanged);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        /*edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call Time picker fragment using setTargetFragment if it is called from a fragment
                DialogFragment timeDialogFragment = new TimePickerFromFragment();
                timeDialogFragment.setTargetFragment(activity, TIME_PICKER_REQUEST);
                timeDialogFragment.show(activity., "Time Picker");
            }
        });*/


        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedDeliveryType.isEmpty()) {
                    switch (selectedDeliveryType) {
                        case DOOR_DELIVERY:
                            callService();
                            break;
                        case PICKUP_RESTAURANT:
                            if (!selectedTime.isEmpty()) {
                                callService();
                            } else {
                                CommonFunctions.shortToast(activity, "Select time of Pickup");
                            }
                            break;
                        case DINING:
                            if (DiningValidate()) {
                                callService();
                            }

                            break;
                    }
                } else {
                    CommonFunctions.shortToast(activity, "Select a delivery Type");
                }
            }
        });


        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(this::onDismiss);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }


    private void setData() {

        dateList.addAll(Global.getDateList("yyyy-MM-dd"));
        Log.e(TAG, "setData:dateList.size "+dateList.size() );


        for (RestaurantTiming restaurantTiming : restaurantTimingList) {
            timeList.addAll(Global.getTimeList(restaurantTiming.getOpening_time(), restaurantTiming.getClosing_time(),restaurantTiming.getIs_weekend()));
        }
        Log.e(TAG, "setData:timeList.size "+timeList.size() );


        Log.e(TAG, "setData:Max_dining_count "+selectedRestaurant.getMax_dining_count());
        for (int i = 1; i <= selectedRestaurant.getMax_dining_count(); i++) {
            memberList.add("" + i);
        }

        openDeliveryTypeDialog();
    }


    private void openDeliveryTypeDialog() {

        Log.e(TAG, "openDeliveryTypeDialog");

        diningDayAdapter = new DiningDayAdapter(activity, dateList, this);
        dayRecycler.setAdapter(diningDayAdapter);

        if(!dateList.isEmpty()) {
            diningTimeAdapter = new DiningTimeAdapter(activity, timeList, this,dateList.get(0).getType());
            timeRecycler.setAdapter(diningTimeAdapter);
        }

        diningMemberAdapter = new DiningMemberAdapter(activity, memberList, this);
        totalMemberRecycler.setAdapter(diningMemberAdapter);

    }

    private void callService() {
       /* if (deliveryType.equals(DINING)) {
            redirectToPayment();
        }else{
            deliveryTypeCallback.OnDeliveryTypeConfirm(selectedDeliveryType,selectedMembers,selectedDate + selectedTime);
        }*/

        deliveryTypeCallback.OnDeliveryTypeConfirm(selectedDeliveryType, selectedMembers, selectedDate + selectedTime);
    }

    private void resetValues() {
        selectedTime = "";
        selectedMembers = "";
        selectedDate = "";
        diningDayAdapter.resetAdapter();
        diningMemberAdapter.resetAdapter();
        diningTimeAdapter.resetAdapter();
    }

    private boolean DiningValidate() {
        if (selectedTime.isEmpty()) {
            CommonFunctions.shortToast(activity, "Select time of Dining");
            return false;
        } else if (selectedMembers.isEmpty()) {
            CommonFunctions.shortToast(activity, "Select Members for Dining");
            return false;
        } else if (selectedDate.isEmpty()) {
            CommonFunctions.shortToast(activity, "Select Date of Dining");
            return false;
        }
        return true;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e(TAG, "onCheckedChanged: checkedId" + checkedId);
        switch (checkedId) {
            case R.id.rad_door:
                selectedDeliveryType = DOOR_DELIVERY;
                resetValues();
                layTime.setVisibility(View.GONE);
                layMember.setVisibility(View.GONE);
                layDate.setVisibility(View.GONE);
                break;

            case R.id.rad_pickup:
                selectedDeliveryType = PICKUP_RESTAURANT;
                resetValues();
                layTime.setVisibility(View.VISIBLE);
                layMember.setVisibility(View.GONE);
                layDate.setVisibility(View.GONE);

                break;

            case R.id.rad_dine:
                selectedDeliveryType = DINING;
                resetValues();
                layTime.setVisibility(View.VISIBLE);
                layMember.setVisibility(View.VISIBLE);
                layDate.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void OnDateSelect(DateData date) {
        Log.e(TAG, "OnDateSelect:date " + date.getDate());
        selectedDate = date.getDate();
        selectedTime= "";
        diningTimeAdapter = new DiningTimeAdapter(activity, timeList, this, date.getType());
        timeRecycler.setAdapter(diningTimeAdapter);
    }

    @Override
    public void OnMemberSelect(String members) {
        Log.e(TAG, "OnMemberSelect:members " + members);
        selectedMembers = members;
    }

    @Override
    public void OnTimeSelect(TimeData time) {
        Log.e(TAG, "OnTimeSelect:time " + time);
        String finalTime = Global.getDateFromString(time.getTime(), "hh:mm a", "HH:mm:ss");
        Log.e(TAG, "OnTimeSelect:finalTime " + finalTime);
        selectedTime = finalTime;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.e(TAG, "onDismiss:DinningDialog " );
        deliveryTypeCallback.OnDeliveryTypeCancel();
    }

    public void dismissDialog() {
        Log.e(TAG, "dismissDialog:DinningDialog " );
        if(alertDialog != null){
            alertDialog.dismiss();
        }
    }
}