/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class SerialPort_SerialPort */


#ifndef _Included_SerialPort_SerialPort
#define _Included_SerialPort_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     SerialPort_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_SerialPort_SerialPort_open
  (JNIEnv *, jclass, jstring, jint, jint);

/*
 * Class:     SerialPort_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_SerialPort_SerialPort_close
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
