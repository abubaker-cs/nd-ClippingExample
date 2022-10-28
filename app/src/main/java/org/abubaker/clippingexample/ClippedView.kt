package org.abubaker.clippingexample

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class ClippedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val paint = Paint().apply {

        // Enable anti-aliasing: Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true

        // Stroke Width: 4dp | 6dp
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)

        // Font size: 18sp
        textSize = resources.getDimension(R.dimen.textSize)

    }

    // To store locally the path of what has been drawn.
    private val path = Path()

    /**
     * Variables for dimensions for a clipping rectangle around the whole set of shape
     */

    // Right/Bottom: 90dp | 120dp
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)

    // Top/Left: 0dp
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    /**
     * inset / offset
     */

    // Inset of a rectangle
    private val rectInset = resources.getDimension(R.dimen.rectInset)

    // offset of a small rectangle
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    /**
     * Inside the Rectangle: Radius of a circle
     */
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    /**
     * Inside the Rectangle: Offset / Text Size
     */
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    /**
     * Coordinates for two columns
     */
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    /**
     * Coordinates for each row
     */

    // 8dp | 10dp
    private val rowOne = rectInset

    // = (8dp | 10dp) + (8dp | 10dp) +  (90dp | 120dp)
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom

    // Final row for the transformed text
    // = ((8dp | 10dp) + (8dp | 10dp) +  (90dp | 120dp)) + (1.5f * (90dp | 120dp))
    private val textRow = rowFour + (1.5f * clipRectBottom)

}
