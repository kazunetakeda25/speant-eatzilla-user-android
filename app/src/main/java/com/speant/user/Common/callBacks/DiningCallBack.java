package com.speant.user.Common.callBacks;

import com.speant.user.Models.DateData;
import com.speant.user.Models.TimeData;

public interface DiningCallBack {

    void OnDateSelect(DateData date);
    void OnMemberSelect(String members);
    void OnTimeSelect(TimeData time);
}
