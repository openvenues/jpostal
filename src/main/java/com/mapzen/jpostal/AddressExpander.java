package com.mapzen.jpostal;

import com.mapzen.jpostal.ExpanderOptions;

public class AddressExpander {
    static {
        System.loadLibrary("jpostal_expander"); // Load native library at runtime
    }

    private static AddressExpander instance = null;
    private static class SingletonHolder {
        private static final AddressExpander instance = new AddressExpander();   
    }

    public static AddressExpander getInstance() {
        return SingletonHolder.instance;
    }
 
    static native synchronized void setup();
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

    protected AddressExpander() {
        setup();      
    } 

    protected void finalize() {
        teardown();
    }
}
