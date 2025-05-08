package com.mapzen.jpostal;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class TestAddressParser {
    private static void testParse(String address, ParsedComponent... expectedOutput) {
        AddressParser parser = AddressParser.getInstance();
        ParsedComponent[] parsedComponents = parser.parseAddress(address);

        assertEquals(expectedOutput.length, parsedComponents.length);

        for (int i = 0; i < parsedComponents.length; i++) {
            ParsedComponent c1 = parsedComponents[i];
            ParsedComponent c2 = expectedOutput[i];

            assertEquals(c1.getLabel(), c2.getLabel());
            assertEquals(c1.getValue(), c2.getValue());
        }
    }

    @Test
    public void testParseNull() {
        AddressParser parser = AddressParser.getInstance();
        ParserOptions options = new ParserOptions.Builder().build();

        try {
            parser.parseAddress(null);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}

        try {
            parser.parseAddressWithOptions(null, options);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}

        try {
            parser.parseAddressWithOptions("address", null);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}
    }

    @Test
    public void testParseUSAddress() {
        testParse("781 Franklin Ave Crown Heights Brooklyn NYC NY 11216 USA", 
                  new ParsedComponent("781", "house_number"),
                  new ParsedComponent("franklin ave", "road"),
                  new ParsedComponent("crown heights", "suburb"),
                  new ParsedComponent("brooklyn", "city_district"),
                  new ParsedComponent("nyc", "city"),
                  new ParsedComponent("ny", "state"),
                  new ParsedComponent("11216", "postcode"),
                  new ParsedComponent("usa", "country")
                 );
    }


    @Test()
    public void testConfigMismatchDataDir() {
        AddressParser.getInstance();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            AddressParser.getInstanceDataDir("foo");
        });

        assertEquals(
                "Config mismatch: initialized instance uses [Config{dataDir=null,libraryFile=null}], but requested [Config{dataDir=foo,libraryFile=null}]",
                thrown.getMessage()
        );
        assertNull(thrown.getCause());
    }

    @Test()
    public void testConfigMismatchLibraryFile() {
        AddressParser.getInstance();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            AddressParser.getInstanceConfig(Config.builder().libraryFile("foo").build());
        });

        assertEquals(
                "Config mismatch: initialized instance uses [Config{dataDir=null,libraryFile=null}], but requested [Config{dataDir=null,libraryFile=foo}]",
                thrown.getMessage()
        );
        assertNull(thrown.getCause());
    }
}
