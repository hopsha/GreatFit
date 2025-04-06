package com.dinodevs.greatfitwatchface;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo;
import com.dinodevs.greatfitwatchface.data.CaloriesRepo;
import com.dinodevs.greatfitwatchface.data.StepsRepo;
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo;
import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.dinodevs.greatfitwatchface.widget.BatteryWidget;
import com.dinodevs.greatfitwatchface.widget.CaloriesWidget;
import com.dinodevs.greatfitwatchface.widget.FloorWidget;
import com.dinodevs.greatfitwatchface.widget.HeartRateWidget;
import com.dinodevs.greatfitwatchface.widget.MainClock;
import com.dinodevs.greatfitwatchface.widget.GreatWidget;
import com.dinodevs.greatfitwatchface.widget.MoonPhaseWidget;
import com.dinodevs.greatfitwatchface.widget.SportTodayDistanceWidget;
import com.dinodevs.greatfitwatchface.widget.SportTotalDistanceWidget;
import com.dinodevs.greatfitwatchface.widget.StepsWidget;
import com.dinodevs.greatfitwatchface.widget.WeatherWidget;
import com.dinodevs.greatfitwatchface.widget.Widget;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

/**
 * Splt version of the watch.
 */

public class GreatFitSlpt extends AbstractWatchFaceSlpt {
    Context context;

    public GreatFitSlpt() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this.getApplicationContext();

        // Load settings
        LoadSettings settings = new LoadSettings(context);

        final BatteryLevelRepo batteryLevelRepo = () -> settings.battery_percent;
        final StepsRepo stepsRepo = () -> settings.steps;
        final CaloriesRepo caloriesRepo = () -> settings.calories;
        final TodayDistanceRepo distanceRepo = () -> settings.today_distance;
        this.clock = new MainClock(batteryLevelRepo, stepsRepo, caloriesRepo, distanceRepo);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected SlptLayout createClockLayout26WC() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this, true)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this, true)) {
                result.add(component);
            }
        }

        return result;
    }

    @Override
    protected SlptLayout createClockLayout8C() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        return result;
    }

    protected void initWatchFaceConfig() {
        //Log.w("DinoDevs-GreatFit", "Initiating watchface");
    }

    @Override
    public boolean isClockPeriodSecond() {
        Context context = this.getApplicationContext();
        boolean needRefreshSecond = Util.needSlptRefreshSecond(context);
        if (needRefreshSecond) {
            this.setClockPeriodSecond(true);
        }
        return needRefreshSecond;
    }

}