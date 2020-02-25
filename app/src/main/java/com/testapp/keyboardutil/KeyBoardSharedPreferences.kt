package com.testapp.keyboardutil

import android.content.Context
import android.content.SharedPreferences

object KeyBoardSharedPreferences {
    //用于单例实例化
    @Volatile
    private var instance: SharedPreferences? = null

    private fun getInstance(context: Context): SharedPreferences {
        return instance
            ?: synchronized(this) {
                instance
                    ?: createInstance(
                        context
                    ).also { instance = it }
            }
    }

    private fun createInstance(context: Context): SharedPreferences {
        return context.getSharedPreferences("keyboar", Context.MODE_PRIVATE)
    }

    /**
     * 获取键盘高度
     */
    fun getHeight(context: Context): Int {
        if (instance == null) {
            getInstance(
                context
            )
        }
        return instance!!.getInt("keyboard.height", context.dip2px(220f))
    }

    /**
     * 设置键盘高度
     */
    fun setHeight(context: Context, height: Int): Boolean {
        if (instance == null) {
            getInstance(
                context
            )
        }
        return instance!!.edit().putInt("keyboard.height", height).commit()
    }

    private inline fun Context.dip2px(dipValue: Float): Int {
        val scale = this.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


}