#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <libpostal/libpostal.h>

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_AddressExpander_setup
  (JNIEnv *env, jclass cls) {

    if (!libpostal_setup() || !libpostal_setup_language_classifier()) {
        exit(EXIT_FAILURE);
    }
}

JNIEXPORT jobjectArray JNICALL Java_com_mapzen_jpostal_AddressExpander_libpostalExpand
  (JNIEnv *env, jclass cls, jstring jAddress, jobject jOptions) {
    const char *address = (*env)->GetStringUTFChars(env, jAddress, 0);

    size_t num_expansions = 0;
    normalize_options_t options = get_libpostal_default_options();

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


    fid = (*env)->GetFieldID(env, optionsCls, "addressComponents", "S");
    if (fid == 0) {
        return NULL;
    }

    options.address_components = (uint16_t) (*env)->GetShortField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "latinAscii", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.latin_ascii = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "transliterate", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.transliterate = (*env)->GetBooleanField(env, jOptions, fid);


    fid = (*env)->GetFieldID(env, optionsCls, "stripAccents", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.strip_accents = (*env)->GetBooleanField(env, jOptions, fid);


    fid = (*env)->GetFieldID(env, optionsCls, "decompose", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.decompose = (*env)->GetBooleanField(env, jOptions, fid);


    fid = (*env)->GetFieldID(env, optionsCls, "lowercase", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.lowercase = (*env)->GetBooleanField(env, jOptions, fid);


    fid = (*env)->GetFieldID(env, optionsCls, "trimString", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.trim_string = (*env)->GetBooleanField(env, jOptions, fid);


    fid = (*env)->GetFieldID(env, optionsCls, "dropParentheticals", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.drop_parentheticals = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "replaceNumericHyphens", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.replace_numeric_hyphens = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "deleteNumericHyphens", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.delete_numeric_hyphens = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "splitAlphaFromNumeric", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.split_alpha_from_numeric = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "replaceWordHyphens", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.replace_word_hyphens = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "deleteWordHyphens", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.delete_word_hyphens = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "deleteFinalPeriods", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.delete_final_periods = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "deleteAcronymPeriods", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.delete_acronym_periods = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "dropEnglishPossessives", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.drop_english_possessives = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "deleteApostrophes", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.delete_apostrophes = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "expandNumex", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.expand_numex = (*env)->GetBooleanField(env, jOptions, fid);

    fid = (*env)->GetFieldID(env, optionsCls, "romanNumerals", "Z");
    if (fid == 0) {
        return NULL;
    }

    options.roman_numerals = (*env)->GetBooleanField(env, jOptions, fid);

    char **expansions = expand_address((char *)address, options, &num_expansions);

    (*env)->ReleaseStringUTFChars(env, jAddress, address);

    jobjectArray ret = (jobjectArray)(*env)->NewObjectArray(env,
                                                            num_expansions,
                                                            (*env)->FindClass(env, "java/lang/String"),
                                                            (*env)->NewStringUTF(env, ""));

    if (num_expansions > 0) {
        for (size_t i = 0; i < num_expansions; i++) {
            (*env)->SetObjectArrayElement(env, ret, i, (*env)->NewStringUTF(env, expansions[i]));
        }

    }

    if (expansions != NULL) {
        expansion_array_destroy(expansions, num_expansions);
    }

    if (languages != NULL) {
        for (i = 0; i < num_languages; i++) {
            free(languages[i]);
        }
        free(languages);
    }

    return ret;
}

JNIEXPORT void JNICALL Java_com_mapzen_jpostal_AddressExpander_teardown
  (JNIEnv *env, jclass cls) {
    libpostal_teardown();
    libpostal_teardown_language_classifier();
}


JNIEXPORT void JNICALL Java_com_mapzen_jpostal_ExpanderOptions_00024Builder_setDefaultOptions
  (JNIEnv *env, jobject builder) {
    jfieldID fid;
    jclass cls = (*env)->GetObjectClass(env, builder);

    normalize_options_t default_options = get_libpostal_default_options();

    fid = (*env)->GetFieldID(env, cls, "languages", "[Ljava/lang/String;");
    if (fid == 0) {
        return;
    }

    (*env)->SetObjectField(env, builder, fid, NULL);

    fid = (*env)->GetFieldID(env, cls, "addressComponents", "S");
    if (fid == 0) {
        return;
    }

    (*env)->SetShortField(env, builder, fid, default_options.address_components);


    fid = (*env)->GetFieldID(env, cls, "latinAscii", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.latin_ascii);


    fid = (*env)->GetFieldID(env, cls, "transliterate", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.transliterate);


    fid = (*env)->GetFieldID(env, cls, "stripAccents", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.strip_accents);


    fid = (*env)->GetFieldID(env, cls, "decompose", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.decompose);


    fid = (*env)->GetFieldID(env, cls, "lowercase", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.lowercase);


    fid = (*env)->GetFieldID(env, cls, "trimString", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.trim_string);


    fid = (*env)->GetFieldID(env, cls, "dropParentheticals", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.drop_parentheticals);


    fid = (*env)->GetFieldID(env, cls, "replaceNumericHyphens", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.replace_numeric_hyphens);


    fid = (*env)->GetFieldID(env, cls, "deleteNumericHyphens", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.delete_numeric_hyphens);


    fid = (*env)->GetFieldID(env, cls, "splitAlphaFromNumeric", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.split_alpha_from_numeric);


    fid = (*env)->GetFieldID(env, cls, "replaceWordHyphens", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.replace_word_hyphens);


    fid = (*env)->GetFieldID(env, cls, "deleteWordHyphens", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.delete_word_hyphens);


    fid = (*env)->GetFieldID(env, cls, "deleteFinalPeriods", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.delete_final_periods);


    fid = (*env)->GetFieldID(env, cls, "deleteAcronymPeriods", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.delete_acronym_periods);


    fid = (*env)->GetFieldID(env, cls, "dropEnglishPossessives", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.drop_english_possessives);


    fid = (*env)->GetFieldID(env, cls, "deleteApostrophes", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.delete_apostrophes);


    fid = (*env)->GetFieldID(env, cls, "expandNumex", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.expand_numex);



    fid = (*env)->GetFieldID(env, cls, "romanNumerals", "Z");
    if (fid == 0) {
        return;
    }

    (*env)->SetBooleanField(env, builder, fid, default_options.roman_numerals);
}
