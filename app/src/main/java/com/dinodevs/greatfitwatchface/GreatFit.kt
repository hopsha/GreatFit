package com.dinodevs.greatfitwatchface

import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo
import com.dinodevs.greatfitwatchface.data.CaloriesRepo
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo
import com.dinodevs.greatfitwatchface.widget.MainClock
import com.dinodevs.greatfitwatchface.widget.NewStepsWidget
import com.dinodevs.greatfitwatchface.widget.WidgetConstants
import com.huami.watch.watchface.AbstractSlptClock
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

/**
 * Amazfit watch faces
 */
class GreatFit : AbstractWatchFace() {
    override fun onCreate() {
        val context = this.applicationContext

        val batteryLevelRepo = BatteryLevelRepo { 74 }
        val caloriesRepo = CaloriesRepo { 50 }
        val distanceRepo = TodayDistanceRepo { 2.01f }
        this.clock = MainClock(batteryLevelRepo, caloriesRepo, distanceRepo)

        val factory = MetaWidgetsFactory(context)
        val metaWidgets = factory.createWidgets()
        widgets.addAll(metaWidgets)

        notifyStatusBarPosition(0f, 175f)

        super.onCreate()
    }

    override fun slptClockClass(): Class<out AbstractSlptClock> {
        return GreatFitSlpt::class.java
    }
}