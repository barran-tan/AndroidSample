package com.barran.example.hardware

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.barran.example.mdtest.R
import com.barran.example.mdtest.databinding.ActHardwareTestBinding

/**
 * description
 *
 * create by tanwei@bigo.sg
 * on 2022/5/17
 */
class HardwareTestAct : AppCompatActivity() {

    private lateinit var bind: ActHardwareTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_hardware_test)
        bind = ActHardwareTestBinding.bind(findViewById(R.id.root))

        init()

        bind.iv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                Log.i(TAG, "onPreDraw")
                output()
                bind.iv.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

//        bind.iv.postDelayed({
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//            )
//
//            Log.i(TAG, "onResume setFlags")
//            output()
//        }, 5000)
    }

    private fun init() {

        bind.webView.loadUrl("https://juejin.cn/post/6844903651475980302")

        bind.btn.setOnClickListener {
            bind.iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            bind.iv.invalidate()
        }

        bind.tv.setOnClickListener {
            anim2()
        }

        bind.log.setOnClickListener {
            output()
        }

        bind.iv.setOnClickListener {
            anim()
        }

        output()
    }

    private fun output(){
        val sb = StringBuilder("hardwareAccelerated ")
        sb.append("web=${bind.webView.isHardwareAccelerated} \n")
        sb.append("surface=${bind.surface.isHardwareAccelerated} \n")
        sb.append("tv=${bind.tv.isHardwareAccelerated} \n")
        sb.append("iv=${bind.iv.isHardwareAccelerated} \n")
        sb.append("btn=${bind.btn.isHardwareAccelerated} \n")
        sb.append("log=${bind.log.isHardwareAccelerated}")
        val toString = sb.toString()
        bind.log.text = toString
        Log.v(TAG, toString)
    }

    private fun anim() {
        val rotation = if (bind.iv.rotation == 0f) 360f else 0f
        bind.iv.animate().rotation(rotation).withLayer().apply {
            duration = 3000
            setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    Log.i(TAG, "onAnimationStart1")

                    bind.iv.post { output() }
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.i(TAG, "onAnimationEnd1")
                    bind.iv.post { output() }
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }

            })
        }
    }

    private fun anim2() {
        bind.tv.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        val rotation = if (bind.tv.rotation == 0f) 360f else 0f
        val animator = ObjectAnimator.ofFloat(bind.tv, "rotation", rotation)
        animator.duration = 3000

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                bind.tv.setLayerType(View.LAYER_TYPE_NONE, null)

                Log.i(TAG, "onAnimationEnd2")
                bind.tv.post { output() }
            }
        })
        var count = 0
        animator.addUpdateListener {
            // 如果设置了缓冲层，这里更新会导致额外的性能损耗
//            bind.tv.text = (count++).toString()
        }

        animator.start()

    }

}

const val TAG = "ha"