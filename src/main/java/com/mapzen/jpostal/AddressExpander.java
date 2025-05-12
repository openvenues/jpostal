package com.mapzen.jpostal;

import com.mapzen.jpostal.ExpanderOptions;

public class AddressExpander {
    private volatile static AddressExpander instance = null;

    private final LibPostal libPostal;

    public static AddressExpander getInstanceDataDir(String dataDir) {
        return getInstanceConfig(Config.builder().dataDir(dataDir).build());
    }

    public static AddressExpander getInstanceConfig(Config config) {
        if (instance == null) {
            synchronized(AddressParser.class) {
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
        return instance != null;
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
    } 

    @Override
    protected void finalize() {
        synchronized (libPostal) {
            teardown();
        }
    }
}
