package com.generalmobi.digitalspeedlib

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat


/**
 * Created by Riccardo on 13/01/2017.
 */

class DigitSpeedView : RelativeLayout {
    private var unit = "Km/h"
    private var speedTextSize = dpTOpx(40f)
    private var unitTextSize = dpTOpx(10f)
    /**
     *
     * @return current speed
     */
    var speed = 0
        private set
    private var speedTextColor: Int = 0
    private var unitTextColor: Int = 0
    private var backgoundColor = Color.BLACK
    private var mSpeedBgTextView: TextView? = null
    private var mSpeedTextView: TextView? = null
    private var mSpeedUnitTextView: TextView? = null
    private var mainLayout: RelativeLayout? = null
    private var showUnit = true
    private var onSpeedChangeListener: OnSpeedChangeListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        initAttributeSet(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
        initAttributeSet(context, attrs)
    }

    private fun init(context: Context) {
        speedTextColor = ContextCompat.getColor(context, R.color.green)
        unitTextColor = ContextCompat.getColor(context, R.color.green)
        val rootView = LayoutInflater.from(context).inflate(R.layout.digit_speed_view, this, true)
        mainLayout = rootView.findViewById<View>(R.id.digit_speed_main) as RelativeLayout
        mSpeedTextView = rootView.findViewById<View>(R.id.digit_speed) as TextView
        mSpeedBgTextView = rootView.findViewById<View>(R.id.digit_speed_bg) as TextView
        mSpeedUnitTextView = rootView.findViewById<View>(R.id.digit_speed_unit) as TextView
        val tf = Typeface.createFromAsset(resources.assets, "fonts/digital-7_mono.ttf")
        mSpeedTextView?.typeface = tf
        mSpeedBgTextView?.typeface = tf
        mSpeedUnitTextView?.typeface = tf
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DigitSpeedView, 0, 0)
        speedTextSize = a.getDimension(R.styleable.DigitSpeedView_speedTextSize, speedTextSize)
        unitTextSize = a.getDimension(R.styleable.DigitSpeedView_unitTextSize, unitTextSize)
        speedTextColor = a.getColor(R.styleable.DigitSpeedView_speedTextColor, speedTextColor)
        unitTextColor = a.getColor(R.styleable.DigitSpeedView_unitTextColor, unitTextColor)
        showUnit = a.getBoolean(R.styleable.DigitSpeedView_showUnit, showUnit)
        if (!showUnit) {
            mSpeedUnitTextView?.visibility = View.GONE
        }
        speed = a.getInt(R.styleable.DigitSpeedView_speed, speed)
        backgoundColor = a.getColor(R.styleable.DigitSpeedView_backgroundColor, backgoundColor)
        val unit = a.getString(R.styleable.DigitSpeedView_unit)
        this.unit = unit ?: this.unit
        if (a.getBoolean(R.styleable.DigitSpeedView_disableBackgroundImage, false)) {
            mainLayout?.setBackgroundResource(0)
            mainLayout?.setBackgroundColor(backgoundColor)
        } else {
            val drawable = a.getDrawable(R.styleable.DigitSpeedView_backgroundDrawable)
            if (drawable != null) {
                mainLayout?.background = drawable
            }
        }
        a.recycle()
        initAttributeValue()
    }

    private fun initAttributeValue() {
        mSpeedTextView?.setTextColor(speedTextColor)
        mSpeedTextView?.text = "$speed"
        mSpeedTextView?.setShadowLayer(20f, 0f, 0f, speedTextColor)
        mSpeedTextView?.textSize = speedTextSize
        mSpeedBgTextView?.textSize = speedTextSize
        mSpeedUnitTextView?.text = unit
        mSpeedUnitTextView?.setTextColor(unitTextColor)
        mSpeedUnitTextView?.setShadowLayer(20f, 0f, 0f, unitTextColor)
        mSpeedUnitTextView?.textSize = unitTextSize

    }

    /**
     * update speed
     * @param speed to update
     */
    fun updateSpeed(speed: Any) {
        if (speed is Int) {
            val isSpeedUp = speed > this.speed
            this.speed = speed
            showUnit()
            mSpeedTextView?.text = "$speed"
            if (onSpeedChangeListener != null) {
                onSpeedChangeListener?.onSpeedChange(this, isSpeedUp)
            }
        }else if (speed is String){
            this.speed = 0
            hideUnit()
            mSpeedTextView?.text = speed
        }
    }

    /**
     * Show unit text
     */
    fun showUnit() {
        mSpeedUnitTextView?.visibility = View.VISIBLE
    }

    /**
     * Hide unit text
     */
    fun hideUnit() {
        mSpeedUnitTextView?.visibility = View.GONE
    }

    /**
     * convert dp to **pixel**.
     * @param dp to convert.
     * @return Dimension in pixel.
     */
    fun dpTOpx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    /**
     * convert pixel to **dp**.
     * @param px to convert.
     * @return Dimension in dp.
     */
    fun pxTOdp(px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    /**
     * Register a callback to be invoked when speed value changed (in integer).
     * @param onSpeedChangeListener maybe null, The callback that will run.
     */
    fun setOnSpeedChangeListener(onSpeedChangeListener: OnSpeedChangeListener) {
        this.onSpeedChangeListener = onSpeedChangeListener
    }

}
