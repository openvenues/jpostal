package com.mapzen.jpostal;

public class Dedupe {
    // static {
    //     System.loadLibrary("jpostal_dedupe"); // Load native library at runtime
    // }

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
    private static native synchronized int isStreetDuplicate(String street1, String street2, DuplicateOptions options);
    private static native synchronized int isNameDuplicate(String name1, String name2, DuplicateOptions options);
    private static native synchronized int isHouseNumberDuplicate(String name1, String name2, DuplicateOptions options);
    private static native synchronized int isPOBoxDuplicate(String poBox1, String poBox2, DuplicateOptions options);
    private static native synchronized int isUnitDuplicate(String unit1, String unit2, DuplicateOptions options);
    private static native synchronized int isFloorDuplicate(String floor1, String floor2, DuplicateOptions options);
    private static native synchronized int isPostalCodeDuplicate(String postalCode1, String postalCode2, DuplicateOptions options);
    static native synchronized void teardown();

    public DuplicateStatus isStreetDupe(String street1, String street2) {
        return isStreetDupeWithOptions(street1, street2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isStreetDupeWithOptions(String street1, String street2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isStreetDuplicate(street1, street2, options));
    }

    public DuplicateStatus isNameDupe(String name1, String name2) {
        return isNameDupeWithOptions(name1, name2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isNameDupeWithOptions(String name1, String name2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isNameDuplicate(name1, name2, options));
    }

    public DuplicateStatus isHouseNumberDupe(String houseNumber1, String houseNumber2) {
        return isHouseNumberDupeWithOptions(houseNumber1, houseNumber2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isHouseNumberDupeWithOptions(String houseNumber1, String houseNumber2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isHouseNumberDuplicate(houseNumber1, houseNumber2, options));
    }

    public DuplicateStatus isPOBoxDupe(String poBox1, String poBox2) {
        return isPOBoxDupeWithOptions(poBox1, poBox2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isPOBoxDupeWithOptions(String poBox1, String poBox2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isPOBoxDuplicate(poBox1, poBox2, options));
    }

    public DuplicateStatus isUnitDupe(String unit1, String unit2) {
        return isUnitDupeWithOptions(unit1, unit2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isUnitDupeWithOptions(String unit1, String unit2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isUnitDuplicate(unit1, unit2, options));
    }

    public DuplicateStatus isFloorDupe(String floor1, String floor2) {
        return isFloorDupeWithOptions(floor1, floor2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isFloorDupeWithOptions(String floor1, String floor2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isFloorDuplicate(floor1, floor2, options));
    }

    public DuplicateStatus isPostalCodeDupe(String postalCode1, String postalCode2) {
        return isPostalCodeDupeWithOptions(postalCode1, postalCode2, new DuplicateOptions.Builder().build());
    }

    public DuplicateStatus isPostalCodeDupeWithOptions(String postalCode1, String postalCode2, DuplicateOptions options) {
        return DuplicateStatus.fromInt(isPostalCodeDuplicate(postalCode1, postalCode2, options));
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
