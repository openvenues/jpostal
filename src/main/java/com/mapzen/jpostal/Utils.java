package com.mapzen.jpostal;

import java.lang.ref.Cleaner;

final class Utils {
    
    private static final Cleaner CLEANER = Cleaner.create();
    
    private Utils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    static Cleaner.Cleanable registerForCleanup(Object obj, Runnable cleanupAction) {
        return CLEANER.register(obj, cleanupAction);
    }
}