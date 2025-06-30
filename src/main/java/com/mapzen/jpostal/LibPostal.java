package com.mapzen.jpostal;

import java.lang.ref.Cleaner;
import java.util.concurrent.atomic.AtomicBoolean;

final class LibPostal {
    private static final Cleaner cleaner = Cleaner.create();
    private static final AtomicBoolean isShutdown = new AtomicBoolean(false);
    
    private final Config config;
    private final Cleaner.Cleanable cleanable;

    private LibPostal(final Config config) {
        if (config == null) {
            throw new NullPointerException("Config must not be null");
        }

        config.loadLibrary();

        final String dataDir = config.getDataDir();
        if (dataDir == null) {
            setup();
        } else {
            setupDataDir(dataDir);
        }

        this.config = config;
        
        // Register cleanup
        this.cleanable = cleaner.register(this, () -> {
            if (isShutdown.compareAndSet(false, true)) {
                try {
                    teardown();
                } catch (Exception e) {
                    System.err.println("LibPostal teardown error: " + e.getMessage());
                }
            }
        });
    }

    Config getConfig() {
        return config;
    }

    private static native void setup();
    private static native void setupDataDir(final String dataDir);
    private static native void teardown();

    private volatile static LibPostal instance = null;

    static LibPostal getInstance(final Config config) {
       if (instance == null) {
            synchronized(LibPostal.class) {
                if (instance == null ) {
                    instance = new LibPostal(config);
                }
            }
        } else if (!instance.config.equals(config)) {
           throw Config.mismatchException(instance.config, config);
       }
       return instance;
    }
}
