package com.speant.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.swipe.util.Attributes;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.callBacks.onCardSelect;
import com.speant.user.Common.customViews.CustomText.CustomRegularTextView;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.CardData;
import com.speant.user.Models.CardListResponse;
import com.speant.user.Models.CardUpdateResponse;
import com.speant.user.R;
import com.speant.user.ui.adapter.CardListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListActivity extends BaseActivity implements onCardSelect {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mTxtAddCard)
    CustomRegularTextView mTxtAddCard;
    @BindView(R.id.cardlist_recycler)
    RecyclerView cardlistRecycler;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private CardListAdapter cardListAdapter;
    List<CardData> cardDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.select_card));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        cardlistRecycler.setLayoutManager(linearLayoutManager);
        cardListAdapter = new CardListAdapter(this,cardDataList, this);
        cardListAdapter.setMode(Attributes.Mode.Single);
        cardlistRecycler.setAdapter(cardListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddedCardsApi(sessionManager.getHeader());
    }

    private void getAddedCardsApi(HashMap<String, String> header) {
        Call<CardListResponse> call = apiInterface.getAddedCards(header);
        call.enqueue(new Callback<CardListResponse>() {
            @Override
            public void onResponse(Call<CardListResponse> call, Response<CardListResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        if(!response.body().getData().isEmpty()) {
                            cardDataList.clear();
                            cardDataList.addAll(response.body().getData());
                            if (cardlistRecycler.getAdapter() != null) {
                                cardListAdapter.setSelectedPosition();
                                cardlistRecycler.getAdapter().notifyDataSetChanged();
                            }
                        }else{
                            setSnackBar("No Cards Added");
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<CardListResponse> call, Throwable t) {
                setSnackBar("Service Failed");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @OnClick({R.id.mToolbar, R.id.mTxtAddCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mToolbar:
                break;
            case R.id.mTxtAddCard:
                startActivity(new Intent(CardListActivity.this, AddCardActivity.class));
                break;
        }
    }

    @Override
    public void onCardSelected(int position) {
        setDefaultCardApi(cardDataList.get(position).getId());
    }

    @Override
    public void onCardDelete(int position, String cardId) {
        deleteCardApi(position,cardId);
    }

    private void setDefaultCardApi(String cardId) {
        Call<CardUpdateResponse> call = apiInterface.setDefaultCard(sessionManager.getHeader(),cardId);
        call.enqueue(new Callback<CardUpdateResponse>() {
            @Override
            public void onResponse(Call<CardUpdateResponse> call, Response<CardUpdateResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        setSnackBar("DefaultCard Changed Successfully");
                    }
                }
            }

            @Override
            public void onFailure(Call<CardUpdateResponse> call, Throwable t) {
                if(cardlistRecycler.getAdapter() != null) {
                    cardlistRecycler.getAdapter().notifyDataSetChanged();
                }
                setSnackBar("Service Failed");
            }
        });

    }

    private void deleteCardApi(int position,String cardId) {
        Call<CardUpdateResponse> call = apiInterface.setDeleteCard(sessionManager.getHeader(),cardId);
        call.enqueue(new Callback<CardUpdateResponse>() {
            @Override
            public void onResponse(Call<CardUpdateResponse> call, Response<CardUpdateResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        setSnackBar("Card Deleted SuccessFully");
                        cardDataList.remove(position);
                        if(cardlistRecycler.getAdapter() != null) {
                            cardlistRecycler.getAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CardUpdateResponse> call, Throwable t) {
                if(cardlistRecycler.getAdapter() != null) {
                    cardlistRecycler.getAdapter().notifyDataSetChanged();
                }
                setSnackBar("Service Failed");
            }
        });
    }
}
