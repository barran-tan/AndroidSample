package com.barran.sample.compat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.barran.sample.R
import com.barran.sample.view.CommonDialog

class InputDialog : CommonDialog() {

    private lateinit var root: View
    private lateinit var edit: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.dialog_input, container, false)
        edit = root.findViewById(R.id.edit_input)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 测试结果：不设置ADJUST_RESIZE，键盘弹起后dialog上滑展示出EditText    ime visible=true imeHeight=870 naviHeight=0 max=870

        //         设置ADJUST_RESIZE后（未调整dialog底部间距），点击输入框键盘弹起dialog未被压缩
        //         ime visible=true imeHeight=870 naviHeight=0 max=870
        //         输入内容后再次点击输入框dialog被压缩
        //         ime visible=true imeHeight=870 naviHeight=0 max=870
        //         ime visible=true imeHeight=0 naviHeight=0 max=0

        //         设置ADJUST_RESIZE后（调整dialog底部间距），点击输入框键盘弹起dialog未被压缩,输入框被顶上去，因为内边距生效
        //         ime visible=true imeHeight=870 naviHeight=0 max=870
        //         输入内容后再次点击输入框dialog被压缩
        //         ime visible=true imeHeight=870 naviHeight=0 max=870
        //         ime visible=true imeHeight=0 naviHeight=0 max=0
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val actContent = activity?.findViewById<View>(android.R.id.content)
        val windowContent = dialog?.window?.findViewById<View>(android.R.id.content)
        Log.i(TAG, "onViewCreated actRoot=$actContent \nwindowRoot=$windowContent")

        val root = windowContent ?: root
        monitorKeyboard(root)
    }

    private fun monitorKeyboard(view: View) {
        Log.i(TAG, "monitorKeyboard $view")
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
                if (imeVisible) {
                    view.setPadding(0, 0, 0, imeInset.bottom)
                } else {
                    view.setPadding(0, 0, 0, naviInset.bottom)
                }

                return insets
            }
        })
    }
}