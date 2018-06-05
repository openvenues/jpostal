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

typedef libpostal_duplicate_status_t (*duplicate_function)(char *, char *, libpostal_duplicate_options_t);

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isDuplicate
  (JNIEnv *env, jobject thisObj, jstring jValue1, jstring jValue2, duplicate_function func) {

    const char *value1 = (*env)->GetStringUTFChars(env, jValue1, 0);

    const char *value2 = (*env)->GetStringUTFChars(env, jValue2, 0);

    libpostal_duplicate_options_t options = libpostal_get_default_duplicate_options();

    libpostal_duplicate_status_t response = func((char *)value1, (char *)value2, options);

    (*env)->ReleaseStringUTFChars(env, jValue1, value1);

    (*env)->ReleaseStringUTFChars(env, jValue2, value2);

    return (jint) response;
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isNameDuplicate
  (JNIEnv *env, jobject thisObj, jstring jName1, jstring jName2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jName1, jName2, libpostal_is_name_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isStreetDuplicate
  (JNIEnv *env, jobject thisObj, jstring jStreet1, jstring jStreet2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jStreet1, jStreet2, libpostal_is_street_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isHouseNumberDuplicate
  (JNIEnv *env, jobject thisObj, jstring jHouseNumber1, jstring jHouseNumber2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jHouseNumber1, jHouseNumber2, libpostal_is_house_number_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isPOBoxDuplicate
  (JNIEnv *env, jobject thisObj, jstring jPOBox1, jstring jPOBox2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jPOBox1, jPOBox2, libpostal_is_po_box_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isUnitDuplicate
  (JNIEnv *env, jobject thisObj, jstring jUnit1, jstring jUnit2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jUnit1, jUnit2, libpostal_is_unit_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isFloorDuplicate
  (JNIEnv *env, jobject thisObj, jstring jFloor1, jstring jFloor2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jFloor1, jFloor2, libpostal_is_floor_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isPostalCodeDuplicate
  (JNIEnv *env, jobject thisObj, jstring jPostalCode1, jstring jPostalCode2) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jPostalCode1, jPostalCode2, libpostal_is_postal_code_duplicate);
}


JNIEXPORT void JNICALL Java_com_mapzen_jpostal_Dedupe_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown_language_classifier();
}
