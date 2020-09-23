package com.speant.user.Common.customCarosuel;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.speant.user.Common.callBacks.UpdateCreditCallBack;
import com.speant.user.R;
import com.speant.user.ui.fragment.CreditFragment;
import com.speant.user.ui.fragment.CreditPagerFragment;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private Activity context;
    private FragmentManager fragmentManager;
    private float scale;
    private CreditFragment creditFragment;
    private UpdateCreditCallBack updateCreditCallBack;

    public CarouselPagerAdapter(Activity context, FragmentManager fm, CreditFragment creditFragment, UpdateCreditCallBack updateCreditCallBack) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        this.context = context;
        this.creditFragment = creditFragment;
        this.updateCreditCallBack = updateCreditCallBack;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("TAG", "getItem:position "+position );
        // make the first pager bigger than others
        try {
            if (position == CreditFragment.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % CreditFragment.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  CreditPagerFragment.newInstance(context, position, scale,updateCreditCallBack);
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = CreditFragment.count * CreditFragment.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + creditFragment.pager.getId() + ":" + position;
    }
}