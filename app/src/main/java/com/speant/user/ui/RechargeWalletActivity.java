package com.speant.user.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.BalanceUpdateResponse;
import com.speant.user.Models.WalletBalanceResponse;
import com.speant.user.R;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeWalletActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_amount)
    AppCompatEditText edtAmount;
    @BindView(R.id.txt_100)
    AppCompatTextView txt100;
    @BindView(R.id.txt_500)
    AppCompatTextView txt500;
    @BindView(R.id.txt_1000)
    AppCompatTextView txt1000;
    @BindView(R.id.txt_2000)
    AppCompatTextView txt2000;
    @BindView(R.id.btn_pay)
    AppCompatButton btnPay;
    @BindView(R.id.tv_wallet_balance)
    AppCompatTextView tvWalletBalance;
    private APIInterface apiInterface;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);
        ButterKnife.bind(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        setValues();
        getWalletBalance();

        toolbar.setTitle("Recharge Food Credits");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getWalletBalance() {
        Call<WalletBalanceResponse> call = apiInterface.getwalletBalance(sessionManager.getHeader());
        call.enqueue(new Callback<WalletBalanceResponse>() {
            @Override
            public void onResponse(Call<WalletBalanceResponse> call, Response<WalletBalanceResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        if (response.body().getWallet_balance() != null) {
                            tvWalletBalance.setText(response.body().getWallet_balance());
                        }else{
                            tvWalletBalance.setText("0.00");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletBalanceResponse> call, Throwable t) {
                CommonFunctions.shortToast(RechargeWalletActivity.this, "Service Failed");
            }
        });

    }

    private void setValues() {
        String rs = "+" + getString(R.string.rs);
        txt100.setText(rs.concat(getString(R.string.hundred)));
        txt500.setText(rs.concat(getString(R.string.five_hundred)));
        txt1000.setText(rs.concat(getString(R.string.thousand)));
        txt2000.setText(rs.concat(getString(R.string.two_thousand)));
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

    @OnClick({R.id.txt_100, R.id.txt_500, R.id.txt_1000, R.id.txt_2000, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_100:
                edtAmount.setText(getString(R.string.hundred));
                break;
            case R.id.txt_500:
                edtAmount.setText(getString(R.string.five_hundred));
                break;
            case R.id.txt_1000:
                edtAmount.setText(getString(R.string.thousand));
                break;
            case R.id.txt_2000:
                edtAmount.setText(getString(R.string.two_thousand));
                break;
            case R.id.btn_pay:
                if (!edtAmount.getText().toString().isEmpty()) {
                    setWalletBalanceApi();
                } else {
                    setSnackBar("Enter Recharge Amount");
                }
                break;
        }
    }

    private void setWalletBalanceApi() {
        Call<BalanceUpdateResponse> call = apiInterface.rechargeWallet(sessionManager.getHeader(),edtAmount.getText().toString());
        call.enqueue(new Callback<BalanceUpdateResponse>() {
            @Override
            public void onResponse(Call<BalanceUpdateResponse> call, Response<BalanceUpdateResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        tvWalletBalance.setText(response.body().getWallet_balance());
                    }
                }
            }

            @Override
            public void onFailure(Call<BalanceUpdateResponse> call, Throwable t) {
                CommonFunctions.shortToast(RechargeWalletActivity.this, "Service Failed");
            }
        });
    }
}
