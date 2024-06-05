package com.barran.sample.reflect

import android.content.res.AssetManager
import android.util.Log
import android.util.TypedValue
import com.barran.sample.App
import java.lang.reflect.Method

object TestReflect {

    private const val TAG = "reflect"

    private val getValueMethod: Method? by lazy {
        val method = try {
            val getValueMethod = AssetManager::class.java.getDeclaredMethod(
                "getResourceValue",
                Int::class.java,
                Int::class.java,
                TypedValue::class.java,
                Boolean::class.java
            )
            getValueMethod.isAccessible = true
            getValueMethod
        } catch (e: Exception) {
            Log.i(TAG, "get method:getResourceValue error ${e.message}")

            var targetMethod: Method? = null
            for (method in AssetManager::class.java.declaredMethods) {
                Log.v(TAG, "method ${method.name} ${method.genericParameterTypes}")
                if (method.name == "getResourceValue") {
                    targetMethod = method
                    break
                }
            }
            targetMethod
        }
        method
    }

    fun getValue(id: Int): TypedValue? {
        val method = getValueMethod ?: return null
        val hexString = Integer.toHexString(id)
        try {
            val value = TypedValue()
            val resources = App.context.resources
            method.invoke(resources.assets, id, 0, value, true)
            return value
        } catch (e: Exception) {
            Log.i(TAG, "getResourceValue $hexString error ${e.message}")
        }
        return null
    }
}