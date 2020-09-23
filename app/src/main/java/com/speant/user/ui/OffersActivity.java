package com.speant.user.ui;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Models.OfferResponse;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.R;
import com.speant.user.ui.adapter.OffersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.offers_rv)
    ShimmerRecyclerView offersRv;
    private Activity activity;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private LinearLayoutManager MyLayoutManager;
    private OffersAdapter offersRvAdapter;
    List<OfferResponse.PromoList> promoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(this);
        toolbar.setTitle("Offers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity = OffersActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);

        MyLayoutManager = new LinearLayoutManager(activity);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        offersRv.setLayoutManager(MyLayoutManager);
        offersRvAdapter = new OffersAdapter(activity, promoList);
        offersRv.setAdapter(offersRvAdapter);
        offersRv.showShimmerAdapter();
        getOfferList();

    }

    private void getOfferList() {
        Call<OfferResponse> call = apiInterface.getOfferList(sessionManager.getHeader());
        call.enqueue(new Callback<OfferResponse>() {
            @Override
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        promoList.clear();
                        promoList.addAll(response.body().getPromo_list());
                        if(promoList.size() >0) {
                            offersRv.getAdapter().notifyDataSetChanged();
                        }else{
                            CommonFunctions.shortToast(OffersActivity.this,"No Offers Found");
                        }
                    }
                }
                offersRv.hideShimmerAdapter();
            }

            @Override
            public void onFailure(Call<OfferResponse> call, Throwable t) {

            }
        });
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

}
