package com.speant.user.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.callBacks.onFavouritesClick;
import com.bumptech.glide.Glide;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SearchRestaurantResponse;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.HotelDetailActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedRestaurantAdapter extends RecyclerView.Adapter<SearchedRestaurantAdapter.Viewholder> {
    private static final String TAG = "SearchedRestaurantAdap" ;
    private final SessionManager sessionManager;
    private final APIInterface apiInterface;
    Context context;
    List<SearchRestaurantResponse.SearchRestaurants> searchedList;
    onFavouritesClick onfavouritesClick;

    public SearchedRestaurantAdapter(Context context, List<SearchRestaurantResponse.SearchRestaurants> searchedList, onFavouritesClick onfavouritesClick) {
        this.context = context;
        this.searchedList = searchedList;
        sessionManager = new SessionManager(context);
        this.onfavouritesClick = onfavouritesClick;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_hotels, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
        SearchRestaurantResponse.SearchRestaurants pojo = searchedList.get(position);

        Log.e(TAG, "onBindViewHolder: "+pojo.getPrice() );

        holder.hotelDetailHotelNameTxt.setText(pojo.getName());
        Glide.with(context)
                .load(pojo.getImage())
                .into(holder.hotelImg);


        String hotelTypeStr = "";

        for (int i = 0; i < pojo.getCuisines().size(); i++) {

            if (i != pojo.getCuisines().size() - 1) {
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName() + " • ";
            } else
                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName();

        }

        holder.hotelDetailHotelTypeTxt.setText(hotelTypeStr);
        holder.hotelDetailRatingTxt.setText("" + pojo.getRating());
        holder.hotelDetailTimeTxt.setText(pojo.getTravel_time().contains("mins") ? pojo.getTravel_time() : pojo.getTravel_time()+" Mins" );
        holder.hotelDetailAmtTxt.setText(pojo.getPrice());

        if(pojo.getDiscount().equals("0")){
            holder.hotelDetailHotelDiscountTxt.setText(" ");
        }else{
            holder.hotelDetailHotelDiscountTxt.setText("" + pojo.getDiscount());
        }

        if (pojo.getIs_open().equals("0")) {
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
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary,R.color.colorAccent);
        Log.e("Giri ", "onBindViewHolder:IsFavourite "+pojo.getIs_favourite() );
        if (pojo.getIs_favourite().equals("0")) {
            holder.hotelLikeImg.setLiked(false);
        } else {
            holder.hotelLikeImg.setLiked(true);
        }

        holder.hotelLikeImg.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(sessionManager.isLoggedIn()) {
                    Log.e(TAG, "liked: " + searchedList.get(holder.getAdapterPosition()).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(searchedList.get(holder.getAdapterPosition()).getId()));
                    jsonLikeUpdate(map);
                }else{
                    holder.hotelLikeImg.setLiked(false);
                    onfavouritesClick.favClickOnLogout();
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if(sessionManager.isLoggedIn()) {
                    Log.e(TAG, "unLiked: " + searchedList.get(holder.getAdapterPosition()).getId());
                    HashMap<String, String> map = new HashMap<>();
                    map.put("restaurant_id", String.valueOf(searchedList.get(holder.getAdapterPosition()).getId()));
                    jsonLikeUpdate(map);
                }else{
                    onfavouritesClick.favClickOnLogout();
                }

            }
        });
    }

    private void jsonLikeUpdate(HashMap<String,String> map) {

        Call<SuccessPojo> call = apiInterface.homeLike(sessionManager.getHeader(),map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code()==200){

                    if (response.body().getStatus()){
                    }else
                        CommonFunctions.shortToast(context,response.body().getMessage());

                }else if(response.code() == 401){
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e(TAG, "onFailure: "+ t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return searchedList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.hotel_detail_linear)
        LinearLayout hotelDetailLinear;
        @BindView(R.id.hotel_detail_time_txt)
        TextView hotelDetailTimeTxt;
        @BindView(R.id.hotel_detail_amt_txt)
        TextView hotelDetailAmtTxt;
        @BindView(R.id.hotel_like_img)
        LikeButton hotelLikeImg;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CONST.restaren_id = String.valueOf(searchedList.get(getAdapterPosition()).getId());
                    context.startActivity(new Intent(context,HotelDetailActivity.class));
                }
            });
        }
    }
}
