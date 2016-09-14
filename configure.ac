#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_INIT([jpostal], [0.3])

AC_CONFIG_MACRO_DIR([m4])

AC_CONFIG_HEADERS([config.h])
AM_INIT_AUTOMAKE([foreign subdir-objects])
AC_CONFIG_SRCDIR([src/main/c])
LT_INIT([shared])

# Checks for programs.
AC_PROG_CC_C99

AX_PROG_JAVAC
AX_JNI_INCLUDE_DIR

for JNI_INCLUDE_DIR in $JNI_INCLUDE_DIRS
do
    CFLAGS="$CFLAGS -I$JNI_INCLUDE_DIR"
done

# Check for OS X, which has shared library extensions of ".dylib",
# and requires the Java library to have an extension of ".jnilib"
case "$host_os" in
darwin*)
    LDFLAGS="$LDFLAGS -shrext .jnilib"
;;
esac

# Checks for libraries.

# Checks for header files.
AC_CHECK_HEADERS([stdlib.h string.h])

# Checks for typedefs, structures, and compiler characteristics.
AC_TYPE_SIZE_T
AC_TYPE_UINT16_T

# Checks for library functions.
AC_FUNC_MALLOC
AC_CHECK_FUNCS([strdup])

PKG_CHECK_MODULES([LIBPOSTAL], [libpostal],, AC_MSG_ERROR([Could not find libpostal]))

AC_CONFIG_FILES([Makefile
                 src/main/c/Makefile])

AC_OUTPUT
