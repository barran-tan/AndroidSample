package com.barran.sample.compat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.barran.sample.R
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.EditText

class Compat35Activity : AppCompatActivity() {

    private val TAG = "compat35"

    private lateinit var btn: Button
    private lateinit var edit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_compat_35)
        btn = findViewById(R.id.show_input_dialog)
        btn.setOnClickListener {
            // adjust resize not work
        }
        edit = findViewById(R.id.input)

        monitorKeyboard()
    }

    private fun monitorKeyboard() {
        val view = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(view, object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(
                v: View,
                insets: WindowInsetsCompat
            ): WindowInsetsCompat {

                val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                val height = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime())
                val naviInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                val both = insets.getInsets(WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.statusBars())
                Log.v(TAG, "ime visible=$imeVisible height=${height} imeBottom=${imeInset.bottom} naviBottom=${naviInset.bottom} bot=${both.bottom}")
                if (imeVisible) {
                    (edit.layoutParams as MarginLayoutParams).bottomMargin = imeInset.bottom
                } else {
                    (edit.layoutParams as MarginLayoutParams).bottomMargin = naviInset.bottom
                }
                edit.requestLayout()

                (btn.layoutParams as MarginLayoutParams).topMargin = both.top
                btn.requestLayout()
                return insets
            }
        })
    }
}