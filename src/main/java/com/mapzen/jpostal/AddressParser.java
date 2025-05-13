package com.mapzen.jpostal;

import com.mapzen.jpostal.ParsedComponent;
import com.mapzen.jpostal.ParserOptions;

public class AddressParser {
    private static native void setup();
    private static native void setupDataDir(String dataDir);
    private native ParsedComponent[] libpostalParse(String address, ParserOptions options);
    private static native void teardown();

    private volatile static AddressParser instance = null;

    private final LibPostal libPostal;

    public static AddressParser getInstanceDataDir(String dataDir) {
        return getInstanceConfig(Config.builder().dataDir(dataDir).build());
    }

    public static AddressParser getInstanceConfig(Config config) {
        if (instance == null) {
            synchronized(AddressParser.class) {
                if (instance == null) {
                    instance = new AddressParser(LibPostal.getInstance(config));
                }
            }
        } else if (!instance.libPostal.getConfig().equals(config)) {
            throw Config.mismatchException(instance.libPostal.getConfig(), config);
        }
        return instance;
    }
        
    public static AddressParser getInstance() {
        return getInstanceDataDir(null);
    }

    public static boolean isInitialized() {
        return instance != null;
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

        synchronized(AddressParser.class) {
            return libpostalParse(address, options);
        }
    } 

    AddressParser(final LibPostal libPostal) {
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
