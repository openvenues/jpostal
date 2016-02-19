#include <jni.h>
#include <libpostal/libpostal.h>

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_AddressParser_setup
  (JNIEnv *env, jclass cls) {

    if (!libpostal_setup() || !libpostal_setup_parser()) {
        exit(EXIT_FAILURE);
    }
}


JNIEXPORT jobjectArray JNICALL Java_com_mapzen_jpostal_AddressParser_libpostalParse
  (JNIEnv *env, jobject thisObj, jstring jAddress, jobject jOptions) {

    const char *address = (*env)->GetStringUTFChars(env, jAddress, 0);

    address_parser_options_t options = get_libpostal_address_parser_default_options();

    jfieldID fid;
 
    jclass optionsCls = (*env)->GetObjectClass(env, jOptions);

    fid = (*env)->GetFieldID(env, optionsCls, "language", "Ljava/lang/String;");
    if (fid == 0) {
        return NULL;
    }

    jstring jLanguage = (*env)->GetObjectField(env, jOptions, fid);

    if (jLanguage != NULL) {
        options.language = (char *)(*env)->GetStringUTFChars(env, jLanguage, 0);
    }

    fid = (*env)->GetFieldID(env, optionsCls, "country", "Ljava/lang/String;");
    if (fid == 0) {
        return NULL;
    }

    jstring jCountry = (*env)->GetObjectField(env, jOptions, fid);

    if (jCountry != NULL) {
        options.country = (char *)(*env)->GetStringUTFChars(env, jCountry, 0);
    }

    address_parser_response_t *response = parse_address((char *)address, options);

    (*env)->ReleaseStringUTFChars(env, jAddress, address);

    if (jLanguage != NULL) {
        (*env)->ReleaseStringUTFChars(env, jLanguage, 0);
    }

    if (jCountry != NULL) {
        (*env)->ReleaseStringUTFChars(env, jCountry, 0);
    }

    jmethodID mid;

    jclass parsedComponentClass = (*env)->FindClass(env, "com/mapzen/jpostal/ParsedComponent");
    mid = (*env)->GetMethodID(env, parsedComponentClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");

    size_t num_components = response != NULL ? response->num_components : 0;

    jobjectArray ret = (*env)->NewObjectArray(env,
                                              num_components,
                                              parsedComponentClass,
                                              NULL);

    if (num_components > 0) {
        for (size_t i = 0; i < num_components; i++) {
            jstring jComponent = (*env)->NewStringUTF(env, response->components[i]);
            jstring jLabel = (*env)->NewStringUTF(env, response->labels[i]);

            jobject jParsedComponent = (*env)->NewObject(env, parsedComponentClass, mid, jComponent, jLabel);

            (*env)->SetObjectArrayElement(env, ret, i, jParsedComponent);
        }
    }

    if (response != NULL) {
        address_parser_response_destroy(response);
    }

    return ret;

}


JNIEXPORT void JNICALL Java_com_mapzen_jpostal_AddressParser_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown_parser();
}


JNIEXPORT void JNICALL Java_com_mapzen_jpostal_ParserOptions_00024Builder_setDefaultOptions
  (JNIEnv *env, jobject builder) {

    jfieldID fid;
    jclass cls = (*env)->GetObjectClass(env, builder);

    address_parser_options_t default_options = get_libpostal_address_parser_default_options();

    fid = (*env)->GetFieldID(env, cls, "language", "Ljava/lang/String;");
    if (fid == 0) {
        return;
    }

    (*env)->SetObjectField(env, builder, fid, NULL);

    fid = (*env)->GetFieldID(env, cls, "country", "Ljava/lang/String;");
    if (fid == 0) {
        return;
    }

    (*env)->SetObjectField(env, builder, fid, NULL);

}

