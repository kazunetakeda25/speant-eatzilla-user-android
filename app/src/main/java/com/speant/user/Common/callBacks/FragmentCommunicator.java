package com.speant.user.Common.callBacks;

import com.speant.user.Models.OnLocationFetch;
import com.speant.user.Models.OnRequestIdFetch;

public interface FragmentCommunicator {

    public void passAddressToFragment(OnLocationFetch onLocationFetch);

    public void passRequestIdToFragment(OnRequestIdFetch onRequestIdFetch);

//    public void passAddressListToFragment(HashMap<String,Object> deliveryAddressMap);

}