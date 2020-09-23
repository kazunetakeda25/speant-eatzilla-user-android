package com.speant.user.Common.callBacks;

public interface DeliveryTypeCallback {
    void OnDeliveryTypeConfirm(String deliveryType, String members, String time);
    void OnDeliveryTypeCancel();
}
