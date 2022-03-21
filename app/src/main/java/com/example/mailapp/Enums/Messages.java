package com.example.mailapp.Enums;

public enum Messages {

    DB_WILL_BE_CREATED("Database will be initialized",00),
    DB_CREATED("Database initialized.",01),
    INSERT_DEMO_DATA("Inserting demo data.",02),
    EMPTY_FIELDS("Make Sure all Fields are completed", 100),
    INVALID_FIELDS("Some Fields are NOT Valid", 101),
    WRONG_INFO("Wrong Email or Password entered", 150),
    EMAIL_ALREADY_EXIST("Email already used",151),
    ACCOUNT_CREATED("Account Created",200),
    ACCOUNT_UPDATED("Account Updated",200),
    ACCOUNT_UPDATED_FAILED("Account Update Failed",200),
    ACCOUNT_DELETED("Account Deleted",200),
    ACCOUNT_DELETED_FAILED("Account Delete Failed",200),
    MAIL_CREATED("Mail Created", 300),
    MAIL_DELETED("Mail Deleted !", 301);

    private String stringValue;
    private int intValue;

    private Messages(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
