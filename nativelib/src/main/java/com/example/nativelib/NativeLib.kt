package com.example.nativelib

import java.lang.reflect.Method
import java.lang.reflect.Modifier

class NativeLib {

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun checkClassLoaded(pointer: Int, className: String): Boolean

    external fun dynamicNativeFunc(): String

    external fun setMethodAccess(flag: Int = Modifier.PUBLIC, method: Method,sdk:Int): Boolean

//    external fun test2(): String

    companion object {
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }
    }
}