package com.mapzen.jpostal;

import com.mapzen.jpostal.ExpanderOptions;
import java.lang.ref.Cleaner;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddressExpander {
    private static final AtomicBoolean isShutdown = new AtomicBoolean(false);
    
    private volatile static AddressExpander instance = null;
    private final LibPostal libPostal;
    private final Cleaner.Cleanable cleanable;

    public static AddressExpander getInstanceDataDir(String dataDir) {
        return getInstanceConfig(Config.builder().dataDir(dataDir).build());
    }

    public static AddressExpander getInstanceConfig(Config config) {
        if (instance == null) {
            synchronized(AddressExpander.class) {
                if (instance == null) {
                    instance = new AddressExpander(LibPostal.getInstance(config));
                }
            }
        } else if (!instance.libPostal.getConfig().equals(config)) {
            throw Config.mismatchException(instance.libPostal.getConfig(), config);
        }
        return instance;
    }

    public static AddressExpander getInstance() {
        return getInstanceDataDir(null);
    }

    public static boolean isInitialized() {
        return instance != null && !isShutdown.get();
    }

    private static native void setup();
    private static native void setupDataDir(String dataDir);
    private static native String[] libpostalExpand(String address, ExpanderOptions options);
    private static native void teardown();

    public String[] expandAddress(String address) {
        return expandAddressWithOptions(address, new ExpanderOptions.Builder().build());
    }

    public String[] expandAddressWithOptions(String address, ExpanderOptions options) {
        if (address == null) {
            throw new NullPointerException("String address must not be null");
        }
        if (options == null) {
            throw new NullPointerException("ExpanderOptions options must not be null");
        }
        
        if (isShutdown.get()) {
            throw new IllegalStateException("AddressExpander has been shut down");
        }

        synchronized(AddressExpander.class) {
            return libpostalExpand(address, options);
        }
    }

    AddressExpander(final LibPostal libPostal) {
        if (libPostal == null) {
            throw new NullPointerException("LibPostal must not be null");
        }

        this.libPostal = libPostal;

        final String dataDir = libPostal.getConfig().getDataDir();
        synchronized (this.libPostal) {
            if (dataDir == null) {
                setup();
            } else {
                setupDataDir(dataDir);
            }
        }
        
        this.cleanable = Utils.registerForCleanup(this, new CleanupAction(isShutdown, libPostal));
    }
    
    private static class CleanupAction implements Runnable {
        private final AtomicBoolean isShutdown;
        private final LibPostal libPostal;
        
        CleanupAction(AtomicBoolean isShutdown, LibPostal libPostal) {
            this.isShutdown = isShutdown;
            this.libPostal = libPostal;
        }
        
        @Override
        public void run() {
            if (isShutdown.compareAndSet(false, true)) {
                try {
                    synchronized (libPostal) {
                        teardown();
                    }
                } catch (Exception e) {
                    System.err.println("AddressExpander cleaner error: " + e.getMessage());
                }
            }
        }
    }
}