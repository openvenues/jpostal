#include <jni.h>
#include <libpostal/libpostal.h>

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_LibPostal_setup
  (JNIEnv *env, jclass cls) {

    if (!libpostal_setup()) {
        jclass exceptionClass;
        exceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
        if (exceptionClass == NULL) return;
        (*env)->ThrowNew(env, exceptionClass, "Error initializing libpostal");
    }
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_LibPostal_setupDataDir
  (JNIEnv *env, jclass cls, jstring jDataDir) {
    const char *datadir = (*env)->GetStringUTFChars(env, jDataDir, 0);
    if (!libpostal_setup_datadir((char *)datadir)) {
        jclass exceptionClass;
        exceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
        if (exceptionClass == NULL) return;
        (*env)->ThrowNew(env, exceptionClass, "Error initializing libpostal with datadir");
    }
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_LibPostal_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown();
}
