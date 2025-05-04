package com.enigmacamp.enigshop.models.enums;

import lombok.Getter;

@Getter
public enum UserRoles {
    ADMIN("Admin"),
    SELLER("Seller"),
    CUSTOMER("Customer");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }

    public static UserRoles fromValue(String value) {
        for (UserRoles role : UserRoles.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }

    public static boolean isValidValue(String role) {
        for (UserRoles userRole : UserRoles.values()) {
            if (userRole.value.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
