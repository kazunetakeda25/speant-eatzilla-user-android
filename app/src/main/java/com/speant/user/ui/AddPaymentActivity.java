package com.speant.user.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_recharge_wal)
    AppCompatTextView txtRechargeWal;
    @BindView(R.id.txt_paytm)
    AppCompatTextView txtPaytm;
    @BindView(R.id.txt_free_charge)
    AppCompatTextView txtFreeCharge;
    @BindView(R.id.txt_phonepe)
    AppCompatTextView txtPhonepe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        ButterKnife.bind(this);
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    @OnClick({R.id.txt_recharge_wal, R.id.txt_paytm, R.id.txt_free_charge, R.id.txt_phonepe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_recharge_wal:
                startActivity(new Intent(AddPaymentActivity.this, RechargeWalletActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.txt_paytm:
                break;
            case R.id.txt_free_charge:
                break;
            case R.id.txt_phonepe:
                break;
        }
    }
}
