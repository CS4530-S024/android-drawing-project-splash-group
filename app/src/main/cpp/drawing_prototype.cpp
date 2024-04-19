// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("drawing_prototype");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("drawing_prototype")
//      }
//    }
#include "drawing_prototype.h"
#include <android/bitmap.h>
#include <cstdlib>
#include <ctime>

//void invertColors(JNIEnv *env, jobject, jobject bitmap) {
//    AndroidBitmapInfo info;
//    void* pixels;
//    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return;
//    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;
//
//    auto *line = static_cast<uint32_t *>(pixels);
//    int height = info.height;
//    int width = info.width;
//    for (int y = 0; y < height; ++y) {
//        for (int x = 0; x < width; ++x) {
//            uint32_t &pixel = line[x];
//
//            // Invert the color for pixel
//            pixel = ~pixel;
//
//        }
//        line = reinterpret_cast<uint32_t *>((char*)line + info.stride);
//    }
//    AndroidBitmap_unlockPixels(env, bitmap);
//}

jobject CW_90Degree(JNIEnv *env, jobject thiz, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels;
    void* destPixels;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return nullptr;

    int width = info.width;
    int height = info.height;
    int ret;

    jobject newBitmap = env->CallObjectMethod(thiz, env->GetMethodID(env->GetObjectClass(thiz), "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;"), height, width, env->GetStaticObjectField(env->FindClass("android/graphics/Bitmap$Config"), env->GetStaticFieldID(env->FindClass("android/graphics/Bitmap$Config"), "ARGB_8888", "Landroid/graphics/Bitmap$Config;")));

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &destPixels)) < 0) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return nullptr;
    }

    uint32_t* srcLine = (uint32_t*) pixels;
    uint32_t* dstLine = (uint32_t*) destPixels;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            uint32_t pixel = srcLine[x + y * width];
            dstLine[(width - 1 - x) * height + y] = pixel;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    AndroidBitmap_unlockPixels(env, newBitmap);

    env->CallVoidMethod(thiz, env->GetMethodID(env->GetObjectClass(thiz), "setBitmap", "(Landroid/graphics/Bitmap;)V"), newBitmap);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_drawing_1prototype_DrawingBoardModel_invertColors(JNIEnv *env, jobject thiz,
                                                                   jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;

    auto *line = static_cast<uint32_t *>(pixels);
    int height = info.height;
    int width = info.width;
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            uint32_t &pixel = line[x];

            // Invert the color for pixel
            pixel = ~pixel;

        }
        line = reinterpret_cast<uint32_t *>((char*)line + info.stride);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_drawing_1prototype_DrawingBoardModel_CW_190Degree(JNIEnv *env, jobject thiz,
                                                                   jobject bitmap) {

    AndroidBitmapInfo info;
    void* pixels;
    void* destPixels;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return nullptr;

    int width = info.width;
    int height = info.height;
    int ret;

    jobject newBitmap = env->CallObjectMethod(thiz, env->GetMethodID(env->GetObjectClass(thiz), "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;"), height, width, env->GetStaticObjectField(env->FindClass("android/graphics/Bitmap$Config"), env->GetStaticFieldID(env->FindClass("android/graphics/Bitmap$Config"), "ARGB_8888", "Landroid/graphics/Bitmap$Config;")));

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &destPixels)) < 0) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return nullptr;
    }

    uint32_t* srcLine = (uint32_t*) pixels;
    uint32_t* dstLine = (uint32_t*) destPixels;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            uint32_t pixel = srcLine[x + y * width];
            dstLine[(width - 1 - x) * height + y] = pixel;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    AndroidBitmap_unlockPixels(env, newBitmap);

    return newBitmap;
}
