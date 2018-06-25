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

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isDuplicate(JNIEnv *env, jobject thisObj,
   jstring jValue1, jstring jValue2, jobject jOptions, duplicate_function func) {

    const char *value1 = (*env)->GetStringUTFChars(env, jValue1, 0);

    const char *value2 = (*env)->GetStringUTFChars(env, jValue2, 0);

    libpostal_duplicate_options_t options = libpostal_get_default_duplicate_options();

    jfieldID fid;

    jclass optionsCls = (*env)->GetObjectClass(env, jOptions);

    fid = (*env)->GetFieldID(env, optionsCls, "languages", "[Ljava/lang/String;");
    if (fid == 0) {
        return NULL;
    }

    jobject jLanguages = (*env)->GetObjectField(env, jOptions, fid);

    size_t num_languages = 0;
    char **languages = NULL;
    int i;

    if (jLanguages != NULL) {
        jsize jNumLanguages = (*env)->GetArrayLength(env, jLanguages);

        languages = malloc(sizeof(char *) * jNumLanguages);
        jboolean is_copy = JNI_FALSE;

        num_languages = (size_t)jNumLanguages;

        for (i = 0; i < jNumLanguages; i++) {
            jstring jLanguage = (*env)->GetObjectArrayElement(env, jLanguages, i);

            const char *lang = (*env)->GetStringUTFChars(env, jLanguage, &is_copy);

            char *language = strdup(lang);
            languages[i] = language;

            (*env)->ReleaseStringUTFChars(env, jLanguage, lang);
        }
        options.languages = languages;
        options.num_languages = num_languages;
    }

    libpostal_duplicate_status_t response = func((char *)value1, (char *)value2, options);

    (*env)->ReleaseStringUTFChars(env, jValue1, value1);

    (*env)->ReleaseStringUTFChars(env, jValue2, value2);

    return (jint) response;
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isNameDuplicate
  (JNIEnv *env, jobject thisObj, jstring jName1, jstring jName2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jName1, jName2, jOptions, libpostal_is_name_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isStreetDuplicate
  (JNIEnv *env, jobject thisObj, jstring jStreet1, jstring jStreet2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jStreet1, jStreet2, jOptions, libpostal_is_street_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isHouseNumberDuplicate
  (JNIEnv *env, jobject thisObj, jstring jHouseNumber1, jstring jHouseNumber2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jHouseNumber1, jHouseNumber2, jOptions, libpostal_is_house_number_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isPOBoxDuplicate
  (JNIEnv *env, jobject thisObj, jstring jPOBox1, jstring jPOBox2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jPOBox1, jPOBox2, jOptions, libpostal_is_po_box_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isUnitDuplicate
  (JNIEnv *env, jobject thisObj, jstring jUnit1, jstring jUnit2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jUnit1, jUnit2, jOptions, libpostal_is_unit_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isFloorDuplicate
  (JNIEnv *env, jobject thisObj, jstring jFloor1, jstring jFloor2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jFloor1, jFloor2, jOptions, libpostal_is_floor_duplicate);
}

JNIEXPORT jint JNICALL Java_com_mapzen_jpostal_Dedupe_isPostalCodeDuplicate
  (JNIEnv *env, jobject thisObj, jstring jPostalCode1, jstring jPostalCode2, jobject jOptions) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicate(
      env, thisObj, jPostalCode1, jPostalCode2, jOptions, libpostal_is_postal_code_duplicate);
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_Dedupe_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown_language_classifier();
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_DuplicateOptions_00024Builder_setDefaultOptions
  (JNIEnv *env, jobject builder) {
    jfieldID fid;
    jclass cls = (*env)->GetObjectClass(env, builder);

    libpostal_duplicate_options_t default_options = libpostal_get_default_duplicate_options();

    fid = (*env)->GetFieldID(env, cls, "languages", "[Ljava/lang/String;");
    if (fid == 0) {
        return;
    }

    (*env)->SetObjectField(env, builder, fid, NULL);
}

typedef libpostal_fuzzy_duplicate_status_t (*fuzzy_duplicate_function)(size_t, char **, double *, size_t, char **, double *, libpostal_fuzzy_duplicate_options_t);

JNIEXPORT jdouble JNICALL Java_com_mapzen_jpostal_Dedupe_isDuplicateFuzzy(
    JNIEnv *env, jobject thisObj,
    jobjectArray jTokens1, jdoubleArray jScores1,
    jobjectArray jTokens2, jdoubleArray jScores2,
    jobject jOptions, fuzzy_duplicate_function func
) {
    jboolean is_copy = JNI_FALSE;

    // Get arguments
    int num_tokens1 = (*env)->GetArrayLength(env, jTokens1);
    int num_tokens2 = (*env)->GetArrayLength(env, jTokens2);

    double *scores1 = (*env)->GetDoubleArrayElements(env, jScores1, &is_copy);
    double *scores2 = (*env)->GetDoubleArrayElements(env, jScores2, &is_copy);

    const char *tokens1[num_tokens1];
    const char *tokens2[num_tokens2];

    for (int i = 0; i < num_tokens1; i++) {
        jstring jToken = (jstring) (*env)->GetObjectArrayElement(env, jTokens1, i);
        const char *token = (*env)->GetStringUTFChars(env, jToken, &is_copy);
        tokens1[i] = strdup(token);
        (*env)->ReleaseStringUTFChars(env, jToken, token);
    }

    for (int i = 0; i < num_tokens2; i++) {
        jstring jToken = (jstring) (*env)->GetObjectArrayElement(env, jTokens2, i);
        const char *token = (*env)->GetStringUTFChars(env, jToken, &is_copy);
        tokens2[i] = strdup(token);
        (*env)->ReleaseStringUTFChars(env, jToken, token);
    }

    // Build Options Argument
    libpostal_fuzzy_duplicate_options_t options = libpostal_get_default_fuzzy_duplicate_options();

    jfieldID fid;
    jclass optionsCls = (*env)->GetObjectClass(env, jOptions);

    fid = (*env)->GetFieldID(env, optionsCls, "languages", "[Ljava/lang/String;");
    if (fid == 0) {
        return 0.0;
    }

    jobject jLanguages = (*env)->GetObjectField(env, jOptions, fid);

    size_t num_languages = 0;
    char **languages = NULL;
    int i;

    if (jLanguages != NULL) {
        jsize jNumLanguages = (*env)->GetArrayLength(env, jLanguages);

        languages = malloc(sizeof(char *) * jNumLanguages);
        jboolean is_copy = JNI_FALSE;

        num_languages = (size_t)jNumLanguages;

        for (i = 0; i < jNumLanguages; i++) {
            jstring jLanguage = (*env)->GetObjectArrayElement(env, jLanguages, i);

            const char *lang = (*env)->GetStringUTFChars(env, jLanguage, &is_copy);

            char *language = strdup(lang);
            languages[i] = language;

            (*env)->ReleaseStringUTFChars(env, jLanguage, lang);
        }
        options.languages = languages;
        options.num_languages = num_languages;
    }

    // Call the libpostal function
    libpostal_fuzzy_duplicate_status_t status = func(num_tokens1, tokens1, scores1, num_tokens2, tokens2, scores2, options);

    // Clean up
    (*env)->ReleaseDoubleArrayElements(env, jScores1, scores1, 0);
    (*env)->ReleaseDoubleArrayElements(env, jScores2, scores2, 0);

    return (jdouble) status.similarity;
}


JNIEXPORT jdouble JNICALL Java_com_mapzen_jpostal_Dedupe_isNameDuplicateFuzzy(
    JNIEnv *env, jobject thisObj,
    jobjectArray jTokens1, jdoubleArray jScores1,
    jobjectArray jTokens2, jdoubleArray jScores2,
    jobject jOptions
) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicateFuzzy(env, thisObj,
        jTokens1, jScores1, jTokens2, jScores2, jOptions, libpostal_is_name_duplicate_fuzzy);
}

JNIEXPORT jdouble JNICALL Java_com_mapzen_jpostal_Dedupe_isStreetDuplicateFuzzy(
    JNIEnv *env, jobject thisObj,
    jobjectArray jTokens1, jdoubleArray jScores1,
    jobjectArray jTokens2, jdoubleArray jScores2,
    jobject jOptions
) {
    return Java_com_mapzen_jpostal_Dedupe_isDuplicateFuzzy(env, thisObj,
        jTokens1, jScores1, jTokens2, jScores2, jOptions, libpostal_is_street_duplicate_fuzzy);
}
