package com.mapzen.jpostal;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

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

        return expansionInOutput(expansions, output);
    }

    private static boolean containsExpansionWithOptions(String address, String output, ExpanderOptions options) {
        AddressExpander expander = AddressExpander.getInstance();
        String[] expansions = expander.expandAddressWithOptions(address, options);

        return expansionInOutput(expansions, output);
    }

    @Test
    public void testEnglishExpansions() {
        assertTrue(containsExpansion("123 Main St", "123 main street"));

        String[] languages = {"en"};
        ExpanderOptions englishOptions = new ExpanderOptions.Builder().languages(languages).build();
        assertTrue(containsExpansionWithOptions("30 West Twenty-sixth St Fl No. 7", "30 west 26th street floor number 7", englishOptions));
        assertTrue(containsExpansionWithOptions("Thirty W 26th St Fl #7", "30 west 26th street floor number 7", englishOptions));
    }


}