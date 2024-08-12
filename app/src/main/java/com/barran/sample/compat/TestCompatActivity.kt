package com.barran.sample.compat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.barran.sample.R
import com.barran.sample.databinding.ActivityTestCompatBinding

class TestCompatActivity : AppCompatActivity() {

    private val TAG = "testCompat"

    private val TEST_BROADCAST = "barran.test"
    private val TEST_BROADCAST_PERMISSION = "barran.permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test_compat)

        initView()
    }

    private fun initView() {
        val bind = ActivityTestCompatBinding.bind(findViewById<View>(R.id.root))

        bind.registerBroadcast.setOnClickListener {
            registerBroadcast()
        }
        bind.registerBroadcastWithPermission.setOnClickListener {
            registerBroadcastWithPermission()
        }
        bind.registerBroadcastExportedWithPermission.setOnClickListener {
            registerBroadcastExportedWithPermission()
        }
        bind.sendBroadcast.setOnClickListener {
            sendBroadcast(false)
        }

        bind.sendBroadcastWithPkg.setOnClickListener {
            sendBroadcast(true)
        }
        bind.sendBroadcastWithPermission.setOnClickListener {
            sendBroadcastWithPermission()
        }
    }

    private fun registerBroadcast() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.v(TAG, "onReceive action=${intent?.action}")
            }
        }
        val filter = IntentFilter(TEST_BROADCAST)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.v(TAG, "registerBroadcast action=$TEST_BROADCAST")
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        }
    }

    private fun registerBroadcastWithPermission() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.v(TAG, "onReceive not exported with permission action=${intent?.action}")
            }
        }
        val filter = IntentFilter(TEST_BROADCAST)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.v(TAG, "registerBroadcastWithPermission action=$TEST_BROADCAST")
            registerReceiver(
                receiver,
                filter,
                TEST_BROADCAST_PERMISSION,
                null,
                Context.RECEIVER_NOT_EXPORTED
            )
        }
    }

    private fun registerBroadcastExportedWithPermission() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.v(TAG, "onReceive exported with permission action=${intent?.action}")
            }
        }
        val filter = IntentFilter(TEST_BROADCAST)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.v(TAG, "registerBroadcastExportedWithPermission >=13 action=$TEST_BROADCAST")
            registerReceiver(
                receiver,
                filter,
                TEST_BROADCAST_PERMISSION,
                null,
                Context.RECEIVER_EXPORTED
            )
        } else {
            Log.v(TAG, "registerBroadcastExportedWithPermission <13 action=$TEST_BROADCAST")
            registerReceiver(
                receiver,
                filter,
                TEST_BROADCAST_PERMISSION,
                null
            )
        }
    }

    private fun sendBroadcast(withPkg: Boolean) {
        Log.v(TAG, "sendBroadcast withPkg=$withPkg")
        val intent = Intent(TEST_BROADCAST)
        if(withPkg){
            intent.setPackage(application.packageName)
        }
        sendBroadcast(intent)
    }

    private fun sendBroadcastWithPermission() {
        Log.v(TAG, "sendBroadcastWithPermission")
        val intent = Intent(TEST_BROADCAST)
        intent.setPackage(application.packageName)
        sendBroadcast(intent, TEST_BROADCAST_PERMISSION)
    }
}