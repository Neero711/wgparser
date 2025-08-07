package org.example.model;

public class WeakAuraEntry {
    private String name;
    private String importString;

    public WeakAuraEntry(String name, String importString) {
        this.name = name;
        this.importString = importString;
    }

    public String getName() {
        return name;
    }

    public String getImportString() {
        return importString;
    }
}