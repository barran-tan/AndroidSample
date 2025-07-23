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

    private val TAG = "compat35Act"

    private lateinit var btn: Button
    private lateinit var edit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_compat_35)
        btn = findViewById(R.id.show_input_dialog)
        btn.setOnClickListener {
            // adjust resize not work
            showInputDialog()
        }
        edit = findViewById(R.id.input)

        monitorKeyboard()
    }

    private fun showInputDialog() {
        val dialog = InputDialog()
        dialog.show(supportFragmentManager, TAG)
    }

    private fun monitorKeyboard() {
        val view = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(view, object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(
                v: View,
                insets: WindowInsetsCompat
            ): WindowInsetsCompat {

                val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime())
                val naviInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                val both =
                    insets.getInsets(WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.statusBars())
                Log.v(
                    TAG,
                    "ime visible=$imeVisible imeHeight=${imeInset.bottom} naviHeight=${naviInset.bottom} max=${both.bottom}"
                )
                view.setPadding(0, both.top, 0, both.bottom)
                return insets
            }
        })
    }
}