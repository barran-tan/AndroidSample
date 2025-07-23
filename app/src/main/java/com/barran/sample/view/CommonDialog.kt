package com.barran.sample.view

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class CommonDialog : DialogFragment() {

    companion object {
        const val TAG = "CommonDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        try {
            super.onStart()
            val dialog = dialog ?: return
            val window = dialog.window ?: return
            val layoutParams = window.attributes
            layoutParams.gravity = Gravity.BOTTOM
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.flags =
                layoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = layoutParams
        } catch (e: Exception) {
            Log.e("CommonDialog", e.message ?: "null msg")
        }
    }

}
