#include <jni.h>
#include <libpostal/libpostal.h>

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_Dedupe_setup
  (JNIEnv *env, jclass cls) {

    if (!libpostal_setup() || !libpostal_setup_language_classifier()) {
        jclass exceptionClass;
        exceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
        if (exceptionClass == NULL) return;
        (*env)->ThrowNew(env, exceptionClass, "Error loading libpostal dedupe modules\n");
    }
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_Dedupe_setupDataDir
  (JNIEnv *env, jclass cls, jstring jDataDir) {
    const char *datadir = (*env)->GetStringUTFChars(env, jDataDir, 0);
    if (!libpostal_setup_datadir((char *)datadir) || !libpostal_setup_language_classifier_datadir((char *)datadir)) {
        jclass exceptionClass;
        exceptionClass = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
        if (exceptionClass == NULL) return;
        (*env)->ThrowNew(env, exceptionClass, "Error loading libpostal dedupe modules\n");
    }    
}

JNIEXPORT jobjectArray JNICALL Java_com_mapzen_jpostal_Dedupe_isStreetDuplicate
  (JNIEnv *env, jobject thisObj, jstring jStreet1, jstring jStreet2) {

    const char *street1 = (*env)->GetStringUTFChars(env, jStreet1, 0);

    const char *street2 = (*env)->GetStringUTFChars(env, jStreet2, 0);

    libpostal_duplicate_options_t options = libpostal_get_default_duplicate_options();

    libpostal_duplicate_status_t response = libpostal_is_street_duplicate(
      (char *)street1, (char *)street2, options);

    (*env)->ReleaseStringUTFChars(env, jStreet1, street1);

    (*env)->ReleaseStringUTFChars(env, jStreet2, street2);

    return (jint) response;
}


JNIEXPORT jobjectArray JNICALL Java_com_mapzen_jpostal_Dedupe_isNameDuplicate
  (JNIEnv *env, jobject thisObj, jstring jName1, jstring jName2) {

    const char *name1 = (*env)->GetStringUTFChars(env, jName1, 0);

    const char *name2 = (*env)->GetStringUTFChars(env, jName2, 0);

    libpostal_duplicate_options_t options = libpostal_get_default_duplicate_options();

    libpostal_duplicate_status_t response = libpostal_is_name_duplicate(
      (char *)name1, (char *)name2, options);

    (*env)->ReleaseStringUTFChars(env, jName1, name1);

    (*env)->ReleaseStringUTFChars(env, jName2, name2);

    return (jint) response;
}


JNIEXPORT void JNICALL Java_com_mapzen_jpostal_Dedupe_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown_language_classifier();
}
