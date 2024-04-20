//
// Created by Nitocris on 2024/4/19.
//
#include <jni.h>
#ifndef DRAWING_PROTOTYPE_DRAWING_PROTOTYPE_H
#define DRAWING_PROTOTYPE_DRAWING_PROTOTYPE_H

#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL
Java_com_example_drawing_1prototype_DrawingBoardModel_invertColors(JNIEnv *, jobject, jobject bitmap);

JNIEXPORT void JNICALL
Java_com_example_drawing_1prototype_DrawingBoardModel_CW_90Degree(JNIEnv *, jobject, jobject bitmap);
#ifdef __cplusplus
}
#endif
#endif