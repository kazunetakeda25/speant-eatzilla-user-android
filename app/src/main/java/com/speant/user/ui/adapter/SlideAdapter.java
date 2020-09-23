package com.speant.user.ui.adapter;

/**
 * Created by welcome on 23-08-2017.
 */

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.speant.user.R;

import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {


    private ArrayList<Integer> images;
    private ArrayList<String> titleList;
    private ArrayList<String> descriptionList;
    private LayoutInflater inflater;
    private Context context;

    public SlideAdapter(Context context, ArrayList<Integer> images, ArrayList<String> titleList, ArrayList<String> descList) {
        this.context = context;
        this.images = images;
        this.titleList = titleList;
        this.descriptionList = descList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        TextView slider_heading_txt = (TextView) myImageLayout
                .findViewById(R.id.slider_heading_txt);
        TextView slider_desc_txt = (TextView) myImageLayout
                .findViewById(R.id.slider_desc_txt);
        myImage.setImageResource(images.get(position));
        slider_desc_txt.setText(descriptionList.get(position));
        slider_heading_txt.setText(titleList.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
