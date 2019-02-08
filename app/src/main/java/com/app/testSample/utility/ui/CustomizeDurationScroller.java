package com.app.testSample.utility.ui;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomizeDurationScroller extends Scroller {

    private double scrollFactor = 1;

    public CustomizeDurationScroller(Context context) {
        super(context);
    }

    public CustomizeDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
    }
}
