package com.barran.example.html

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

import com.barran.example.mdtest.R

/**
 * fullscreen video
 *
 * Created by tanwei on 2018/1/26.
 */

class WebVideoActivity : AppCompatActivity() {

    private var webView: WebView? = null

    private var webUrl = "https://live.bilibili.com/5269?spm_id_from=333.334.bili_live.12"
    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_web_video)
        webView = findViewById(R.id.web_view)

        initWebView()
    }

    override fun onStop() {
        super.onStop()
        webView!!.reload()
    }

    /** 展示网页界面  */
    private fun initWebView() {
        val wvcc = WebChromeClient()
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true // 关键点
        webSettings.allowFileAccess = true // 允许访问文件
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.loadWithOverviewMode = true
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        //        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE) // 不加载缓存内容

        webView!!.webChromeClient = wvcc
        val wvc = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                webView!!.loadUrl(url)
                return true
            }
        }
        webView!!.webViewClient = wvc

        webView!!.webChromeClient = object : WebChromeClient() {

            /*** 视频播放相关的方法  */

            override fun getVideoLoadingProgressView(): View? {
                val frameLayout = FrameLayout(this@WebVideoActivity)
                frameLayout.layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT)
                return frameLayout
            }

            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                showCustomView(view, callback)
            }

            override fun onHideCustomView() {
                hideCustomView()
            }
        }

        // 加载Web地址
        webView!!.loadUrl(webUrl)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    /** 视频播放全屏  */
    private fun showCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }

        this@WebVideoActivity.window.decorView

        val decor = window.decorView as FrameLayout
        fullscreenContainer = FullscreenHolder(this@WebVideoActivity)
        fullscreenContainer!!.addView(view, COVER_SCREEN_PARAMS)
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS)
        customView = view
        setStatusBarVisibility(false)
        customViewCallback = callback

        // 切换方向
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /** 隐藏视频全屏  */
    private fun hideCustomView() {
        if (customView == null) {
            return
        }

        setStatusBarVisibility(true)
        val decor = window.decorView as FrameLayout
        decor.removeView(fullscreenContainer)
        fullscreenContainer = null
        customView = null
        customViewCallback!!.onCustomViewHidden()
        webView!!.visibility = View.VISIBLE

        // 切换方向
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /** 全屏容器界面  */
    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {

        init {
            setBackgroundColor(ctx.resources.getColor(android.R.color.black))
        }

        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面  */
                if (customView != null) {
                    hideCustomView()
                } else if (webView!!.canGoBack()) {
                    webView!!.goBack()
                } else {
                    finish()
                }
                return true
            }
            else -> return super.onKeyUp(keyCode, event)
        }
    }

    companion object {

        /** 视频全屏参数  */
        private val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
