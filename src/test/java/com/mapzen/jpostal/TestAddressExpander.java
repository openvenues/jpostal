package com.mapzen.jpostal;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestAddressExpander {
    private static boolean expansionInOutput(String[] expansions, String output) {
        for (String expansion : expansions) {
            if (expansion.equals(output)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsExpansion(String address, String output) {
        AddressExpander expander = AddressExpander.getInstance();
        String[] expansions = expander.expandAddress(address);

        System.err.println(String.format("address=%s   expected=%s   expansions=%s", address, output, java.util.Arrays.toString(expansions)));


        return expansionInOutput(expansions, output);
    }

    private static boolean containsExpansionWithOptions(String address, String output, ExpanderOptions options) {
        AddressExpander expander = AddressExpander.getInstance();
        String[] expansions = expander.expandAddressWithOptions(address, options);

        return expansionInOutput(expansions, output);
    }

    @Test
    public void testExpandNull() {
        AddressExpander expander = AddressExpander.getInstance();
        ExpanderOptions options = new ExpanderOptions.Builder().build();

        try {
            expander.expandAddress(null);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}

        try {
            expander.expandAddressWithOptions(null, options);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}

        try {
            expander.expandAddressWithOptions("address", null);
            fail("Should throw NullPointerException to protect JNI");
        } catch (NullPointerException e) {}
    }

    @Test
    public void testEnglishExpansions() {
        assertTrue(containsExpansion("123 Main St", "123 main street"));

        String[] languages = {"en"};
        ExpanderOptions englishOptions = new ExpanderOptions.Builder().languages(languages).build();
        assertTrue(containsExpansionWithOptions("30 West Twenty-sixth St Fl No. 7", "30 west 26th street floor number 7", englishOptions));
    }

    @Test()
    public void testConfigMismatchDataDir() {
        AddressExpander.getInstance();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            AddressExpander.getInstanceDataDir("foo");
        });

        System.err.println("THROWN = " + thrown);

        assertEquals(
                "Config mismatch: initialized instance uses [Config{dataDir=null,libraryFile=null}], but requested [Config{dataDir=foo,libraryFile=null}]",
                thrown.getMessage()
        );
        assertNull(thrown.getCause());
    }

    @Test()
    public void testConfigMismatchLibraryFile() {
        AddressExpander.getInstance();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            AddressExpander.getInstanceConfig(Config.builder().libraryFile("foo").build());
        });

        assertEquals(
                "Config mismatch: initialized instance uses [Config{dataDir=null,libraryFile=null}], but requested [Config{dataDir=null,libraryFile=foo}]",
                thrown.getMessage()
        );
        assertNull(thrown.getCause());
    }
}
