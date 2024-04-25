package sdumchykov.androidApp.presentation.custom

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
import sdumchykov.androidApp.R

private const val RADIUS_VALUE = 4F
private const val ICON_SIDE_VALUE =
    18F // https://developers.google.com/identity/branding-guidelines#padding
private const val BETWEEN_LOGO_AND_TEXT =
    24F // https://developers.google.com/identity/branding-guidelines#padding
private const val GOOGLE = "GOOGLE"
// private const val HEIGHT = 36F

class GoogleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val text = GOOGLE
    val fontFamily = ResourcesCompat.getFont(context, R.font.roboto_regular)

    private val paint = Paint()
    private val rect = Rect()
    val radius = RADIUS_VALUE.toDP() // Radius of the rounded corner.
    private val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_google_logo, null)
    private val iconSide = ICON_SIDE_VALUE.toDP().toInt()

    //region Text dimensions
    private var textX = 0F
    private var textY = 0F
    //endregion

    //region Logo dimensions
    private var left = 0
    private var top = 0
    private var right = 0
    private var bottom = 0
    //endregion

    init {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(Color.WHITE)
        shape.cornerRadius = radius
        background = shape // background is rounded rectangle

        paint.apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
            typeface = fontFamily
        }

        paint.getTextBounds(text, 0, text.lastIndex, rect)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        textX = (measuredWidth + iconSide + BETWEEN_LOGO_AND_TEXT.toDP()) / 2
        textY = (height - paint.descent() - paint.ascent()) / 2

        this.left =
            ((measuredWidth - iconSide - BETWEEN_LOGO_AND_TEXT.toDP() - text.width()) / 2).toInt()
        this.top = (measuredHeight - iconSide) / 2
        this.right = this.left + iconSide
        this.bottom = (measuredHeight + iconSide) / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawLogo(canvas, left, top, right, bottom)
        drawText(canvas, textX, textY)
    }

    private fun drawLogo(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        icon?.setBounds(left, top, right, bottom)
        icon?.draw(canvas)
    }

    private fun drawText(canvas: Canvas, x: Float, y: Float) {
        canvas.drawText(text, x, y, paint)
    }

    private fun Float.toDP(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
        )
    }

    private fun String.width(): Float {
        return paint.measureText(this)
    }
}
