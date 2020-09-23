package com.speant.user.ui.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.OfferClickCallBack;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.CheckOfferResponse;
import com.speant.user.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDialog{
    private final APIInterface apiInterface;
    private final APIInterface apiService;
    Activity activity;
    OfferClickCallBack offerClickCallBack;
    AlertDialog alertDialog;
    String billamount;
    SessionManager sessionManager;
    EditText applyCodeEdt;
    String selectedDeliveryType;

    public OfferDialog(Activity activity, OfferClickCallBack offerClickCallBack, String billamount, String selectedDeliveryType) {
        this.activity = activity;
        this.offerClickCallBack = offerClickCallBack;
        this.billamount = billamount;
        this.selectedDeliveryType =selectedDeliveryType;
        sessionManager = new SessionManager(activity);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        setAlertDialog(activity);
    }


    private void setAlertDialog(Activity activity) {
        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.offer_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        applyCodeEdt = (EditText) promptsView
                .findViewById(R.id.edt_apply_code);

        // set dialog message
        alertDialogBuilder
                .setTitle(" ")
                .setCancelable(true)
                .setPositiveButton("Apply",null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (!applyCodeEdt.getText().toString().isEmpty()) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(CONST.Params.bill_amount, billamount);
                            map.put(CONST.Params.promocode, applyCodeEdt.getText().toString().trim());
                            map.put(CONST.Params.delivery_type,selectedDeliveryType);
                            jsonCheckOffers(map);
                        }else {
                            CommonFunctions.shortToast(activity, "Enter Valid Coupon Code");
                        }
                    }
                });
            }
        });

        // show it
        alertDialog.show();
    }

    private void jsonCheckOffers(HashMap<String, String> map) {
        Log.e("TAG", "jsonCheckOffers:HashMap " +map );
        Call<CheckOfferResponse> call = apiInterface.checkOffers(sessionManager.getHeader(), map);
        call.enqueue(new Callback<CheckOfferResponse>() {
            @Override
            public void onResponse(Call<CheckOfferResponse> call, Response<CheckOfferResponse> response) {

                if (response.code() == 200) {

                    if (response.body().isStatus()) {
                        CommonFunctions.shortToast(activity, "Offer Applied Successfully");
                        offerClickCallBack.onApplyClick(response.body().getOffer_amount(),applyCodeEdt.getText().toString());
                        alertDialog.cancel();
                    } else {
                        CommonFunctions.shortToast(activity, response.body().getMessage());
                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CheckOfferResponse> call, Throwable t) {

                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}