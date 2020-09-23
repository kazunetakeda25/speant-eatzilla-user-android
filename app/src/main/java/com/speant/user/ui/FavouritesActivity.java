package com.speant.user.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;

import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.ui.fragment.LoginBottomFragment;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.FavResResponse;
import com.speant.user.R;
import com.speant.user.ui.adapter.FavRestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesActivity extends AppCompatActivity implements onFavouritesClick, LoginSuccessCallBack {

    @BindView(R.id.fav_restarent_rv)
    ShimmerRecyclerView favRestarentRv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context context;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private LinearLayoutManager MyLayoutManager;
    FavRestaurantAdapter favRestaurantAdapter;
    List<FavResResponse.FavouriteList> favouriteLists = new ArrayList<>();
    private LoginBottomFragment loginBottomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        context = FavouritesActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(context);

        MyLayoutManager = new LinearLayoutManager(context);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        favRestarentRv.setLayoutManager(MyLayoutManager);
        favRestaurantAdapter = new FavRestaurantAdapter(context, favouriteLists,this::favClickOnLogout);
        favRestarentRv.setAdapter(favRestaurantAdapter);
        favRestarentRv.showShimmerAdapter();

        getFavouriteList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getFavouriteList() {
        Call<FavResResponse> call = apiInterface.getFavList(sessionManager.getHeader());
        call.enqueue(new Callback<FavResResponse>() {
            @Override
            public void onResponse(Call<FavResResponse> call, Response<FavResResponse> response) {
                if(response.code() == 200) {
                    favouriteLists.clear();
                    if (response.body().getStatus()) {
                        favouriteLists.addAll(response.body().getFavourite_list());
                    }else{
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                    }
                    if (favRestarentRv.getAdapter() != null) {
                        Log.e("Giri ", "onResponse:notifyDataSetChanged ");
                        favRestaurantAdapter.notifyDataSetChanged();
                    }
                }else if(response.code() == 401){
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }
                favRestarentRv.hideShimmerAdapter();

            }

            @Override
            public void onFailure(Call<FavResResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
    }

    @Override
    public void favClickOnLogout() {
        openLoginFragment();
    }

    private void openLoginFragment() {
        loginBottomFragment = new LoginBottomFragment(this, this::onDismissFragment);
        loginBottomFragment.showNow(getSupportFragmentManager(), "loginFragment");
    }
}
