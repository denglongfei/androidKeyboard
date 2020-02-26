package com.testapp.keyboardutil

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtil {
    //最后保存的键盘高度
    private var lastKeyboard = 0
    //上次键盘是否显示
    private var lastKeyboardshow = false

    /**
     * 显示键盘
     */
    private fun showKeyboard(view: View) {
        view.requestFocus()
        val inputManager =
            view.context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputManager.showSoftInput(view, 0)
    }

    /**
     * 隐藏键盘
     */
    private fun hideKeyboard(view: View) {
        view.clearFocus()
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    /**
     * 键盘初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    fun init(
        layoutHelper: LayoutHelper,
        switch: View, //切换按钮
        edt: EditText,//输入框
        panelView: MyFlow, //面板view
        function: ((isShowPanel: Boolean) -> Unit)? = null
    ) {
        setListener(panelView, layoutHelper)

        switch.setOnClickListener {
            //只有面板view展开时才可见
            if (panelView.visibility == View.VISIBLE) {
                //显示键盘
                panelView.visibility = View.INVISIBLE
                showKeyboard(edt)
            } else {
                //显示面板view
                showPanel(layoutHelper, panelView, edt)
                edt.clearFocus()
            }
            function?.invoke(panelView.visibility == View.VISIBLE)
        }


        edt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val keyboardHeight = -getKeyboardHeight(
                    layoutHelper.context
                )
                layoutHelper.translationY = keyboardHeight.toFloat()
                panelView.visibility = View.INVISIBLE
            }
            false
        }


    }


    private fun setListener(
        panelView: MyFlow,
        layoutHelper: LayoutHelper
    ) {
        KeyboardHeightProvider(panelView) { keyboardHeight ->
            val isKeyboardshow = keyboardHeight > 0
            //计算并保存键盘高度
            calculateKeyboardHeight(panelView,layoutHelper, keyboardHeight)
            if (isKeyboardshow != lastKeyboardshow) {
                changedPanelAndKeyboard(isKeyboardshow, layoutHelper, panelView)
            }
            lastKeyboardshow = isKeyboardshow


        }
    }


    /**
     * 计算并保存键盘高度
     */
    private fun calculateKeyboardHeight(
        panelView: MyFlow,
        layoutHelper: LayoutHelper,
        keyboardHeight: Int
    ) {
        val height = getKeyboardHeight(panelView.context)
        //如果面板高度和键盘不同
        if (panelView.height != height) {
            //改变view高度
            changedViewHeight(panelView, height)
        }
        //保存键盘高度
        val changed =
            saveKeyboardHeight(panelView.context, keyboardHeight)
        if (changed) {
            var height = getKeyboardHeight(panelView.context)
            //改变view高度
            changedViewHeight(
                panelView,
                height
            )
            //面板是展开的时候 键盘变化需要平移布局
            if (layoutHelper.isExpand){
                layoutHelper.translationY= -height.toFloat()
            }

        }


    }

    /**
     * 改变面板和键盘
     */
    private fun changedPanelAndKeyboard(
        isKeyboardShowing: Boolean,
        layoutHelper: LayoutHelper,
        panelView: View
    ) {
        //隐藏键盘时
        if (!isKeyboardShowing && panelView.visibility == View.INVISIBLE) {
            layoutHelper.translationY = 0f
        }
        //面板view显示时 键盘弹起
        if (isKeyboardShowing && panelView.visibility == View.VISIBLE) {
            panelView.visibility = View.INVISIBLE
        }

    }


    /**
     * 显示面板
     */
    private fun showPanel(layoutHelper: LayoutHelper, panelView: View, edt: EditText) {
        if (layoutHelper.isExpand) {
            //已经展开 显示面板view隐藏键盘
            panelView.visibility = View.VISIBLE
            hideKeyboard(edt)
        } else {
            //没有展开  直接展开显示面板view
            panelView.visibility = View.VISIBLE
            val keyboardHeight = -getKeyboardHeight(
                layoutHelper.context
            )
            layoutHelper.translationY = keyboardHeight.toFloat()
        }
    }


    /**
     * 隐藏面板和键盘
     */
    fun hidePanelAndKeyboard(layoutHelper: LayoutHelper, panelView: MyFlow) {
        val activity = panelView.context as Activity
        val focusView = activity.currentFocus

        if (focusView == null) {
            layoutHelper.translationY = 0f
        } else {
            hideKeyboard(focusView)
            focusView.clearFocus()
        }
        panelView.visibility = View.INVISIBLE
    }

    /**
     * 显示面板和键盘
     */
    fun showePanelAndKeyboard(layoutHelper: LayoutHelper, edt: EditText) {
        layoutHelper.translationY = -getKeyboardHeight(
            layoutHelper.context
        ).toFloat()
        showKeyboard(edt)
    }


    /**
     * 改变view高度
     */
    private fun changedViewHeight(
        view: MyFlow,
        height: Int
    ) {

        var layoutParams = view.layoutParams
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
            )
            view.layoutParams = layoutParams
        } else {
            layoutParams.height = height
        }
        view.requestLayout()
    }


    /**
     * 保存键盘高度
     */
    private fun saveKeyboardHeight(context: Context, keyboardHeight: Int): Boolean {
        if (lastKeyboard == keyboardHeight) {
            return false
        }
        if (keyboardHeight <= 0) {
            return false
        }
        lastKeyboard = keyboardHeight
        return KeyBoardSharedPreferences.setHeight(
            context,
            keyboardHeight
        )
    }

    /**
     * 获取键盘高度
     */
    private fun getKeyboardHeight(context: Context): Int {
        if (lastKeyboard == 0) {
            lastKeyboard =
                KeyBoardSharedPreferences.getHeight(
                    context
                )

        }
        return lastKeyboard
    }


}