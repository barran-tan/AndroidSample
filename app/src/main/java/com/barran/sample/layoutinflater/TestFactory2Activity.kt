package com.barran.sample.layoutinflater

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory
import android.view.LayoutInflater.Factory2
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatViewInflater
import androidx.core.view.LayoutInflaterCompat
import java.lang.reflect.Method

private const val TAG = "TestFactory"

open class TestFactory2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomFactory()
        super.onCreate(savedInstanceState)
    }

    private fun setCustomFactory() {
        Log.i(TAG, "setCustomFactory $this")
        val inflater = LayoutInflater.from(this)
        val factory2 = inflater.factory
        if (factory2 != null) {
            Log.w(TAG, "factory2 already set $factory2")
        } else {
            val compatDelegate = delegate
            if (compatDelegate is Factory2) {
                LayoutInflaterCompat.setFactory2(inflater, TestFactory2(inflater, compatDelegate))
            } else {
                Log.w(TAG, "app compat delegate not Factory2 $delegate")
            }
        }
    }
}

class TestFactory2(inflater: LayoutInflater, private val factoryDelegate: Factory2) : Factory2 {

    private var factory2: Factory2? = null
    private var factory: Factory? = null
    private var defaultFactory: Factory2? = null

    private val compatViewInflater = AppCompatViewInflater()
    private var createViewFromTagMethod: Method? = null

    init {
        Log.v(TAG, "TestFactory2 delegate $factoryDelegate")
        try {
            createViewFromTagMethod = AppCompatViewInflater::class.java.getDeclaredMethod(
                "createViewFromTag", Context::class.java, String::class.java, AttributeSet::class.java
            )
            if (createViewFromTagMethod != null) {
                requireNotNull(createViewFromTagMethod).isAccessible = true
            } else {
                Log.w(TAG, "get method of createViewFromTag null")
            }

            val factory2Field = LayoutInflater::class.java.getDeclaredField("mFactory2")
            if (factory2Field == null) {
                Log.w(TAG, "get field of mFactory2 null")
            } else {
                factory2Field.isAccessible = true
                factory2 = factory2Field[inflater] as Factory2?
                Log.i(TAG, "mFactory2 $factory2")
            }

            val factoryField = LayoutInflater::class.java.getDeclaredField("mFactory")
            if (factoryField == null) {
                Log.w(TAG, "get field of mFactory null")
            } else {
                factoryField.isAccessible = true
                factory = factoryField[inflater] as Factory?
                Log.i(TAG, "mFactory $factory")
            }

            val field = LayoutInflater::class.java.getDeclaredField("mPrivateFactory")
            if (field == null) {
                Log.w(TAG, "get field of mPrivateFactory null")
            } else {
                field.isAccessible = true
                defaultFactory = field[inflater] as Factory2?
                Log.i(TAG, "mPrivateFactory $defaultFactory")
            }
        } catch (e: Exception) {
            Log.e(TAG, "reflect mPrivateFactory failed", e)
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var view = factoryDelegate.onCreateView(parent, name, context, attrs)
        Log.v(TAG, "onCreateView2 by delegate name=$name view=$view")

        if (view == null && createViewFromTagMethod != null) {
            try {
                view = requireNotNull(createViewFromTagMethod).invoke(
                    compatViewInflater, context, name, attrs
                ) as View?
                Log.v(TAG, "onCreateView2 by compatInflater name=$name view=$view")
            } catch (e: Exception) {
                Log.e(TAG, "createViewFromTag failed", e)
            }
        }

        if (view == null) {

            if (factory2 != null) {
                view = requireNotNull(factory2).onCreateView(parent, name, context, attrs)
                Log.v(TAG, "onCreateView2 by factory2 name=$name view=$view")
            } else if (factory != null) {
                view = requireNotNull(factory).onCreateView(name, context, attrs)
                Log.v(TAG, "onCreateView2 by factory name=$name view=$view")
            }

            if (view == null) {
                view = requireNotNull(defaultFactory).onCreateView(parent, name, context, attrs)
                Log.v(TAG, "onCreateView2 by default name=$name view=$view")
            }
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view = factoryDelegate.onCreateView(name, context, attrs)
        Log.v(TAG, "onCreateView by delegate name=$name view=$view")

        if (view == null && createViewFromTagMethod != null) {
            try {
                view = requireNotNull(createViewFromTagMethod).invoke(
                    compatViewInflater, context, name, attrs
                ) as View?
                Log.v(TAG, "onCreateView by compatInflater name=$name view=$view")
            } catch (e: Exception) {
                Log.e(TAG, "createViewFromTag failed", e)
            }
        }

        if (view == null) {

            if (factory2 != null) {
                view = requireNotNull(factory2).onCreateView(name, context, attrs)
                Log.v(TAG, "onCreateView by factory2 name=$name view=$view")
            } else if (factory != null) {
                view = requireNotNull(factory).onCreateView(name, context, attrs)
                Log.v(TAG, "onCreateView by factory name=$name view=$view")
            }

            if (view == null) {
                view = requireNotNull(defaultFactory).onCreateView(name, context, attrs)
                Log.v(TAG, "onCreateView by default name=$name view=$view")
            }
        }
        return view
    }
}