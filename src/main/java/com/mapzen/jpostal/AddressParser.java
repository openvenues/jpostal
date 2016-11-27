package com.mapzen.jpostal;

import com.mapzen.jpostal.ParsedComponent;
import com.mapzen.jpostal.ParserOptions;

public class AddressParser {
    static {
        System.loadLibrary("jpostal_parser"); // Load native library at runtime
    }

    static native synchronized void setup();
    private native synchronized ParsedComponent[] libpostalParse(String address, ParserOptions options);
    static native synchronized void teardown();

    private static class SingletonHolder {
        private static final AddressParser instance = new AddressParser();
    }

    public static AddressParser getInstance() {
        return SingletonHolder.instance;
    }

    public ParsedComponent[] parseAddress(String address) {
        return parseAddressWithOptions(address, new ParserOptions.Builder().build());
    }

    public ParsedComponent[] parseAddressWithOptions(String address, ParserOptions options) {
        if (address == null) {
            throw new NullPointerException("String address must not be null");
        }
        if (options == null) {
            throw new NullPointerException("ParserOptions options must not be null");
        }

        synchronized(this) {
            return libpostalParse(address, options);
        }
    }
    

    protected AddressParser() {
        setup();      
    } 

    protected void finalize() {
        teardown();
    }

}
