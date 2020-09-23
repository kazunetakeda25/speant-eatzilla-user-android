package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.RatingRefreshCallBack;
import com.speant.user.Models.CurrentOrderPojo;
import com.speant.user.R;
import com.speant.user.ui.DiningTrackActivity;
import com.speant.user.ui.TrackingActivity;
import com.speant.user.ui.dialogs.RatingsBottomFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class HomeOrderDetailAdapter extends PagerAdapter {
    private final LayoutInflater inflater;
    private final RatingRefreshCallBack ratingRefreshCallBack;
    Activity activity;
    List<CurrentOrderPojo.OrderStatus> orderStatusList;
    @BindView(R.id.home_notify_hotel_name_txt)
    TextView homeNotifyHotelNameTxt;
    @BindView(R.id.home_notify_time_txt)
    TextView homeNotifyTimeTxt;
    @BindView(R.id.home_notify_item_txt)
    TextView homeNotifyItemTxt;
    @BindView(R.id.home_notify_amount_txt)
    TextView homeNotifyAmountTxt;
    @BindView(R.id.track_order_txt)
    TextView trackOrderTxt;
    @BindView(R.id.lay_txt_order)
    LinearLayout layTxtOrder;
    @BindView(R.id.home_notify_delivery_relative)
    RelativeLayout homeNotifyDeliveryRelative;
    @BindView(R.id.notify_rating)
    AppCompatRatingBar notifyRating;
    @BindView(R.id.notify_home_rate_hotel_name_txt)
    TextView notifyHomeRateHotelNameTxt;
    @BindView(R.id.home_notify_rating_relative)
    RelativeLayout homeNotifyRatingRelative;
    @BindView(R.id.lay_item)
    LinearLayout layItem;
    @BindView(R.id.lay_amount)
    LinearLayout layAmount;
    private String requestId;
    SessionManager sessionManager;

    public HomeOrderDetailAdapter(Activity activity, List<CurrentOrderPojo.OrderStatus> orderStatusList, RatingRefreshCallBack ratingRefreshCallBack) {
        this.activity = activity;
        this.orderStatusList = orderStatusList;
        inflater = LayoutInflater.from(activity);
        this.ratingRefreshCallBack = ratingRefreshCallBack;
        sessionManager = new SessionManager(activity);
    }

    private void setStatusView(String status_value_str, String delivery_type) {

        switch (status_value_str) {
            case CONST.NO_ORDER:
                //No request found
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.GONE);
                break;
            case CONST.ORDER_CREATED:
                //Delivery boy not get Assigned
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);

                if (delivery_type.equals(DOOR_DELIVERY)) {
                    trackOrderTxt.setText(activity.getString(R.string.order_created));
                } else if (delivery_type.equals(PICKUP_RESTAURANT)) {
                    trackOrderTxt.setText(activity.getString(R.string.pickup_order_created));
                } else {
                    trackOrderTxt.setText(activity.getString(R.string.dining_order_created));
                }

                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.RESTAURANT_ACCEPTED:
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText(activity.getString(R.string.restaurant_accepted));
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.FOOD_PREPARED:
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText(activity.getString(R.string.food_preparing));
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.DELIVERY_REQUEST_ACCEPTED:
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText(activity.getString(R.string.order_assigned));
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.ORDER_COMPLETE:
                homeNotifyDeliveryRelative.setVisibility(View.GONE);
                homeNotifyRatingRelative.setVisibility(View.VISIBLE);
                break;
            default:
                homeNotifyRatingRelative.setVisibility(View.GONE);
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText(activity.getString(R.string.track_order));
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getCount() {
        Log.e("TAG", "getCount:orderStatusList " + orderStatusList.size());
        return orderStatusList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        CurrentOrderPojo.OrderStatus pojo = orderStatusList.get(position);
        Log.e("Giri ", "setPrimaryItem:position" + position);
        Log.e("Giri ", "setPrimaryItem:BillAmount" + pojo.getBillAmount());
        requestId = "" + pojo.getRequestId();
        Log.e("Giri ", "setPrimaryItem:requestId" + requestId);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {

        View view = inflater.inflate(R.layout.home_cart_list_adapter, viewGroup, false);
        ButterKnife.bind(this, view);
        CurrentOrderPojo.OrderStatus pojo = orderStatusList.get(position);


        setStatusView("" + pojo.getStatus(), pojo.getDelivery_type());

        if (homeNotifyDeliveryRelative.getVisibility() == View.VISIBLE) {
            homeNotifyHotelNameTxt.setText(pojo.getRestaurantName());

            if (pojo.getDelivery_type().equals(DOOR_DELIVERY) ||
                    pojo.getDelivery_type().equals(PICKUP_RESTAURANT)) {
                homeNotifyItemTxt.setText("" + pojo.getItemCount() + "\tItem");
                homeNotifyAmountTxt.setText(pojo.getBillAmount() + "\t" + sessionManager.getCurrency());
            } else {
                layItem.setVisibility(View.GONE);
                layAmount.setVisibility(View.GONE);
            }

            DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            Date date = null;

            String inputText = " ";
            if (pojo.getDelivery_type().equals(DOOR_DELIVERY)) {
                Log.e("TAG", "instantiateItem:DOOR_DELIVERY OrderedTime" + pojo.getOrderedTime());
                inputText = pojo.getOrderedTime();
            } else if (pojo.getDelivery_type().equals(DINING) ||
                    pojo.getDelivery_type().equals(PICKUP_RESTAURANT)) {
                Log.e("TAG", "instantiateItem:DINING dining_time" + pojo.getPickup_dining_time());
                inputText = pojo.getPickup_dining_time();
            }

            try {
                date = inputFormat.parse(inputText);
                homeNotifyTimeTxt.setText(outputFormat.format(date));
            } catch (Exception e) {
                Log.e("TAG", "instantiateItem:Exception date " + e.getMessage());
            }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pojo.getDelivery_type().equals(DINING)) {
                        Log.e("Giri ", "instantiateItem:requestId DINING" + requestId);
                        Intent intent = new Intent(activity, DiningTrackActivity.class);
                        intent.putExtra(CONST.REQUEST_ID, requestId);
                        activity.startActivity(intent);
                    } else if (pojo.getDelivery_type().equals(DOOR_DELIVERY) ||
                            pojo.getDelivery_type().equals(PICKUP_RESTAURANT)) {
                        Log.e("Giri ", "instantiateItem: REQUEST_ID" + requestId);
                        Log.e("Giri ", "instantiateItem: DELIVERY_TYPE" + pojo.getDelivery_type());
                        Intent intent = new Intent(activity, TrackingActivity.class);
                        intent.putExtra(CONST.REQUEST_ID, requestId);
                        intent.putExtra(CONST.DELIVERY_TYPE, pojo.getDelivery_type());
                        activity.startActivity(intent);
                    }

                   /* RatingsBottomFragment ratingsBottomFragment = new RatingsBottomFragment(Integer.parseInt(requestId), activity);
                    ratingsBottomFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), ratingsBottomFragment.getTag());*/
                }
            });
        } else if (homeNotifyRatingRelative.getVisibility() == View.VISIBLE) {
            notifyHomeRateHotelNameTxt.setText(pojo.getRestaurantName());

            homeNotifyRatingRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RatingsBottomFragment ratingsBottomFragment = new RatingsBottomFragment(orderStatusList.get(position).getRequestId(), activity, ratingRefreshCallBack);
                    ratingsBottomFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), ratingsBottomFragment.getTag());
                }
            });
        }


        viewGroup.addView(view, 0);
        return view;
    }

}
