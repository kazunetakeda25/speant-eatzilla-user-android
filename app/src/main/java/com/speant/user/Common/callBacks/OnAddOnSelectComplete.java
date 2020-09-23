package com.speant.user.Common.callBacks;

import com.speant.user.Models.AddOns;
import com.speant.user.Models.FoodQuantity;

import java.util.List;

public interface OnAddOnSelectComplete {
    void onAddOnComplete(List<AddOns> selectedAddOnsList, FoodQuantity foodQuantity);
}
