package sdumchykov.presentation.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import sdumchykov.R

private const val RADIUS_VALUE = 4F
private const val ICON_SIDE_VALUE =
    18F   //https://developers.google.com/identity/branding-guidelines#padding
private const val BETWEEN_LOGO_AND_TEXT =
    24F  //https://developers.google.com/identity/branding-guidelines#padding
private const val GOOGLE = "GOOGLE"
private const val HEIGHT = 36F

class GoogleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val text = GOOGLE
    val fontFamily = ResourcesCompat.getFont(context, R.font.roboto_regular)

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }
    private val rect = Rect()
    val radius = floatToDP(RADIUS_VALUE)  // Radius of the rounded corner.
    private val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_google_logo, null)!!
    private val iconSide = floatToDP(ICON_SIDE_VALUE).toInt()

    init {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(Color.WHITE)
        shape.cornerRadius = radius
        background = shape  //background is rounded rectangle

        paint.getTextBounds(text, 0, text.lastIndex, rect)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            drawLogo(canvas)
            drawText(canvas)
        }

    }

    private fun drawText(canvas: Canvas) {
        paint.typeface = fontFamily
        val yPos = (height - paint.descent() - paint.ascent()) / 2
        canvas.drawText(
            text, (measuredWidth + iconSide + floatToDP(BETWEEN_LOGO_AND_TEXT)) / 2, yPos, paint
        )
    }

    private fun drawLogo(canvas: Canvas) {
        val startX =
            (measuredWidth - iconSide - floatToDP(BETWEEN_LOGO_AND_TEXT) - text.width()) / 2
        icon.setBounds(
            startX.toInt(),
            (measuredHeight - iconSide) / 2,
            (startX + iconSide).toInt(),
            (measuredHeight + iconSide) / 2
        )
        icon.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeight = floatToDP(HEIGHT).toInt()

        super.onMeasure(
            widthMeasureSpec, MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        )

    }

    private fun floatToDP(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics
        )
    }

    private fun String.width(): Float {
        return paint.measureText(this)
    }

}
