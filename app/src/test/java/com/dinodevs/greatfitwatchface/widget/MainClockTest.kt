package com.dinodevs.greatfitwatchface.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.dinodevs.greatfitwatchface.MetaWidgetsFactory
import com.dinodevs.greatfitwatchface.WatchfaceRoborazziTest
import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo
import com.dinodevs.greatfitwatchface.data.Calories
import com.dinodevs.greatfitwatchface.data.CaloriesRepo
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.Steps
import com.dinodevs.greatfitwatchface.data.TodayDistance
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo
import org.junit.Test

class MainClockTest : WatchfaceRoborazziTest() {

    private val batteryLevelRepo = BatteryLevelRepo { 74 }

    private val mainClock by lazy {
        val context = ApplicationProvider.getApplicationContext<Context>()
        MainClock(batteryLevelRepo).apply {
            init(context)
        }
    }
    private val widgets by lazy {
        val factory = MetaWidgetsFactory(ApplicationProvider.getApplicationContext())
        factory.createWidgets().apply {
            updateData(DataType.STEPS, Steps(7153, 10000))
            updateData(DataType.DISTANCE, TodayDistance(2.01))
            updateData(DataType.CALORIES, Calories(50))
        }
    }

    @Test
    fun test() {
        verifyScreenshot { context ->
            val imageView = ImageView(context)
            val bitmap = Bitmap.createBitmap(320, 300, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            mainClock.onDrawDigital(
                canvas,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                canvas.width / 2f,
                canvas.height / 2f,
                12,
                42,
                15,
                2025,
                5,
                6,
                1,
                1,
            )
            widgets.forEach { widget ->
                widget.draw(
                    canvas,
                    canvas.width.toFloat(),
                    canvas.height.toFloat(),
                    canvas.width / 2f,
                    canvas.height / 2f,
                )
            }
            imageView.setImageBitmap(bitmap)
            imageView
        }
    }

    private fun List<Widget>.updateData(type: DataType, data: Any) {
        filter {
            it.dataTypes.contains(type)
        }.forEach {
            it.onDataUpdate(type, data)
        }
    }
}