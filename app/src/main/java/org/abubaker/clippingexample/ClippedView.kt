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

    // RectF is a class that holds rectangle coordinates in floating point.
    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )

    //
    private val rejectRow = rowFour + rectInset + 2 * clipRectBottom


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

        // Circular path
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

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Column: 2
        // Row: 2
        canvas.translate(columnTwo, rowTwo)

        //
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )

        // The method clipRect(float, float, float, float, Region.Op
        // .INTERSECT) was deprecated in API level 26. The recommended
        // alternative method is clipRect(float, float, float, float), which
        // is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight, clipRectBottom,
                Region.Op.INTERSECT
            )

        } else {

            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight, clipRectBottom
            )

        }

        // Draw the modified canvas.
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 05 - Combined Clipping
     */
    private fun drawCombinedClippingExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Column: 1
        // Row: 3
        canvas.translate(columnOne, rowThree)

        //
        path.rewind()

        //
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius, Path.Direction.CCW
        )

        //
        path.addRect(
            clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset, Path.Direction.CCW
        )

        //
        canvas.clipPath(path)

        // Draw the modified canvas.
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 06 - Rounded Rectangle Clipping
     */
    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Column: 2
        // Row: 3
        canvas.translate(columnTwo, rowThree)

        //
        path.rewind()

        // 1. The addRoundRect() function takes a rectangle.
        // 2. Values for the x and y values of the corner radius.
        // 3. The direction to wind the round-rectangle's contour.
        // 4. Path.Direction specifies how closed shapes (e.g. rects, ovals) are oriented when
        //    they are added to a path. CCW stands for counter-clockwise.
        path.addRoundRect(
            rectF, clipRectRight / 4,
            clipRectRight / 4, Path.Direction.CCW
        )

        //
        canvas.clipPath(path)

        //
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 07 - Outside Clipping
     */
    private fun drawOutsideClippingExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        // Move the origin to the right for the next rectangle.
        // Column: 1
        // Row: 4
        canvas.translate(columnOne, rowFour)

        //
        canvas.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )

        //
        drawClippedRectangle(canvas)

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 08 - Translated Text
     */
    private fun drawTranslatedTextExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        //
        paint.color = Color.GREEN

        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.LEFT

        // Move the origin to the right for the next rectangle.
        // Column: 2
        // Row: Text Row
        canvas.translate(columnTwo, textRow)

        // Draw text.
        canvas.drawText(
            context.getString(R.string.translated),
            clipRectLeft, clipRectTop, paint
        )

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 09 - Skewed Text
     */
    private fun drawSkewedTextExample(canvas: Canvas) {

        // Save the canvas.
        canvas.save()

        //
        paint.color = Color.YELLOW

        //
        paint.textAlign = Paint.Align.RIGHT

        // Move the origin to the right for the next rectangle.
        // Column: 2
        // Row: Text Row
        canvas.translate(columnTwo, textRow)

        // Apply skew transformation.
        canvas.skew(0.2f, 0.3f)

        //
        canvas.drawText(
            context.getString(R.string.skewed),
            clipRectLeft, clipRectTop, paint
        )

        // Restore the canvas state.
        canvas.restore()

    }

    /**
     * 10 - Quick Reject
     */
    private fun drawQuickRejectExample(canvas: Canvas) {

        val inClipRectangle = RectF(
            clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2
        )

        val notInClipRectangle = RectF(
            RectF(
                clipRectRight + 1,
                clipRectBottom + 1,
                clipRectRight * 2,
                clipRectBottom * 2
            )
        )

        // Save the canvas.
        canvas.save()

        // Position
        // Column: 1
        // Row: Reject Row
        canvas.translate(columnOne, rejectRow)

        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom
        )

        if (canvas.quickReject(
                inClipRectangle, Canvas.EdgeType.AA
            )
        ) {
            canvas.drawColor(Color.WHITE)
        } else {
            canvas.drawColor(Color.BLACK)
            canvas.drawRect(
                inClipRectangle, paint
            )
        }

        // Restore the canvas state.
        canvas.restore()

    }


}

/**
 * Note:
 * ====
 * 1. When you use View classes provided by the Android system, the system clips views for you to
 *    minimize overdraw.
 *
 * 2. When you use custom View classes and override the onDraw() method, clipping what you
 *    draw becomes your responsibility.
 */
