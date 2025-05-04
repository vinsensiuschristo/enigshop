package com.enigmacamp.enigshop.constants;

import lombok.Getter;

@Getter
public enum CustomerColumn {
    ID("id"),
    FULL_NAME("full_name"),
    PHONE_NUMBER("phone_number"),
    ADDRESS("address"),
    IS_ACTIVE("is_active");

    private final String column;

    CustomerColumn(String column) {
        this.column = column;
    }

    public static boolean isValidColumn(String value) {
        for (CustomerColumn column : CustomerColumn.values()) {
            if (column.getColumn().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
