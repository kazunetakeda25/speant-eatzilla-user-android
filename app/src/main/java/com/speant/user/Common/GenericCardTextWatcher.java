package com.speant.user.Common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.speant.user.R;

import androidx.appcompat.widget.AppCompatTextView;

public class GenericCardTextWatcher implements TextWatcher {
    private View view;
    public GenericCardTextWatcher(View view) {
        this.view = view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch(view.getId()){
            case R.id.txt_card_num:
                AppCompatTextView txtCardNum = (AppCompatTextView) view;
                txtCardNum.setText(text);
                break;
            case R.id.txt_expiry_card:
                AppCompatTextView txtExpiryCard = (AppCompatTextView) view;
                txtExpiryCard.setText(text);
                break;
        }
    }
}