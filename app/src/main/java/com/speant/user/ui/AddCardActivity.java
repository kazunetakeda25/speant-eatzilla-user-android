package com.speant.user.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.GenericCardTextWatcher;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.AddCardRequest;
import com.speant.user.Models.AddCardResponse;
import com.speant.user.Models.StripeTokenResponse;
import com.speant.user.R;
import com.google.android.material.snackbar.Snackbar;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardActivity extends AppCompatActivity {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.img_card)
    AppCompatImageView imgCard;
    @BindView(R.id.img_card_type)
    AppCompatImageView imgCardType;
    @BindView(R.id.txt_card_num)
    AppCompatTextView txtCardNum;
    @BindView(R.id.txt_expiry_card)
    AppCompatTextView txtExpiryCard;
    @BindView(R.id.btn_scan_card)
    LinearLayout btnScanCard;
    @BindView(R.id.card_widget)
    CardMultilineWidget cardWidget;
    @BindView(R.id.btn_add_card)
    AppCompatTextView btnAddCard;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private static final int MY_SCAN_REQUEST_CODE = 2293;
    private Stripe stripe;

    private String stripeKey;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        getStripeToken();


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_card_st));
        cardWidget.setCardNumberTextWatcher(new GenericCardTextWatcher(txtCardNum));
        cardWidget.setExpiryDateTextWatcher(new GenericCardTextWatcher(txtExpiryCard));
    }

    private void getStripeToken() {
        Call<StripeTokenResponse> call = apiInterface.getStripeToken(sessionManager.getHeader());
        call.enqueue(new Callback<StripeTokenResponse>() {
            @Override
            public void onResponse(Call<StripeTokenResponse> call, Response<StripeTokenResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        stripeKey = response.body().getToken();
                    }
                }
            }

            @Override
            public void onFailure(Call<StripeTokenResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    @OnClick({R.id.btn_scan_card, R.id.btn_add_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan_card:
                onScanPress();
                break;
            case R.id.btn_add_card:
                if (cardWidget.validateAllFields()) {

                    snackbar = Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "Getting Card Token! Please Wait", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    snackbar.show();

                    btnAddCard.setEnabled(false);
                    Log.e("TAG", "onViewClicked: " + cardWidget.getCard().getNumber());
                    Log.e("TAG", "onViewClicked: " + cardWidget.getCard().getBrand());
                    Log.e("TAG", "onViewClicked: " + cardWidget.getCard().getExpYear());
                    Log.e("TAG", "onViewClicked: " + cardWidget.getCard().getExpMonth());
                    Log.e("TAG", "onViewClicked: " + cardWidget.getCard().getCustomerId());
                    stripe = new Stripe(this, stripeKey);
                    Card card = cardWidget.getCard();


                    stripe.createToken(card,stripeKey, new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            Log.e("TAG", "OnCVVComplete:onError " + error.getMessage());
                            snackbar.setText("Failed to get Token");
                            snackbar.dismiss();
                        }

                        @Override
                        public void onSuccess(Token token) {
                            String stripeId = token.getId();
                            snackbar.setText("Adding Card ! Please Wait");
                            Log.e("TAG", "OnCVVComplete:onSuccess " + stripeId);
                            Log.e("TAG", "OnCVVComplete:getcard " + token.getCard().getCustomerId());
                            Log.e("TAG", "OnCVVComplete:getcard " + token.getCard().getId());
                            String cardNumber = cardWidget.getCard().getNumber();
                            int cardLength = cardNumber.length();
                            String lastFour = cardNumber.substring(cardLength - 4, cardLength);

                            AddCardRequest addCardRequest = new AddCardRequest();
                            addCardRequest.setLast_four(lastFour);
                            addCardRequest.setCard_token(stripeId);
                            addCardRequest.setCustomer_id(token.getCard().getId());
                            addCardApiRequest(addCardRequest);
                        }
                    });

                }
                break;
        }
    }

    private void addCardApiRequest(AddCardRequest addCardRequest) {
        Call<AddCardResponse> call = apiInterface.addCard(sessionManager.getHeader(), addCardRequest);
        call.enqueue(new Callback<AddCardResponse>() {
            @Override
            public void onResponse(Call<AddCardResponse> call, Response<AddCardResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        CommonFunctions.shortToast(AddCardActivity.this, "Card Added Successfully");
                        finish();
                    } else {
                        btnAddCard.setEnabled(true);
                    }
                } else {
                    btnAddCard.setEnabled(true);
                }

                snackbar.dismiss();

            }

            @Override
            public void onFailure(Call<AddCardResponse> call, Throwable t) {
                btnAddCard.setEnabled(true);
                snackbar.dismiss();
                CommonFunctions.shortToast(AddCardActivity.this, "Service Failed");
            }
        });
    }

    private void onScanPress() {

        Intent intent = new Intent(this, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_NO_CAMERA, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false)
                .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
                .putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false)
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
                .putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false)
                .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en")
                .putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
                .putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false)
                .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, false)
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, false)
                .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);

        startActivityForResult(intent, MY_SCAN_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("TAG", "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");

        String outStr = new String();
        Bitmap cardTypeImage = null;

        if ((requestCode == MY_SCAN_REQUEST_CODE) && data != null
                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {

                outStr += result.getFormattedCardNumber();

                CardType cardType = result.getCardType();
                cardTypeImage = cardType.imageBitmap(this);

            }

        }
        imgCardType.setImageBitmap(cardTypeImage);
        cardWidget.setCardNumber(outStr);
        Log.i("TAG", "Set result: " + outStr);
    }

}
