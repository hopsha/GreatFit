package com.dinodevs.greatfitwatchface

import android.content.Context
import com.dinodevs.greatfitwatchface.widget.NewBatteryWidget
import com.dinodevs.greatfitwatchface.widget.NewCaloriesWidget
import com.dinodevs.greatfitwatchface.widget.NewStepsWidget
import com.dinodevs.greatfitwatchface.widget.NewTodayDistanceWidget
import com.dinodevs.greatfitwatchface.widget.Widget
import com.dinodevs.greatfitwatchface.widget.WidgetConstants
import kotlin.math.roundToInt

class MetaWidgetsFactory(
    private val context: Context
) {
    fun createWidgets(): List<Widget> {
        val widgets = mutableListOf<Widget>()

        val metaX = 160
        MetadataItem.entries.forEachIndexed { index, metadataItem ->
            val extraTopOffset: Float =
                index * (WidgetConstants.Meta.MARGIN_INTERLINE + WidgetConstants.Meta.ICON_SIZE)
            val y = WidgetConstants.Meta.MARGIN_TOP + extraTopOffset
            when (metadataItem) {
                MetadataItem.STEPS -> {
                    val widget = NewStepsWidget(
                        context = context,
                        startX = metaX,
                        startY = y.roundToInt(),
                    )
                    widgets.add(widget)
                }

                MetadataItem.DISTANCE -> {
                    val widget = NewTodayDistanceWidget(
                        context = context,
                        startX = metaX,
                        startY = y.roundToInt(),
                    )
                    widgets.add(widget)
                }
                MetadataItem.CALORIES -> {
                    val widget = NewCaloriesWidget(
                        context = context,
                        startX = metaX,
                        startY = y.roundToInt(),
                    )
                    widgets.add(widget)
                }
                MetadataItem.BATTERY -> {
                    val widget = NewBatteryWidget(
                        context = context,
                        startX = metaX,
                        startY = y.roundToInt(),
                    )
                    widgets.add(widget)
                }
            }
        }

        return widgets
    }
}