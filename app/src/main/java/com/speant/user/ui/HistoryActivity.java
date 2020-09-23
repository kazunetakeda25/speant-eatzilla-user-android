package com.speant.user.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.HistoryResponse;
import com.speant.user.Models.PastOrders;
import com.speant.user.Models.UpcomingOrders;
import com.speant.user.R;
import com.speant.user.ui.adapter.PastOrderAdapter;
import com.speant.user.ui.adapter.UpcomingOrderAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyler_upcoming)
    RecyclerView recylerUpcoming;
    @BindView(R.id.recycler_past)
    RecyclerView recyclerPast;
    @BindView(R.id.upcoming_lay)
    LinearLayout upcomingLay;
    @BindView(R.id.past_lay)
    LinearLayout pastLay;
    @BindView(R.id.progress)
    ProgressBar progress;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private List<PastOrders> pastOrders = new ArrayList<>();
    private List<UpcomingOrders> upcomingOrders = new ArrayList<>();
    private PastOrderAdapter pastOrderAdapter;
    private UpcomingOrderAdapter upcomingOrderAdapter;
    LinearLayoutManager pastLinearLayoutManager;
    LinearLayoutManager upcomingLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pastLinearLayoutManager = new LinearLayoutManager(HistoryActivity.this, RecyclerView.VERTICAL, false);
        recyclerPast.setLayoutManager(pastLinearLayoutManager);
        recyclerPast.setNestedScrollingEnabled(false);
        pastOrderAdapter = new PastOrderAdapter(HistoryActivity.this, pastOrders);
        recyclerPast.setAdapter(pastOrderAdapter);

        upcomingLinearLayoutManager = new LinearLayoutManager(HistoryActivity.this, RecyclerView.VERTICAL, false);
        recylerUpcoming.setLayoutManager(upcomingLinearLayoutManager);
        recylerUpcoming.setNestedScrollingEnabled(false);
        upcomingOrderAdapter = new UpcomingOrderAdapter(HistoryActivity.this, upcomingOrders);
        recylerUpcoming.setAdapter(upcomingOrderAdapter);

        getOrderHistory();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
        getOrderHistory();
    }

    private void getOrderHistory() {
        Call<HistoryResponse> call = apiInterface.getOrderHistory(sessionManager.getHeader());
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        System.out.println("upcomigsize"+upcomingOrders.size());

                        upcomingOrders.clear();
                        pastOrders.clear();

                        pastOrders.addAll(response.body().getPast_orders());
                        upcomingOrders.addAll(response.body().getUpcoming_orders());

                        if(upcomingOrders.size() >0){
                            Log.e("TAG", "onResponse:upcomingOrders "+upcomingOrders.get(0).getRestaurant_name() );
                        }
                        if(pastOrders.size() >0){
                            Log.e("TAG", "onResponse:pastOrders "+pastOrders.get(0).getRestaurant_name() );
                        }




                        pastOrderAdapter.notifyDataSetChanged();
                        upcomingOrderAdapter.notifyDataSetChanged();

                        if (!pastOrders.isEmpty()) {
                            pastLay.setVisibility(View.VISIBLE);

                        } else {
                            pastLay.setVisibility(View.GONE);
                        }

                        if (!upcomingOrders.isEmpty()) {
                            upcomingLay.setVisibility(View.VISIBLE);

                        } else {
                            upcomingLay.setVisibility(View.GONE);
                        }
                    } else {
                        CommonFunctions.shortToast(HistoryActivity.this, getString(R.string.no_order_history));
                    }

                }
                progress.setVisibility(View.GONE);

                if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }

            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t );
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void setPastListAdapter() {

    }

    private void setUpcomingListAdapter() {

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
