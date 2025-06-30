package com.mapzen.jpostal;

import com.mapzen.jpostal.ParsedComponent;
import com.mapzen.jpostal.ParserOptions;
import java.lang.ref.Cleaner;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddressParser {
    private static final Cleaner cleaner = Cleaner.create();
    private static final AtomicBoolean isShutdown = new AtomicBoolean(false);
    
    private volatile static AddressParser instance = null;
    private final LibPostal libPostal;
    private final Cleaner.Cleanable cleanable;

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
        return instance != null && !isShutdown.get();
    }

    private static native void setup();
    private static native void setupDataDir(String dataDir);
    private native ParsedComponent[] libpostalParse(String address, ParserOptions options);
    private static native void teardown();

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
        
        if (isShutdown.get()) {
            throw new IllegalStateException("AddressParser has been shut down");
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
        
        // Register cleanup
        this.cleanable = cleaner.register(this, () -> {
            if (isShutdown.compareAndSet(false, true)) {
                try {
                    synchronized (libPostal) {
                        teardown();
                    }
                } catch (Exception e) {
                    System.err.println("AddressParser cleaner error: " + e.getMessage());
                }
            }
        });
    }
}