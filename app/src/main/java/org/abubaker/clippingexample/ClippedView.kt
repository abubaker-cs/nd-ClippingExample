package org.abubaker.clippingexample

import android.content.Context
import android.graphics.*
import android.os.Build
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 01 - Back & Un-clipped
        drawBackAndUnclippedRectangle(canvas)

        // 02 - Difference Clipping
        drawDifferenceClippingExample(canvas)

        // 03 - Circular Clipping
        drawCircularClippingExample(canvas)

        // 04 - Intersection  Clipping
        drawIntersectionClippingExample(canvas)

        // 05 - Combined Clipping
        drawCombinedClippingExample(canvas)

        // 06 - Rounded Rectangle Clipping
        drawRoundedRectangleClippingExample(canvas)

        // 07 - Outside Clipping
        drawOutsideClippingExample(canvas)

        // 08 - Skewed Text
        drawSkewedTextExample(canvas)

        // 09 - Translated Text
        drawTranslatedTextExample(canvas)

        // 10 - Quick Reject
        drawQuickRejectExample(canvas)
    }

    /**
     * 00 - Clipped Rectangle
     */
    private fun drawClippedRectangle(canvas: Canvas) {

        /**
         * 01
         * Set the boundaries of the clipping rectangle for the whole shape
         * Apply a clipping rectangle that constrains to drawing only the square
         */

        // Left, Top, Right, Bottom
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom
        )

        // Fill: White Color
        // Add code to fill the canvas with white color. Only the region inside the clipping rectangle will be filled!
        canvas.drawColor(Color.WHITE)

        /**
         * 02
         * Change the color to red and draw a diagonal line inside the clipping rectangle.
         */

        paint.color = Color.RED

        // Left, Top, Right, Bottom, Paint
        canvas.drawLine(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom, paint
        )

        /**
         * 03
         * Set the color to green and draw a circle inside the clipping rectangle.
         */
        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius, clipRectBottom - circleRadius,
            circleRadius, paint
        )

        /**
         * 04 Text
         * Set the color to blue and draw text aligned with the right edge of the clipping rectangle.
         * Use Canvas.drawText() to draw text.
         */
        paint.color = Color.BLUE
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping),
            clipRectRight, textOffset, paint
        )


    }

    /**
     * 01 - Back & Un-clipped
     */
    private fun drawBackAndUnclippedRectangle(canvas: Canvas) {

        // Color of the BORDER / PATH
        canvas.drawColor(Color.GRAY)

        // Save the canvas
        //
        canvas.save()

        // Translate to the first row and column position.
        // Column: 1
        // Row: 1
        canvas.translate(columnOne, rowOne)

        // Draw by calling drawClippedRectangle()
        drawClippedRectangle(canvas)

        // Then restore the canvas to its previous state
        //
        canvas.restore()
    }

    /**
     * 02 - Difference Clipping
     */
    private fun drawDifferenceClippingExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Translate the origin of the canvas into open space to the first row, second column, to the right of the first rectangle.
        // Column: 2
        // Row: 1
        canvas.translate(columnTwo, rowOne)

        // Use the subtraction of two clipping rectangles to create a frame.
        // Apply two clipping rectangles. The DIFFERENCE operator subtracts the second rectangle from the first one.
        canvas.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )

        // The method clipRect(float, float, float, float, Region.Op
        // .DIFFERENCE) was deprecated in API level 26. The recommended
        // alternative method is clipOutRect(float, float, float, float),
        // which is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)

        // The method clipRect(float, float, float, float, Region.Op.DIFFERENCE) was deprecated in API level 26.
            canvas.clipRect(
                4 * rectInset, 4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        else {

            // The recommended alternative method is clipOutRect(float, float, float, float),
            // which is currently available in API level 26 and higher.
            canvas.clipOutRect(
                4 * rectInset, 4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset
            )

        }

        // Draw the modified canvas.
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 03 - Circular Clipping
     */
    private fun drawCircularClippingExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Column: 1
        // Row: 2
        canvas.translate(columnOne, rowTwo)

        // Clears any lines and curves from the path but unlike reset(),
        // keeps the internal data structure for faster reuse.
        path.rewind()

        path.addCircle(
            circleRadius, clipRectBottom - circleRadius,
            circleRadius, Path.Direction.CCW
        )

        // The method clipPath(path, Region.Op.DIFFERENCE) was deprecated in
        // API level 26. The recommended alternative method is
        // clipOutPath(Path), which is currently available in
        // API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(path)
        }

        // Draw the modified canvas.
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 04 - Intersection Clipping
     */
    private fun drawIntersectionClippingExample(canvas: Canvas) {
    }

    /**
     * 05 - Combined Clipping
     */
    private fun drawCombinedClippingExample(canvas: Canvas) {
    }

    /**
     * 06 - Rounded Rectangle Clipping
     */
    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {
    }

    /**
     * 07 - Outside Clipping
     */
    private fun drawOutsideClippingExample(canvas: Canvas) {
    }

    /**
     * 08 - Translated Text
     */
    private fun drawTranslatedTextExample(canvas: Canvas) {
    }

    /**
     * 09 - Skewed Text
     */
    private fun drawSkewedTextExample(canvas: Canvas) {
    }

    /**
     * 10 - Quick Reject
     */
    private fun drawQuickRejectExample(canvas: Canvas) {
    }


}
