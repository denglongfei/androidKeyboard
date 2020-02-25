package com.testapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.testapp.databinding.ActivityMainBinding
import com.testapp.keyboardutil.KeyboardUtil

class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        KeyboardUtil.init(binding.layer, binding.switchBut, binding.sendEdt, binding.panelFlow)

        binding.recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                KeyboardUtil.hidePanelAndKeyboard(binding.layer,binding.panelFlow)
            }
            false
        }

    }
}
