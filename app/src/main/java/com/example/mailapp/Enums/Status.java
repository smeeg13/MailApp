package com.example.mailapp.Enums;


public enum Status {
    IN_PROGRESS("In Progress", 0),
    DELETED("Deleted", -1),
    DONE("Done", 1),
    RETURNED("Returned to Sender", -10);

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

