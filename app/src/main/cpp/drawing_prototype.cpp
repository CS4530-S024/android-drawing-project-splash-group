
#include "drawing_prototype.h"
#include <android/bitmap.h>
#include <cstdlib>
#include <ctime>

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
JNIEXPORT void JNICALL
Java_com_example_drawing_1prototype_DrawingBoardModel_CW_190Degree(JNIEnv *env, jobject thiz,
                                                                   jobject bitmap) {

    AndroidBitmapInfo info;
    void* pixels;
    void* destPixels;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;

    int width = info.width;
    int height = info.height;
    int ret;

    jobject newBitmap = env->CallObjectMethod(thiz, env->GetMethodID(env->GetObjectClass(thiz), "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;"), height, width, env->GetStaticObjectField(env->FindClass("android/graphics/Bitmap$Config"), env->GetStaticFieldID(env->FindClass("android/graphics/Bitmap$Config"), "ARGB_8888", "Landroid/graphics/Bitmap$Config;")));

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &destPixels)) < 0) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    }

    uint32_t* srcLine = (uint32_t*) pixels;
    uint32_t* dstLine = (uint32_t*) destPixels;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            uint32_t pixel = srcLine[x + y * width];
            dstLine[(width - 1 - x) * height + y] = pixel;
        }
    }

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            uint32_t pixel = dstLine[x + y * width];
            srcLine[x + y * width] = pixel;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    AndroidBitmap_unlockPixels(env, newBitmap);

    return;
}
