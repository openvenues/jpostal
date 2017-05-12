package com.mapzen.jpostal;

public class AddressParser {
    static {
        System.loadLibrary("jpostal_parser"); // Load native library at runtime
    }

    static native synchronized void setup();
    static native synchronized void setupDataDir(String dataDir);
    private native synchronized ParsedComponent[] libpostalParse(String address, ParserOptions options);
    static native synchronized void teardown();

    private volatile static AddressParser instance = null;

    public static AddressParser getInstanceDataDir(String dataDir) {
        if (instance == null) {
            synchronized(AddressParser.class) {
                if (instance == null) {
                    instance = new AddressParser(dataDir);
                }
            }
        }
        return instance;
    }
        
    public static AddressParser getInstance() {
        return getInstanceDataDir(null);
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
        if (address.contains("\0"))
            throw new IllegalArgumentException("Input cannot contain null bytes");

        synchronized(this) {
            return libpostalParse(address, options);
        }
    } 

    protected AddressParser(String dataDir) {
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
