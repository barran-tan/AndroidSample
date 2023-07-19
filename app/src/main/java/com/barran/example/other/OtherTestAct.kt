package com.barran.example.other

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.barran.example.mdtest.R

class OtherTestAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        val listener = View.OnClickListener {
            when (it.id) {
                R.id.test_get_install_package_list -> {
                    val list = applicationContext.packageManager?.getInstalledApplications(
                        PackageManager.GET_META_DATA)
                    if(list != null){
                        Log.v("other","app list size ${list.size}")
                        for (info in list) {
                            Log.v("other","app ${info.name}")
                        }
                    }else{
                        Log.v("other","app list null")
                    }
                }

                R.id.query_actiivty -> {
                    val link = "market://details?id=$packageName"
                    val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

                    val pm: PackageManager = applicationContext.packageManager
                    val list = pm.queryIntentActivities(marketIntent, 0)

                    if (list != null) {
                        Log.v("other", "query intent list size ${list.size}")
                        for (info in list) {
                            Log.v("other", "query intent package=${info.resolvePackageName} $info")
                        }
                    } else {
                        Log.v("other", "query intent list null")
                    }
                }
            }
        }

        findViewById<View>(R.id.test_get_install_package_list).setOnClickListener(listener)
        findViewById<View>(R.id.test_get_install_package_list2).setOnClickListener(listener)
        findViewById<View>(R.id.query_actiivty).setOnClickListener(listener)
    }
}