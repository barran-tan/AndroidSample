#include <jni.h>
#include <string>
#include "ClassLoadHelper.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_nativelib_NativeLib_checkClassLoaded(JNIEnv *env, jobject thiz, jint pointer,
                                                      jstring class_name) {
    ClassLoadHelper helper(pointer);
    const char* name = env->GetStringUTFChars(class_name, 0);
    return helper.checkLoad(name);
}


extern "C" JNIEXPORT jstring JNICALL
dynamicNativeFunc(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "dynamicNativeFunc from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
//System.loadLibrary方法会调用载入的.so文件的函数列表中查找JNI_OnLoad函数并执行
JNI_OnLoad(JavaVM* vm, void* reserved) {
    static JNINativeMethod methods[] = {
            {
                    "dynamicNativeFunc", //在java中声明的native函数名
                    "()Ljava/lang/String;", //函数的签名，可以通过javah获取
                    (void *)dynamicNativeFunc//对应的native函数名
            }
    };
    JNIEnv *env = NULL;
    jint result = -1;
    // 获取JNI env变量
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        // 失败返回-1
        return result;
    }
    // 获取native方法所在Java类，包名和类名之间使用“/”风格
    const char* className = "com/example/nativelib/NativeLib";
    //这个可以找到要注册的类，提前是这个类已经加载到Java虚拟机中；
    //这里说明，动态库和有native方法的类之间，没有任何关系。
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        return result;
    }
    // 动态注册native方法
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) < 0) {
        return result;
    }

    // 返回成功
    result = JNI_VERSION_1_6;
    return result;
}
