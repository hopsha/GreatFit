package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import com.dinodevs.greatfitwatchface.data.Battery
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.resource.ResourceManager
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptPowerNumView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import kotlin.math.roundToInt

class NewBatteryWidget(
    context: Context,
    private val startX: Int,
    private val startY: Int,
) : AbstractWidget() {

    private var latestData: Battery? = null
    private val iconBitmap = Util.decodeImage(context.resources, ASSET_ICON_HIGH_RES)
    private val textTypeface =
        ResourceManager.getTypeFace(context.resources, ResourceManager.Font.GoogleSansMedium)
    private val textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG).apply {
        typeface = textTypeface
        textSize = WidgetConstants.Meta.TEXT_SIZE
        color = WidgetConstants.COLOR_TEXT
        textAlign = Paint.Align.LEFT
    }
    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        colorFilter = PorterDuffColorFilter(WidgetConstants.COLOR_ACCENT, PorterDuff.Mode.SRC_IN)
    }
    private val fontTopOffset = textPaint.fontMetrics.top

    // Draw screen-on
    override fun draw(canvas: Canvas, width: Float, height: Float, centerX: Float, centerY: Float) {
        canvas.drawBitmap(
            iconBitmap,
            Rect(0, 0, iconBitmap.width, iconBitmap.height),
            RectF(
                startX.toFloat(),
                startY.toFloat(),
                startX + WidgetConstants.Meta.ICON_SIZE,
                startY + WidgetConstants.Meta.ICON_SIZE
            ),
            iconPaint,
        )
        canvas.drawText(
            "${latestData?.percentage() ?: 0}%",
            startX + WidgetConstants.Meta.ICON_SIZE + WidgetConstants.Meta.MARGIN_LEFT,
            startY - fontTopOffset,
            textPaint
        )
    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(
        service: Service,
        better_resolution: Boolean
    ): List<SlptViewComponent> {
        val result = mutableListOf<SlptViewComponent>()

        val icon = SlptPictureView().apply {
            val assetPath = if (better_resolution) {
                ASSET_ICON_26WC
            } else {
                ASSET_ICON_SLPT
            }
            val iconBytes = SimpleFile.readFileFromAssets(service, assetPath)
            setImagePicture(iconBytes)
            setStart(startX, startY)
        }
        result.add(icon)

        val textContainer = SlptLinearLayout().apply {
            add(SlptPowerNumView())
            add(
                SlptPictureView().apply {
                    setStringPicture("%")
                }
            )

            setTextAttrForAll(
                WidgetConstants.Meta.TEXT_SIZE,
                WidgetConstants.COLOR_TEXT,
                textTypeface,
            )
            alignX = 2
            alignY = 0
            setStart(
                (startX + WidgetConstants.Meta.ICON_SIZE + WidgetConstants.Meta.MARGIN_LEFT).roundToInt(),
                (startY - fontTopOffset).roundToInt(),
            )
        }
        result.add(textContainer)

        return result
    }

    override fun getDataTypes(): MutableList<DataType> = mutableListOf(DataType.BATTERY)

    override fun onDataUpdate(type: DataType, value: Any) {
        latestData = value as? Battery
    }

    private fun Battery.percentage(): Int {
        return ((level.toFloat() / scale) * 100).roundToInt().coerceIn(0, 100)
    }

    companion object {
        private const val ASSET_ICON_HIGH_RES = "icons/battery.png"
        private const val ASSET_ICON_26WC = "26wc_icons/battery.png"
        private const val ASSET_ICON_SLPT = "slpt_icons/battery.png"
    }
}
