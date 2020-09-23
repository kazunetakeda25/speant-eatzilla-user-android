package com.speant.user.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.speant.user.ui.fragment.HotelItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Integer> categoryIdList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("Giri ", "getItem: ViewPagerAdapter " );
//        return mFragmentList.get(position);
        return HotelItemsFragment.newInstance(categoryIdList.get(position));
    }

    @Override
    public int getCount() {
        return categoryIdList.size();
    }

    public void addFrag(int categoryId, String title) {
        categoryIdList.add(categoryId);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
