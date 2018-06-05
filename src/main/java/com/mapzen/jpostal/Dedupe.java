package com.mapzen.jpostal;

public class Dedupe {
    private volatile static Dedupe instance = null;

    public static Dedupe getInstanceDataDir(String dataDir) {
        if (instance == null) {
            synchronized(Dedupe.class) {
                if (instance == null) {
                    instance = new Dedupe(dataDir);
                }
            }
        }
        return instance;
    }

    public static Dedupe getInstance() {
        return getInstanceDataDir(null);
    }


    static native synchronized void setup();
    static native synchronized void setupDataDir(String dataDir);
    private static native synchronized int isStreetDuplicate(String street1, String street2);
    private static native synchronized int isNameDuplicate(String name1, String name2);
    static native synchronized void teardown();

    public int isStreetDupe(String street1, String street2) {
        return isStreetDuplicate(street1, street2);
    }

    public int isNameDupe(String name1, String name2) {
        return isNameDuplicate(name1, name2);
    }

    protected Dedupe(String dataDir) {
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
