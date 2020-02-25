package com.testapp.keyboardutil

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow

/**
 * 键盘高度测量
 */
class KeyboardHeightProvider(view: View, val function: (keyboardHeight: Int) -> Unit) :
    PopupWindow(view.context),
    ViewTreeObserver.OnGlobalLayoutListener {

    //当前PopupWindow最大的显示高度
    private var maxHeight = 0

    init {
        contentView = View(view.context)
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        //设置背景
        setBackgroundDrawable(ColorDrawable(0))
        //设置键盘弹出模式
        softInputMode =
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED
        //设置监听
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
        //显示弹窗
        view.post {
            showAtLocation(
                view,
                Gravity.NO_GRAVITY,
                0,
                0
            )
        }
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > maxHeight) {
            maxHeight = rect.bottom
        }
        //键盘的高度
        val keyboardHeight = maxHeight - rect.bottom
        function(keyboardHeight)
    }


}
