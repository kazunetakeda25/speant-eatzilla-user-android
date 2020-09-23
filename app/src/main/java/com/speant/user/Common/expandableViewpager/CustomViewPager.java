package com.speant.user.Common.expandableViewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager {

    /*boolean canSwipe = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("TAG", "onMeasure: "+widthMeasureSpec+"heightMeasureSpec"+heightMeasureSpec );
        Log.e("TAG", "onMeasure:CurrentItem "+getCurrentItem());
        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void canSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }*/

    //-------------------------------------------

   /* public CustomViewPager (Context context) {
        super(context);
    }

    public CustomViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

        final View tab = getChildAt(getCurrentItem());
        int width = getMeasuredWidth();
        int tabHeight = tab.getMeasuredHeight();

        if (wrapHeight) {
            // Keep the current measured width.
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        int fragmentHeight = measureFragment(((Fragment) getAdapter().instantiateItem(this, getCurrentItem())).getView());
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(tabHeight + fragmentHeight + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()), MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }*/

   //---------------------------------

    private int mCurrentPagePosition = 0;

    public CustomViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            View child = getChildAt(mCurrentPagePosition);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void reMeasureCurrentPage(int position) {
        mCurrentPagePosition = position;
        requestLayout();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
    //down one is added for smooth scrolling

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}