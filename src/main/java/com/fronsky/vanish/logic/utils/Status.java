package com.fronsky.vanish.logic.utils;

public enum Status {
    IDLE("Idle"), // id = 0
    ENABLING("Enabling"), // id = 1
    ENABLED("Enabled"), // id = 2
    DISABLING("Disabling"), // id = 3
    DISABLED("Disabled"), // id = 4
    LOADING("Loading"), // id = 5
    LOADED("Loaded"), // id = 6
    ERROR("Error"), // id = 7
    SUCCESS("Success"), // id = 8
    FAILURE("Failure"), // id = 9
    NOT_FOUND("Not Found"), // id = 10
    INVALID_INPUT("Invalid Input"), // id = 11
    TIMEOUT("Timeout"), // id = 12
    UNAUTHORIZED("Unauthorized"), // id = 13
    FORBIDDEN("Forbidden"), // id = 14
    INTERNAL_ERROR("Internal Error"); // id = 15

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public int getId() {
        return ordinal();
    }

    public String getDescription() {
        return description;
    }
}
