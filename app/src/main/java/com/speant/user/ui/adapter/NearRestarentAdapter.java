package com.speant.user.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.HotelDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearRestarentAdapter extends RecyclerView.Adapter<NearRestarentAdapter.ViewHolder> {

    private static final String TAG = "NearRestarentAdapter";

    SessionManager sessionManager;
    APIInterface apiInterface;
    onFavouritesClick onFavouritesClick;
    public Context mContext;
    public int finalBindPosition;
    List<NearRestarentPojo.Restaurants> restarentPojoList;


    public int getFinalBindPosition() {
        return finalBindPosition;
    }

    public void setFinalBindPosition(int finalBindPosition) {
        this.finalBindPosition = finalBindPosition;
    }

    public NearRestarentAdapter(Context context, List<NearRestarentPojo.Restaurants> nearRestarentList, onFavouritesClick onFavouritesClick) {
        this.restarentPojoList = nearRestarentList;
        mContext = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.onFavouritesClick = onFavouritesClick;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hotels, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int tpos) {


        setFinalBindPosition(tpos);

        NearRestarentPojo.Restaurants pojo = restarentPojoList.get(tpos);

        Log.e(TAG, "onBindViewHolder: " + pojo.getPrice());

        holder.hotelDetailHotelNameTxt.setText(pojo.getName());
      /*  Glide.with(mContext)
                .load(pojo.getImage())
                .pla
                .into(holder.hotelImg);*/
        Picasso.get().load(pojo.getImage()).placeholder(R.drawable.ic_placeholder).into(holder.hotelImg);

        if(pojo.getCredit_accept().equals("1")){
            holder.txtCreditOffer.setVisibility(View.VISIBLE);
        }else {
            holder.txtCreditOffer.setVisibility(View.GONE);
        }

        String hotelTypeStr = "";

        for (int i = 0; i < pojo.getCuisines().size(); i++) {

            if (i != pojo.getCuisines().size() - 1) {
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName() + " • ";
            } else
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName();

        }

        holder.hotelDetailHotelTypeTxt.setText(hotelTypeStr);
        Log.e(TAG, "onBindViewHolder: NearRestarent" + pojo.getRating());
        holder.hotelDetailTimeTxt.setText(pojo.getTravelTime().contains("mins") ? pojo.getTravelTime() : pojo.getTravelTime() + " Mins");
        holder.hotelDetailAmtTxt.setText(pojo.getPrice());

        if (pojo.getDiscount().equals("0")) {
            holder.hotelDetailHotelDiscountTxt.setText(" ");
        } else {
            holder.hotelDetailHotelDiscountTxt.setText("" + pojo.getDiscount());
        }


        if (pojo.getIsOpen() == 0) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.hotelImg.setColorFilter(filter);
            holder.itemView.setClickable(false);
        }

        holder.hotelLikeImg.setUnlikeDrawableRes(R.drawable.ic_unlike);
        holder.hotelLikeImg.setLikeDrawableRes(R.drawable.ic_heart_green);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorAccent);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorPrimary);
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary, R.color.colorAccent);
        holder.hotelDetailRatingTxt.setText("" + restarentPojoList.get(tpos).getRating());
        if (pojo.getIsFavourite() == 0) {
            holder.hotelLikeImg.setLiked(false);
        } else {
            holder.hotelLikeImg.setLiked(true);
        }

        holder.hotelLikeImg.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (sessionManager.isLoggedIn()) {
                    Log.e(TAG, "liked: " + restarentPojoList.get(tpos).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(restarentPojoList.get(tpos).getId()));
                    jsonLikeUpdate(map);
                } else {
                    holder.hotelLikeImg.setLiked(false);
                    onFavouritesClick.favClickOnLogout();
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (sessionManager.isLoggedIn()) {
                    Log.e(TAG, "unLiked: " + restarentPojoList.get(tpos).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(restarentPojoList.get(tpos).getId()));
                    jsonLikeUpdate(map);
                } else {
                    onFavouritesClick.favClickOnLogout();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return restarentPojoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hotel_img)
        ImageView hotelImg;
        @BindView(R.id.hotel_detail_rating_txt)
        TextView hotelDetailRatingTxt;
        @BindView(R.id.hotel_detail_hotel_name_txt)
        TextView hotelDetailHotelNameTxt;
        @BindView(R.id.hotel_detail_hotel_type_txt)
        TextView hotelDetailHotelTypeTxt;
        @BindView(R.id.hotel_detail_hotel_discount_txt)
        TextView hotelDetailHotelDiscountTxt;
        @BindView(R.id.hotel_detail_time_txt)
        TextView hotelDetailTimeTxt;
        @BindView(R.id.hotel_detail_amt_txt)
        TextView hotelDetailAmtTxt;
        @BindView(R.id.hotel_detail_linear)
        LinearLayout hotelDetailLinear;
        @BindView(R.id.hotel_like_img)
        LikeButton hotelLikeImg;
        @BindView(R.id.txt_credit_offer)
        AppCompatTextView txtCreditOffer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CONST.restaren_id = String.valueOf(restarentPojoList.get(getAdapterPosition()).getId());
                    Intent intent = new Intent(mContext, HotelDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public void jsonLikeUpdate(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.homeLike(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                    } else
                        CommonFunctions.shortToast(mContext, response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}
