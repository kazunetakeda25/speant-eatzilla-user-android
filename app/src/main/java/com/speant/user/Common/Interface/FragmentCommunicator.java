package com.speant.user.Common.Interface;

import android.location.Address;

import java.util.HashMap;
import java.util.List;

public interface FragmentCommunicator {

    public void passAddressToFragment(List<Address> addresses);

    public void passRequestIdToFragment(String request_id,String status);

    public void passAddressListToFragment(HashMap<String,Object> deliveryAddressMap);

}
