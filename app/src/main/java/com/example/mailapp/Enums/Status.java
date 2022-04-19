package com.example.mailapp.Enums;


public enum Status {
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String stringValue;

    Status(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}

