package com.enigmacamp.enigshop.constants;

import lombok.Getter;

@Getter
public enum DepartmentColumn {
    ID("id"),
    NAME("name"),
    CODE("code"),
    DESCRIPTION("description");

    private final String column;

    DepartmentColumn(String column) {
        this.column = column;
    }

    public static boolean isValidColumn(String value) {
        for (DepartmentColumn column : DepartmentColumn.values()) {
            if (column.getColumn().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
