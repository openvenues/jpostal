package com.mapzen.jpostal;

import java.nio.charset.StandardCharsets;

public class ParsedComponent {
    private String value;
    private String label;

    public String getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ParsedComponent(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public ParsedComponent(byte[] value, String label) {
        this.value = new String(value, StandardCharsets.UTF_8);
        this.label = label;
    }
}