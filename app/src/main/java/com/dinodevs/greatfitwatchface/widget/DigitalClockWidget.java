package com.dinodevs.greatfitwatchface.widget;

import android.content.Context;
import android.graphics.Canvas;


public abstract class DigitalClockWidget implements ClockWidget {

    @Override
    public void init(Context context) {
        
    }

    public abstract void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm);
}
