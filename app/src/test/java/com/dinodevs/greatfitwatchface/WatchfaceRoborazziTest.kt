package com.dinodevs.greatfitwatchface

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dinodevs.greatfitwatchface.test.TestActivity
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
@Config(qualifiers = "w320dp-h300dp")
open class WatchfaceRoborazziTest {

    @get:Rule val activityRule = ActivityScenarioRule(TestActivity::class.java)
    @get:Rule val roborazziRule = RoborazziRule(
        captureRoot = Espresso.onView(ViewMatchers.isRoot()),
        options = RoborazziRule.Options(
            outputDirectoryPath = "src\\test\\screenshots\\",
            captureType = RoborazziRule.CaptureType.LastImage(),
        )
    )

    fun verifyScreenshot(viewProvider: (Context) -> View) {
        activityRule.scenario.onActivity { activity ->
            val view = viewProvider(activity)
            val container = FrameLayout(activity)
            container.addView(view)

            val maskView = ImageView(activity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            val maskBitmap = Bitmap.createBitmap(320, 300, Bitmap.Config.ARGB_8888)
            val maskCanvas = Canvas(maskBitmap)
            maskCanvas.drawColor(Color.WHITE)
            val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                color = Color.BLACK
            }
            maskCanvas.drawCircle(
                160f,
                160f,
                160f,
                maskPaint,
            )
            maskView.setImageBitmap(maskBitmap)
            container.addView(maskView)

            activity.setContentView(container)
        }
    }
}