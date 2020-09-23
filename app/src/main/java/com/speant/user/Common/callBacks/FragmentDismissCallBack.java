package com.speant.user.Common.callBacks;

import com.speant.user.Models.ProfileUpdateResponse;

public interface FragmentDismissCallBack {
    void dismiss(ProfileUpdateResponse.UpdateData updatedData);
}
