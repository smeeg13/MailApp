package com.example.mailapp.Enums;

public enum ToastsMsg {

    EMPTY_FIELDS("Make Sure all Fields are completed", 100),
    INVALID_FIELDS("Some Fields are NOT Valid", 101),
    WRONG_INFO("Wrong Email or Password entered", 150),
    ACCOUNT_CREATED("Account Created",200),
    MAIL_DELETED("Mail Deleted !", 300);

    private String stringValue;
    private int intValue;

    private ToastsMsg(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
