package com.dinodevs.greatfitwatchface;

import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo;
import com.dinodevs.greatfitwatchface.data.CaloriesRepo;
import com.dinodevs.greatfitwatchface.data.StepsRepo;
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo;
import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.dinodevs.greatfitwatchface.widget.GreatWidget;
import com.dinodevs.greatfitwatchface.widget.MainClock;
import com.huami.watch.watchface.AbstractSlptClock;

import java.lang.ref.WeakReference;


/**
 * Amazfit watch faces
 */

public class GreatFit extends AbstractWatchFace {
    public GreatFit() {
        super();
    }
    private static WeakReference<GreatFit> instance;
    private GreatWidget greatWidget = null;


    @Override
    public void onCreate() {
        instance = new WeakReference(this);

        // Load settings
        LoadSettings settings = new LoadSettings(this.getApplicationContext());

        final BatteryLevelRepo batteryLevelRepo = () -> settings.battery_percent;
        final StepsRepo stepsRepo = () -> settings.steps;
        final CaloriesRepo caloriesRepo = () -> settings.calories;
        final TodayDistanceRepo distanceRepo = () -> settings.today_distance;
        this.clock = new MainClock(batteryLevelRepo, stepsRepo, caloriesRepo, distanceRepo);

        notifyStatusBarPosition(0f, 175f);

        super.onCreate();
    }

    public static GreatWidget getGreatWidget() {
        WeakReference weakReference = instance;
        if (weakReference != null) {
            return ((GreatFit) weakReference.get()).greatWidget;
        }
        return null;
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return GreatFitSlpt.class;
    }
}