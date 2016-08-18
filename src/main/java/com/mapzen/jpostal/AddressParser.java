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
        ParserOptions options = new ParserOptions.Builder().build();
        return libpostalParse(address, options);
    }

    public ParsedComponent[] parseAddressWithOptions(String address, ParserOptions options) {
        return libpostalParse(address, options);
    }
    

    protected AddressParser() {
        setup();      
    } 

    protected void finalize() {
        teardown();
    }

}
