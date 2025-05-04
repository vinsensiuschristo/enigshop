package com.enigmacamp.enigshop.constants;

import lombok.Getter;

@Getter
public enum ProductColumn {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    STOCK("stock");

    private final String column;

    ProductColumn(String column) {
        this.column = column;
    }

    public static boolean isValidColumn(String value) {
        for (ProductColumn column : ProductColumn.values()) {
            if (column.getColumn().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
