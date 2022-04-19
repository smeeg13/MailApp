package com.example.mailapp.Enums;

public enum Messages {

    //Validation on fields entry
    EMPTY_FIELDS("Make Sure all Fields are completed", 100),
    INVALID_FIELDS("Some Fields are NOT Valid", 101),
    INVALID_EMAIL("Email is not valid", 101),
    WEAK_PWD("Password is too weak", 101),
    NOT_SAME_PWD("Passwords are not the same", 101),

    WRONG_INFO("Wrong Email or Password entered", 150),
    EMAIL_ALREADY_EXIST("Email already used",151),

    //Infos Creation - Delete - Update
    ACCOUNT_CREATED("Account Created",200),
    ACCOUNT_UPDATED("Account Updated",200),
    ACCOUNT_UPDATED_FAILED("Account Update Failed",200),
    ACCOUNT_DELETED("Account Deleted",200),
    ACCOUNT_DELETED_FAILED("Account Delete Failed",200),
    MAIL_CREATED("Mail Created", 300),
    MAIL_CREATED_FAILED("Mail Created Failed", 300),
    MAIL_DELETED_FAILED("Mail Deleted Failed ", 300),
    MAIL_DELETED("Mail Deleted", 300),
    MAIL_UPDATED("Mail Updated",300 ),
    MAIL_UPDATE_FAILED("Mail Update Failed",300 ),
    BACKGROUND_UPDATED_FAILED("Background update failed",400),
    BACKGROUND_UPDATED("Background updated successfully",400);


    private final String stringValue;

    Messages(String toString, int value) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
