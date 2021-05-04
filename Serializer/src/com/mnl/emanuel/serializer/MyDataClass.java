package com.mnl.emanuel.serializer;

public final class MyDataClass {

    private boolean booleanField;

    private int intField;

    private String stringField;

    @DoNotSerialize
    private double pi = 3.14159;

    public MyDataClass(boolean booleanField, int intField, String stringField) {
        this.booleanField = booleanField;
        this.intField = intField;
        this.stringField = stringField;
    }

    /* [...] Getters, setters, and other methods omitted. */

}