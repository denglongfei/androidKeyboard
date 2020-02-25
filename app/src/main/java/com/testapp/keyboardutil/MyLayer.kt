package com.testapp.keyboardutil

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding

class MyLayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    private var mContainer: ConstraintLayout? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mContainer = this.parent as ConstraintLayout
        initData()
    }

    private fun initData() {
        val views = getViews(mContainer)
        anim.duration = 300
        anim.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            views.map { view ->
                when (view) {
                    is ViewDataBinding -> {
                        view.root.translationY = value
                    }
                    is MyFlow -> {
                        view.translationY = value
                    }
                    else -> {
                        view.translationY = value
                    }
                }
            }
        }

    }




    private val anim = ValueAnimator()

    //是否展开
    val isExpand: Boolean
        get() = previousY != 0f

    private var previousY = 0f //上次平移距离
    override fun setTranslationY(translationY: Float) {
        mContainer?.also {
            anim.setFloatValues(previousY, translationY)
            previousY = translationY
            anim.start()
        }
    }


}