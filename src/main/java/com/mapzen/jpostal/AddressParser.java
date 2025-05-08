package com.mapzen.jpostal;

import com.mapzen.jpostal.ParsedComponent;
import com.mapzen.jpostal.ParserOptions;

public class AddressParser {
    static native synchronized void setup();
    static native synchronized void setupDataDir(String dataDir);
    private native synchronized ParsedComponent[] libpostalParse(String address, ParserOptions options);
    static native synchronized void teardown();

    private volatile static AddressParser instance = null;

    private final Config config;

    public static AddressParser getInstanceDataDir(String dataDir) {
        return getInstanceConfig(Config.builder().dataDir(dataDir).build());
    }

    public static AddressParser getInstanceConfig(Config config) {
        if (instance == null) {
            synchronized(AddressParser.class) {
                if (instance == null) {
                    instance = new AddressParser(config);
                }
            }
        } else if (!instance.config.equals(config)) {
            throw Config.mismatchException(instance.config, config);
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

        synchronized(this) {
            return libpostalParse(address, options);
        }
    } 

    protected AddressParser(Config config) {
        config.loadLibrary();
        new ParserOptions.Builder().setDefaultOptions();

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
