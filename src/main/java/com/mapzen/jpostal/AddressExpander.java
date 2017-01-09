package com.mapzen.jpostal;

import com.mapzen.jpostal.ExpanderOptions;

public class AddressExpander {
    static {
        System.loadLibrary("jpostal_expander"); // Load native library at runtime
    }

    private volatile static AddressExpander instance = null;

    public static AddressExpander getInstanceDataDir(String dataDir) {
        if (instance == null) {
            synchronized(AddressExpander.class) {
                if (instance == null) {
                    instance = new AddressExpander(dataDir);
                }
            }
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

    protected AddressExpander(String dataDir) {
        if (dataDir == null) {
            setup();
        } else {
            setupDataDir(dataDir);
        }
    } 

    protected void finalize() {
        teardown();
    }
}
