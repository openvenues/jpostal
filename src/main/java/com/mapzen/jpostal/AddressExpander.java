package com.mapzen.jpostal;

import com.mapzen.jpostal.ExpanderOptions;

public class AddressExpander {
    private volatile static AddressExpander instance = null;

    private final Config config;

    public static AddressExpander getInstanceDataDir(String dataDir) {
        return getInstanceConfig(Config.builder().dataDir(dataDir).build());
    }

    public static AddressExpander getInstanceConfig(Config config) {
        if (instance == null) {
            synchronized(AddressParser.class) {
                if (instance == null) {
                    instance = new AddressExpander(config);
                }
            }
        } else if (!instance.config.equals(config)) {
            throw Config.mismatchException(instance.config, config);
        }
        return instance;
    }

    public static AddressExpander getInstance() {
        return getInstanceDataDir(null);
    }


    static native synchronized void setup();
    static native synchronized void setupDataDir(String dataDir);
    private static native synchronized String[] libpostalExpand(String address, ExpanderOptions options);
    static native synchronized void teardown();

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

        synchronized(this) {
            return libpostalExpand(address, options);
        }
    }

    protected AddressExpander(Config config) {
        config.loadLibrary();
        new ExpanderOptions.Builder().setDefaultOptions();

        final String dataDir = config.getDataDir();
        if (dataDir == null) {
            setup();
        } else {
            setupDataDir(dataDir);
        }

        this.config = config;
    } 

    @Override
    protected void finalize() {
        teardown();
    }
}
