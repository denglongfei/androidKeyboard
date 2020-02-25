package com.testapp.keyboardutil

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout

class MyFlow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Flow(context, attrs, defStyleAttr) {


    override fun setTranslationY(translationY: Float) {
        val mContainer = this.parent as ConstraintLayout
        getViews(mContainer).map { v ->
            v.translationY = translationY
        }
        super.setTranslationY(translationY)
    }


    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        val mContainer = this.parent as ConstraintLayout
        getViews(mContainer).map { v ->
            v.visibility = visibility
        }
    }





}