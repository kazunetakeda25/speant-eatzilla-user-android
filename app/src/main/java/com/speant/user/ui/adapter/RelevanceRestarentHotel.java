package com.speant.user.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.ui.HotelDetailActivity;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.RelevanceHotelPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelevanceRestarentHotel extends RecyclerView.Adapter<RelevanceRestarentHotel.ViewHolder> {

    private final onFavouritesClick onFavouritesClick;
    private Context mContext;
    private List<RelevanceHotelPojo.Restaurants> restaurantsArrayList;
    private static final String TAG = "RelevanceRestarent";
    SessionManager sessionManager;
    APIInterface apiInterface;

    public RelevanceRestarentHotel(Context context, List<RelevanceHotelPojo.Restaurants> relevanceRestarentList, onFavouritesClick onFavouritesClick) {
        this.mContext = context;
        this.restaurantsArrayList = relevanceRestarentList;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.onFavouritesClick = onFavouritesClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_relavance_restarent, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int tpos) {

        RelevanceHotelPojo.Restaurants pojo = restaurantsArrayList.get(tpos);

        holder.hotelDetailHotelNameTxt.setText(pojo.getName());
       /* Glide.with(mContext)
                .load(pojo.getImage())
                .into(holder.restaListImg);*/
        Picasso.get().load(pojo.getImage()).into(holder.restaListImg);
        Log.e(TAG, "onBindViewHolder: " + pojo.getImage());

        String hotelTypeStr = "";

        for (int i = 0; i < pojo.getCuisines().size(); i++) {

            if (i != pojo.getCuisines().size() - 1) {
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName() + " • ";
            } else
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName();

        }

        holder.hotelDetailHotelTypeTxt.setText(hotelTypeStr);
        holder.hotelDetailRatingTxt.setText("" + pojo.getRating());
        holder.hotelDetailTimeTxt.setText(pojo.getTravelTime().contains("mins") ? pojo.getTravelTime() : pojo.getTravelTime()+" Mins" );
        holder.hotelDetailAmtTxt.setText(sessionManager.getCurrency()+" "+ pojo.getPrice());
        holder.hotelDetailHotelDiscountTxt.setText("" + pojo.getDiscount());

        if (pojo.getIsOpen() == 0) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.restaListImg.setColorFilter(filter);
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
        }

        holder.hotelLikeImg.setUnlikeDrawableRes(R.drawable.ic_unlike);
        holder.hotelLikeImg.setLikeDrawableRes(R.drawable.ic_heart_green);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorAccent);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorPrimary);
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary, R.color.colorAccent);

        if (pojo.getIsFavourite() == 0) {
            holder.hotelLikeImg.setLiked(false);
        } else {
            holder.hotelLikeImg.setLiked(true);
        }

        /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.restaren_id = String.valueOf(restaurantsArrayList.get(tpos).getId());
                Intent intent = new Intent(mContext, HotelDetailActivity.class);
                mContext.startActivity(intent);
            }
        });*/

        holder.hotelLikeImg.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(sessionManager.isLoggedIn()) {
                    Log.e(TAG, "liked: " + restaurantsArrayList.get(tpos).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(restaurantsArrayList.get(tpos).getId()));
                    jsonLikeUpdate(map);
                }else{
                    onFavouritesClick.favClickOnLogout();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if(sessionManager.isLoggedIn()) {
                    Log.e(TAG, "unLiked: " + restaurantsArrayList.get(tpos).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(restaurantsArrayList.get(tpos).getId()));
                    jsonLikeUpdate(map);
                }else{
                    onFavouritesClick.favClickOnLogout();
                }

            }
        });

        Log.e(TAG, "onBindViewHolder: " + pojo.getName());

    }

    @Override
    public int getItemCount() {
        return restaurantsArrayList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.resta_list_img)
        ImageView restaListImg;
        @BindView(R.id.hotel_like_img)
        LikeButton hotelLikeImg;
        @BindView(R.id.resta_list_rel)
        RelativeLayout restaListRel;
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
        @BindView(R.id.hotel_detail_rating_txt)
        TextView hotelDetailRatingTxt;
        @BindView(R.id.hotel_detail_linear)
        LinearLayout hotelDetailLinear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CONST.restaren_id = String.valueOf(restaurantsArrayList.get(getAdapterPosition()).getId());
                    System.out.println("the restaraunt id:"+CONST.restaren_id);
                    Intent intent = new Intent(mContext, HotelDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
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

                }else if(response.code() == 401){
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
