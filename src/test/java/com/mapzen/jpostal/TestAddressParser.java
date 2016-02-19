package com.mapzen.jpostal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestAddressParser {
    private static void testParse(String address, ParsedComponent... expectedOutput) {
        AddressParser parser = AddressParser.getInstance();
        ParsedComponent[] parsedComponents = parser.parseAddress(address);

        assertTrue(parsedComponents.length == expectedOutput.length);

        for (int i = 0; i < parsedComponents.length; i++) {
            ParsedComponent c1 = parsedComponents[i];
            ParsedComponent c2 = expectedOutput[i];

            assertEquals(c1.getLabel(), c2.getLabel());
            assertEquals(c1.getComponent(), c2.getComponent());
        }
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

        testParse("whole foods ny",
                  new ParsedComponent("whole foods", "house"),
                  new ParsedComponent("ny", "state")
                 );

        testParse("1917/2 Pike Drive",
                  new ParsedComponent("1917 / 2", "house_number"),
                  new ParsedComponent("pike drive", "road")
                 );

        testParse("3437 warwickshire rd,pa",
                  new ParsedComponent("3437", "house_number"),
                  new ParsedComponent("warwickshire rd", "road"),
                  new ParsedComponent("pa", "state")
                 );

        testParse("3437 warwickshire rd, pa",
                  new ParsedComponent("3437", "house_number"),
                  new ParsedComponent("warwickshire rd", "road"),
                  new ParsedComponent("pa", "state")
                 );

        testParse("3437 warwickshire rd pa",
                  new ParsedComponent("3437", "house_number"),
                  new ParsedComponent("warwickshire rd", "road"),
                  new ParsedComponent("pa", "state")
                 );
    }
}