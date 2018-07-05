package com.mapzen.jpostal;

public enum DuplicateStatus {
  LIBPOSTAL_NULL_DUPLICATE_STATUS(-1),
  LIBPOSTAL_NON_DUPLICATE(0),
  LIBPOSTAL_POSSIBLE_DUPLICATE_NEEDS_REVIEW(3),
  LIBPOSTAL_LIKELY_DUPLICATE(6),
  LIBPOSTAL_EXACT_DUPLICATE(9);

  public final int intVal;

  DuplicateStatus(int intVal) {
    this.intVal = intVal;
  }

  public static DuplicateStatus fromInt(int i) {
    for (DuplicateStatus s: values()) {
      if (s.intVal == i) {
        return s;
      }
    }

    return null;
  }
}
