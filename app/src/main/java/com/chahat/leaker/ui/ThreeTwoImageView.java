package com.chahat.leaker.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chahat on 10/8/17.
 */

public class ThreeTwoImageView extends android.support.v7.widget.AppCompatImageView {

    public ThreeTwoImageView(Context context){
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public ThreeTwoImageView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec)*2/3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,threeTwoHeightSpec);
    }
}
