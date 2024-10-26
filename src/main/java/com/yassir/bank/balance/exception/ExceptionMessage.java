package com.yassir.bank.balance.exception;

public enum ExceptionMessage {

    NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID("New Entity shouldn't have an ID"),
    ENTITY_NOT_FOUND("There is no entity with this ID "),
    ENDPOINT_NOT_FOUND("Endpoint not found, please check the URL and try again"),
    INVALID_REQUEST_PARAMETERS("Invalid input, please check the request parameters"),
    INVALID_LONG_ID("Invalid Id, please ensure that the Id is a valid number");

    private final String message;

    public String getMessage() {
        return message;
    }

    ExceptionMessage(String message) {
        this.message = message;
    }
}
