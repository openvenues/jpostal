package com.mapzen.jpostal;

public class ParsedComponent {
    private String component;
    private String label;

    public String getComponent() {
        return this.component;
    }

    public String getLabel() {
        return this.label;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ParsedComponent(String component, String label) {
        this.component = component;
        this.label = label;
    }
}