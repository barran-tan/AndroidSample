package com.barran.sample.hook

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.BitmapDrawable.BitmapState
import android.graphics.drawable.Drawable
import android.util.Log
import java.lang.reflect.Field

class NewBitmapDrawable(bitmap: Bitmap?) : BitmapDrawable(bitmap) {

    init {
        try {
            val stateField = BitmapDrawable::class.java.getDeclaredField("mBitmapState")
            stateField.isAccessible = true
            stateField[this] = NewBitmapState(constantState as BitmapState)
        } catch (e: Exception) {
            Log.w("bitmap", "hook mBitmapState failed", e)
        }
    }

    class NewBitmapState(state: BitmapState) : BitmapDrawable.BitmapState(state) {

        private lateinit var field: Field

        init {
            try {

                val stateClz =
                    Class.forName("android.graphics.drawable.BitmapDrawable\$BitmapState")
                field = stateClz.getDeclaredField("mBitmap")
                field.isAccessible = true
                Log.v("bitmap", "NewBitmapState<init> bitmap ${field[state]}")
            } catch (e: Exception) {
                Log.w("bitmap", "reflect bitmap in BitmapState failed", e)
            }
        }

        override fun newDrawable(): Drawable {
            try {
                val bitmap = field[this] as Bitmap?
                Log.v("newDrawable bitmap", "bitmap $bitmap")
                return NewBitmapDrawable(bitmap)
            } catch (e: Exception) {
                Log.w("bitmap", "reflect bitmap in BitmapState failed", e)
            }
            return NewBitmapDrawable(null)
        }
    }
}
