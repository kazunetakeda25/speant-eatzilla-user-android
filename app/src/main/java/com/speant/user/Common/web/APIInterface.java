package com.speant.user.Common.web;


import com.speant.user.Models.AddCardRequest;
import com.speant.user.Models.AddCardResponse;
import com.speant.user.Models.BalanceUpdateResponse;
import com.speant.user.Models.BannerPojo;
import com.speant.user.Models.CardListResponse;
import com.speant.user.Models.CardUpdateResponse;
import com.speant.user.Models.CategoryPojo;
import com.speant.user.Models.CategoryWiseFoodPojo;
import com.speant.user.Models.ChatHistory;
import com.speant.user.Models.CheckAddressResponse;
import com.speant.user.Models.CheckCartPojo;
import com.speant.user.Models.CheckOfferResponse;
import com.speant.user.Models.CheckoutPojo;
import com.speant.user.Models.CreditStatusResponse;
import com.speant.user.Models.CreditUpdateResponse;
import com.speant.user.Models.CurrentOrderPojo;
import com.speant.user.Models.DirectionResults;
import com.speant.user.Models.FavResResponse;
import com.speant.user.Models.FilterPojo;
import com.speant.user.Models.FoodListResponse;
import com.speant.user.Models.HistoryResponse;
import com.speant.user.Models.HotelDetailPojo;
import com.speant.user.Models.LatestVersionResponse;
import com.speant.user.Models.LoginPojo;
import com.speant.user.Models.MenuPojo;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.OfferResponse;
import com.speant.user.Models.PlacesPojo;
import com.speant.user.Models.PopularBrandsPojo;
import com.speant.user.Models.ProfileUpdateResponse;
import com.speant.user.Models.RatingRequest;
import com.speant.user.Models.RatingResponse;
import com.speant.user.Models.RelevanceHotelPojo;
import com.speant.user.Models.SavedAddressPojo;
import com.speant.user.Models.SearchRestaurantResponse;
import com.speant.user.Models.StripeTokenResponse;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.Models.TodayTrendPojo;
import com.speant.user.Models.TrackingDetailPojo;
import com.speant.user.Models.UpdateTokenResponse;
import com.speant.user.Models.UrlResponse;
import com.speant.user.Models.WalletBalanceResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @FormUrlEncoded
    @POST("{url}")
