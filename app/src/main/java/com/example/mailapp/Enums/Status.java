package com.example.mailapp.Enums;


public enum Status {
    DONE("Done", 1),
    DELETED("Deleted", -1),
    IN_PROGRESS("In Progress", 0);

    private String stringValue;
    private int intValue;

    private Status(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}

