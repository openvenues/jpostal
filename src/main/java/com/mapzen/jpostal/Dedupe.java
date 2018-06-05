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
    private static native synchronized int isHouseNumberDuplicate(String name1, String name2);
    private static native synchronized int isPOBoxDuplicate(String poBox1, String poBox2);
    private static native synchronized int isUnitDuplicate(String unit1, String unit2);
    private static native synchronized int isFloorDuplicate(String floor1, String floor2);
    private static native synchronized int isPostalCodeDuplicate(String postalCode1, String postalCode2);
    static native synchronized void teardown();

    public int isStreetDupe(String street1, String street2) {
        return isStreetDuplicate(street1, street2);
    }

    public int isNameDupe(String name1, String name2) {
        return isNameDuplicate(name1, name2);
    }

    public int isHouseNumberDupe(String houseNumber1, String houseNumber2) {
        return isHouseNumberDuplicate(houseNumber1, houseNumber2);
    }

    public int isPOBoxDupe(String poBox1, String poBox2) {
        return isPOBoxDuplicate(poBox1, poBox2);
    }

    public int isUnitDupe(String unit1, String unit2) {
        return isUnitDuplicate(unit1, unit2);
    }

    public int isFloorDupe(String floor1, String floor2) {
        return isFloorDuplicate(floor1, floor2);
    }

    public int isPostalCodeDupe(String postalCode1, String postalCode2) {
        return isPostalCodeDuplicate(postalCode1, postalCode2);
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
