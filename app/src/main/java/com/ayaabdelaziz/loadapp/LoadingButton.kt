package com.ayaabdelaziz.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = ""
    private var loadingWidth = 0f
    private var loadingAngle = 0f
    private var btnColor = 0
    private var btnTextColor = 0
    private var circleColor = 0
    private val paintBtn = Paint()
    private val paintCircle = Paint()
    private val paintBtnText = Paint().apply {
        textSize = 55f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }


    private var valueAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                // dont know how to add that progress number in here
                buttonText = "We are loading"
                // btn animation
                valueAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat())
                    .apply {
                        duration = 2000
                        repeatMode = ValueAnimator.RESTART
                        repeatCount = ValueAnimator.INFINITE
                        addUpdateListener {
                            loadingWidth = animatedValue as Float
                            this@LoadingButton.invalidate()
                        }
                        start()
                    }
                // circle animation
                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = 1500
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }

                    start()

                }
            }
            ButtonState.Completed -> {
                buttonText = "Download"
                loadingWidth = 0f
                loadingAngle = 0f
                valueAnimator.end()
                circleAnimator.end()
                invalidate()
            }
            else -> {}
        }
    }


    init {

        isClickable = true
        buttonText = "Download"
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0).apply {
            btnColor = getColor(R.styleable.LoadingButton_btnBgColor, 0)
            btnTextColor = getColor(R.styleable.LoadingButton_btnTextColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleBgColor, 0)
        }
        paintBtn.color = btnColor
        paintBtnText.color = btnTextColor
        paintCircle.color = circleColor

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        paintBtn.color = btnColor
        super.onDraw(canvas)
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintBtn)

        paintBtn.color = Color.parseColor("#1B1F35")
        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), paintBtn)

        canvas.drawText(
            buttonText,
            measuredWidth.toFloat() / 2,
            measuredHeight / 1.7f,
            paintBtnText
        )
        canvas.drawArc(
            measuredWidth - 100f,
            (measuredHeight / 2) - 30f,
            measuredWidth - 50f,
            (measuredHeight / 2) + 30f,
            0f, loadingAngle, true, paintCircle
        )
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun setBtnStatus(state: ButtonState) {
        buttonState = state
    }

}