package com.enigmacamp.enigshop.models.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    ORDERED( "Ordered",  "Transaction has been ordered"),
    SETTELMENT( "Settlement",  "Transaction has been settled"),
    PENDING( "Pending",  "Transaction is pending"),
    CANCEL( "Cancel",  "Transaction has been canceled"),
    DENY( "Deny",  "Transaction has been denied"),
    FAILURE( "Failure",  "Transaction has been failed"),
    EXPIRE( "Expire",  "Transaction has been expired"),
    REFUND( "Refund",  "Transaction has been refunded");

    private final String name;
    private final String description;

    TransactionStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static TransactionStatus findByName(String name) {
        for (TransactionStatus value : values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }


}
