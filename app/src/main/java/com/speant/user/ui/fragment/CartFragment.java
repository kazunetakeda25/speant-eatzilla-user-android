package com.speant.user.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.speant.user.Common.CONST;
import com.speant.user.Common.callBacks.CheckLoginCallBack;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.ui.ViewCartActivity;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.app.App;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.R;
import com.speant.user.ui.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends androidx.fragment.app.Fragment implements CheckLoginCallBack, LoginSuccessCallBack {

    APIInterface apiInterface;
    SessionManager sessionManager;
    CartAdapter cartAdapter;

    @BindView(R.id.dummyLay)
    LinearLayout dummyLay;
    @BindView(R.id.cartRecyclerView)
    ShimmerRecyclerView cartRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.topLay)
    LinearLayout topLay;
    private LoginBottomFragment loginBottomFragment;
    private LinearLayoutManager MyLayoutManager;
    ArrayList<StringBuilder> combineItemList = new ArrayList<>();

    StringBuilder joinString;


    List<FoodItemDb> foodItemDbList = new ArrayList<>();


    StringBuilder builder = new StringBuilder();


    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        sessionManager = new SessionManager(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        unbinder = ButterKnife.bind(this, view);
        MyLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        cartRecyclerView.setLayoutManager(MyLayoutManager);
        cartRecyclerView.showShimmerAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setList();
       /* if (!combineItemList.isEmpty()) {
            Log.e("Giri ", "onResume: combineItemList");
            combineItemList.clear();
        }
        if (!itemListArrayList.isEmpty()) {
            Log.e("Giri ", "onResume: itemListArrayList");
            itemListArrayList.clear();
        }
        if (cartRecyclerView.getAdapter() != null) {
            Log.e("Giri ", "onResume: getAdapter");
            cartRecyclerView.getAdapter().notifyDataSetChanged();
        }

        setData();*/

    }

    /*private void setData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callListCartApi();
            }
        },3000);

    }

    private void callListCartApi() {
        Call<CheckCartPojo> call = apiInterface.checkCart(sessionManager.getHeader());
        call.enqueue(new Callback<CheckCartPojo>() {
            @Override
            public void onResponse(Call<CheckCartPojo> call, Response<CheckCartPojo> response) {
                if(response.code() == 200) {
                    try {
                        Log.e("Nive ", "onResponse: Cart" + response);
                        setList(response.body());
                    } catch (Exception e) {
                        Log.e("Giri ", "onResponse: ListCart Exception" + e);
                    }
                }else if(response.code() == 401){
                    sessionManager.logoutUser();

                }

            }

            @Override
            public void onFailure(Call<CheckCartPojo> call, Throwable t) {
                try {
                    cartRecyclerView.setVisibility(View.GONE);
                    dummyLay.setVisibility(View.VISIBLE);
                    topLay.setVisibility(View.GONE);
                }catch(Exception e){
                    Log.e("Giri ", "onResponse: ListCart Exception" + e);
                }
            }
        });

    }*/

    private void setList() {
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();

        if (cartDetailsDbList.size() > 0) {
            //initialise const RestId then only the quantity list and addOn list will get displayed in view cart activity
            CONST.restaren_id = String.valueOf(cartDetailsDbList.get(0).getRestaurant_id());
            cartRecyclerView.setVisibility(View.VISIBLE);
            topLay.setVisibility(View.VISIBLE);
            dummyLay.setVisibility(View.GONE);

            foodItemDbList.clear();
            foodItemDbList.addAll(App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll());

            Log.e("Giri ", "setList:Size " + foodItemDbList.size());

            joinString = new StringBuilder();

            //this for loop use to join all items into one cardItem to display
            for (int i = 0; i < foodItemDbList.size(); i++) {

                if (i == foodItemDbList.size() - 1) {
                    joinString.append(foodItemDbList.get(i).getFood_qty()).append(" x ").append(foodItemDbList.get(i).getFood_name()).append(".");
                } else {
                    joinString.append(foodItemDbList.get(i).getFood_qty()).append(" x ").append(foodItemDbList.get(i).getFood_name()).append(",").append("\n");
                }


            }

            combineItemList.clear();
            combineItemList.add(joinString);

            Log.e("Giri ", "combineItemList: " + combineItemList);
            Log.e("Giri ", "joinString: " + joinString);

            cartAdapter = new CartAdapter(getActivity(), cartDetailsDbList, foodItemDbList,combineItemList,this::loginCheck);
            cartRecyclerView.setAdapter(cartAdapter);
            cartRecyclerView.hideShimmerAdapter();

        }else{
            cartRecyclerView.setVisibility(View.GONE);
            dummyLay.setVisibility(View.VISIBLE);
            topLay.setVisibility(View.GONE);
        }


    }


    @Override
    public void loginCheck() {
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), ViewCartActivity.class);
            startActivity(intent);
        } else {
            loginBottomFragment = new LoginBottomFragment(getActivity(), this::onDismissFragment);
            loginBottomFragment.showNow(getFragmentManager(), "loginFragment");
        }
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        Intent intent = new Intent(getActivity(), ViewCartActivity.class);
        startActivity(intent);
    }
}
