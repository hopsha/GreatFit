package com.dinodevs.greatfitwatchface

import android.content.Context
import android.content.Intent
import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo
import com.dinodevs.greatfitwatchface.data.CaloriesRepo
import com.dinodevs.greatfitwatchface.data.StepsRepo
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.widget.MainClock
import com.dinodevs.greatfitwatchface.widget.NewStepsWidget
import com.dinodevs.greatfitwatchface.widget.WidgetConstants
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout
import com.ingenic.iwds.slpt.view.core.SlptLayout
import kotlin.math.roundToInt

/**
 * Splt version of the watch.
 */
class GreatFitSlpt : AbstractWatchFaceSlpt() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val context = this.applicationContext

        val batteryLevelRepo = BatteryLevelRepo { 74 }
        val caloriesRepo = CaloriesRepo { 50 }
        this.clock = MainClock(batteryLevelRepo, caloriesRepo)

        val factory = MetaWidgetsFactory(context)
        val metaWidgets = factory.createWidgets()
        widgets.addAll(metaWidgets)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun createClockLayout26WC(): SlptLayout {
        val result = SlptAbsoluteLayout()
        for (component in clock.buildSlptViewComponent(this, true)) {
            result.add(component)
        }
        for (widget in widgets) {
            for (component in widget.buildSlptViewComponent(this, true)) {
                result.add(component)
            }
        }

        return result
    }

    override fun createClockLayout8C(): SlptLayout {
        val result = SlptAbsoluteLayout()
        for (component in clock.buildSlptViewComponent(this)) {
            result.add(component)
        }
        for (widget in widgets) {
            for (component in widget.buildSlptViewComponent(this)) {
                result.add(component)
            }
        }

        return result
    }

    override fun initWatchFaceConfig() {
        //Log.w("DinoDevs-GreatFit", "Initiating watchface");
    }

    override fun isClockPeriodSecond(): Boolean {
        val context = this.applicationContext
        val needRefreshSecond = Util.needSlptRefreshSecond(context)
        if (needRefreshSecond) {
            this.isClockPeriodSecond = true
        }
        return needRefreshSecond
    }
}