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
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.Steps
import com.dinodevs.greatfitwatchface.resource.ResourceManager
import com.dinodevs.greatfitwatchface.slpt.AlignX
import com.dinodevs.greatfitwatchface.slpt.AlignY
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import kotlin.math.roundToInt

class NewStepsWidget(
    context: Context,
    private val startX: Int,
    private val startY: Int,
) : AbstractWidget() {

    private var latestData: Steps? = null
    private val iconBitmap = Util.decodeImage(context.resources, ASSET_ICON_HIGH_RES)
    private val textTypeface = ResourceManager.getTypeFace(context.resources, ResourceManager.Font.GoogleSansMedium)
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

    override fun buildSlptViewComponent(service: Service): MutableList<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    override fun buildSlptViewComponent(
        service: Service,
        better_resolution: Boolean
    ): MutableList<SlptViewComponent> {
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
            setRect(
                WidgetConstants.Meta.ICON_SIZE.roundToInt(),
                WidgetConstants.Meta.ICON_SIZE.roundToInt()
            )
        }
        result.add(icon)

        val textContainer = SlptLinearLayout().apply {
            add(SlptTodayStepNumView())
            setTextAttrForAll(
                WidgetConstants.Meta.TEXT_SIZE,
                WidgetConstants.COLOR_TEXT,
                textTypeface,
            )
            alignX = AlignX.LEFT.slptValue
            alignY = AlignY.CENTER.slptValue
            setStart(
                (startX + WidgetConstants.Meta.ICON_SIZE + WidgetConstants.Meta.MARGIN_LEFT).roundToInt(),
                startY,
            )
            setRect(
                (160 - WidgetConstants.Meta.MARGIN_LEFT).roundToInt(),
                WidgetConstants.Meta.ICON_SIZE.roundToInt()
            )
        }
        result.add(textContainer)

        return result
    }

    override fun draw(
        canvas: Canvas,
        width: Float,
        height: Float,
        centerX: Float,
        centerY: Float
    ) {
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
            (latestData?.steps ?: 0).toString(),
            startX + WidgetConstants.Meta.ICON_SIZE + WidgetConstants.Meta.MARGIN_LEFT,
            startY - fontTopOffset,
            textPaint
        )
    }

    override fun getDataTypes(): MutableList<DataType> = mutableListOf(DataType.STEPS)

    override fun onDataUpdate(type: DataType, value: Any) {
        latestData = value as? Steps
    }

    companion object {
        private const val ASSET_ICON_HIGH_RES = "icons/steps.png"
        private const val ASSET_ICON_26WC = "26wc_icons/steps.png"
        private const val ASSET_ICON_SLPT = "slpt_icons/steps.png"
    }
}