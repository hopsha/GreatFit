package com.dinodevs.greatfitwatchface

import android.content.Context
import com.dinodevs.greatfitwatchface.widget.NewStepsWidget
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

                MetadataItem.DISTANCE -> {}
                MetadataItem.CALORIES -> {}
                MetadataItem.BATTERY -> {}
            }
        }

        return widgets
    }
}