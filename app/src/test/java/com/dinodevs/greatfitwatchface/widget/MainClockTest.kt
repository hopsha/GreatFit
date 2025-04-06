package com.dinodevs.greatfitwatchface.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.dinodevs.greatfitwatchface.MetaWidgetsFactory
import com.dinodevs.greatfitwatchface.WatchfaceRoborazziTest
import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo
import com.dinodevs.greatfitwatchface.data.CaloriesRepo
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.StepsRepo
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo
import org.junit.Test

class MainClockTest : WatchfaceRoborazziTest() {

    private val batteryLevelRepo = BatteryLevelRepo { 74 }
    private val caloriesRepo = CaloriesRepo { 50 }
    private val distanceRepo = TodayDistanceRepo { 2.01f }

    private val mainClock by lazy {
        val context = ApplicationProvider.getApplicationContext<Context>()
        MainClock(batteryLevelRepo, caloriesRepo, distanceRepo).apply {
            init(context)
        }
    }
    private val widgets by lazy {
        val factory = MetaWidgetsFactory(ApplicationProvider.getApplicationContext())
        factory.createWidgets().apply {
            firstOrNull {
                it.dataTypes.contains(DataType.STEPS)
            }?.onDataUpdate(DataType.STEPS, 7153)
        }
    }

    @Test
    fun test() {
        verifyScreenshot { context ->
            val imageView = ImageView(context)
            val bitmap = Bitmap.createBitmap(320, 320, Bitmap.Config.ARGB_8888)
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
}