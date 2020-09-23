package com.speant.user.Common.customViews.CustomText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.speant.user.R;

/**
 * Created by user on 1/4/2017.
 */

public class CustomRegularEditView extends androidx.appcompat.widget.AppCompatEditText {

    private static final String TAG = "EditText";

    private Typeface typeface;

    public CustomRegularEditView(Context context) {
        super(context);
    }

    public CustomRegularEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHintTextColor(getResources().getColor(R.color.main_color));
        setCustomFont(context, attrs);
    }

    public CustomRegularEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        String customFont = a.getString(R.styleable.app_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "font/sourcesanspro_regular.otf");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }

}