//Its for both login and signup
    Call<LoginPojo> loginorSignup(@Path(value = "url", encoded = true) String type, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("{url}")
        //It user for Send OTP and ResetPassword
    Call<SuccessPojo> sendOtp(@Path(value = "url", encoded = true) String type, @FieldMap Map<String, String> fields);

    @GET("{url}")
        //It user for Send OTP and ResetPassword
    Call<SuccessPojo> logout(@Path(value = "url", encoded = true) String type, @HeaderMap Map<String, String> fields);

    @GET("get_filter_list/{type}")
    Call<FilterPojo> getFilter(@Path(value = "type", encoded = true) String type, @HeaderMap Map<String, String> header);

    @GET("get_delivery_address")
    Call<SavedAddressPojo> getSavedAddress(@HeaderMap Map<String, String> header);

    @FormUrlEncoded
    @POST("add_delivery_address")
    Call<SuccessPojo> addAddress(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @GET("place/autocomplete/json?")
    Call<PlacesPojo> doPlaces(@Query(value = "key", encoded = true) String key,
                              @Query(value = "input", encoded = true) String input);

    @FormUrlEncoded
    @POST("get_nearby_restaurant?")
    Call<NearRestarentPojo> getNearRestarent(@HeaderMap Map<String, String> header,
                                             @FieldMap Map<String, String> fields,
                                             @Field("cuisines[]") ArrayList<String> cuisines,
                                             @Field("relevance[]") ArrayList<String> relevance,
                                             @QueryMap Map<String, String> LatLang,
                                             @Field("page") String totalPage);

    @FormUrlEncoded
    @POST("get_relevance_restaurant")
    Call<RelevanceHotelPojo> getRelevance(@HeaderMap Map<String, String> header,
                                          @FieldMap Map<String, String> fields,
                                          @Field("cuisines[]") ArrayList<String> cuisines,
                                          @Field("relevance[]") ArrayList<String> relevance,
                                          @QueryMap Map<String, String> LatLang);

    @GET("get_banners")
    Call<BannerPojo> getBanners(@HeaderMap Map<String, String> header);

    @GET("get_popular_brands")
    Call<PopularBrandsPojo> getPopularBrands(@HeaderMap Map<String, String> header, @QueryMap HashMap<String, String> locationMap);

    @GET("todays_special")
    Call<TodayTrendPojo> getTrendToday(@HeaderMap HashMap<String, String> header, @QueryMap HashMap<String, String> locationMap);

    @FormUrlEncoded
    @POST("update_favourite")
    Call<SuccessPojo> homeLike(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("get_menu")
    Call<MenuPojo> menuList(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("single_restaurant")
    Call<HotelDetailPojo> hotelDetail(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("add_to_cart")
    Call<SuccessPojo> addToCart(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("reduce_from_cart")
    Call<SuccessPojo> reduceFromCart(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @GET("check_cart")
    Call<CheckCartPojo> checkCart(@HeaderMap Map<String, String> header);

    @FormUrlEncoded
    @POST("checkout")
    Call<CheckoutPojo> checkout(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields, @Field("android_version") int versionCode);

    @GET("get_category/{restaurant_id}")
    Call<CategoryPojo> getCategory(@HeaderMap Map<String, String> header, @Path(value = "restaurant_id", encoded = true) String bid);

    @GET("get_current_order_status")
    Call<CurrentOrderPojo> getCurrentOrderStatus(@HeaderMap Map<String, String> header);

    @FormUrlEncoded
    @POST("track_order_detail")
    Call<TrackingDetailPojo> trackingDetail(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("get_category_wise_food_list")
    Call<CategoryWiseFoodPojo> getCategoryWiseFoodList(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("paynow")
    Call<SuccessPojo> finalPayment(@HeaderMap Map<String, String> header,
                                   @FieldMap Map<String, String> fields, @Field("food_id[]") ArrayList<String> food_id,
                                   @Field("food_qty[]") ArrayList<String> food_qty, @Field("food_quantity[]") ArrayList<String> foodQuantityIdList,
                                   @Field("food_quantity_price[]") ArrayList<String> foodQuantityPriceList,
                                   @Field("add_ons[]") ArrayList<String> addOnsIdList);

    @FormUrlEncoded
    @POST("generateChecksum")
    Call<SuccessPojo> getHaskKey(@HeaderMap Map<String, String> header,
                                 @Field("amount") String finalAmount,
                                 @Field("food_id[]") ArrayList<String> food_id,
                                 @Field("food_qty[]") ArrayList<String> food_qty,
                                 @Field("food_quantity[]") ArrayList<String> foodQuantityIdList,
                                 @Field("food_quantity_price[]") ArrayList<String> foodQuantityPriceList,
                                 @Field("add_ons[]") ArrayList<String> addOnsIdList);


    @GET("directions/json?")
    Call<DirectionResults> polyLines(@Query(value = "key") String key,
                                     @Query(value = "origin") String origin,
                                     @Query(value = "destination") String destination);

    @GET("order_history")
    Call<HistoryResponse> getOrderHistory(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("get_food_list")
    Call<FoodListResponse> getfoodlist(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("update_profile")
    Call<ProfileUpdateResponse> editProfile(@FieldMap HashMap<String, String> map);

    @GET("get_favourite_list")
    Call<FavResResponse> getFavList(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("search_restaurants")
    Call<SearchRestaurantResponse> getSearchList(@HeaderMap HashMap<String, String> header,
                                                 @Field("key_word") String searchName,
                                                 @QueryMap Map<String, String> LatLang);

    @GET("get_cards")
    Call<CardListResponse> getAddedCards(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("default_card")
    Call<CardUpdateResponse> setDefaultCard(@HeaderMap HashMap<String, String> header, @Field("card_id") String cardId);

    @FormUrlEncoded
    @POST("delete_card")
    Call<CardUpdateResponse> setDeleteCard(@HeaderMap HashMap<String, String> header, @Field("card_id") String cardId);

    @POST("add_card")
    Call<AddCardResponse> addCard(@HeaderMap HashMap<String, String> header, @Body AddCardRequest addCardRequest);

    @GET("get_stripe_token")
    Call<StripeTokenResponse> getStripeToken(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("add_money_to_wallet")
    Call<BalanceUpdateResponse> rechargeWallet(@HeaderMap HashMap<String, String> header, @Field("amount") String amount);

    @GET("get_balance")
    Call<WalletBalanceResponse> getwalletBalance(@HeaderMap HashMap<String, String> header);

    @GET("get_promo_list")
    Call<OfferResponse> getOfferList(@HeaderMap HashMap<String, String> header);

    @POST("order_ratings")
    Call<RatingResponse> setRating(@HeaderMap HashMap<String, String> header, @Body RatingRequest ratingRequest);

    @FormUrlEncoded
    @POST("check_promocode")
    Call<CheckOfferResponse> checkOffers(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("check_restaurant_availability")
    Call<CheckAddressResponse> checkAddress(@HeaderMap HashMap<String, String> header, @QueryMap HashMap<String, String> locationMap, @Field("restaurant_id") String restaurantId, @Field("android_version") int versionCode);

    @FormUrlEncoded
    @POST("paynow_dining?")
    Call<SuccessPojo> finalGoPayment(@HeaderMap Map<String, String> header,
                                     @Field("restaurant_id") String restaurant_id,
                                     @Field("delivery_type") String selectedDeliveryType,
                                     @Field("pickup_dining_time") String selectedDateTime,
                                     @Field("total_members") String selectedmembers);

    @FormUrlEncoded
    @POST("get_dining_restaurant?")
    Call<NearRestarentPojo> getNearGoRestarent(@HeaderMap HashMap<String, String> header,
                                               @QueryMap HashMap<String, String> locationMap,
                                               @Field("page") String totalpage);

    @GET("get_cms_pages")
    Call<UrlResponse> getSupportUrl(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("version_code_check")
    Call<LatestVersionResponse> checkLatestVersion(@HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> hashMap);


    //chat

    @GET("get_chat_history/{request_id}/{type}")
    Call<ChatHistory> getChat(@Path("request_id") String req_id, @Path("type") String type);

    @GET("delete_delivery_address/{address_id}")
    Call<SuccessPojo> deleteAddress(@Path("address_id") int id);

    @FormUrlEncoded
    @POST("update_stripetoken")
    Call<UpdateTokenResponse> updateToken(@HeaderMap HashMap<String, String> header,
                                          @Field("stripe_access_token") String token,
                                          @Field("account_id") String accId);

    @GET("get_credit_amount")
    Call<CreditStatusResponse> getcreditstate(@HeaderMap HashMap<String, String> header);

    @FormUrlEncoded
    @POST("update_credit_amount")
    Call<CreditUpdateResponse> updateCredit(@HeaderMap HashMap<String, String> header , @Field("credit_amount") String amount);
}