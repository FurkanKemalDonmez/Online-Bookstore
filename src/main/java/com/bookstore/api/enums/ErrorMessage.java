package com.bookstore.api.enums;

public enum ErrorMessage {
    INVALID_PAGE_SIZE_REQUEST("Page number and size must be positive."),
    NO_DATA("No data found for this page number."),
    ADD_BOOK_VALIDATION_ERRORS("All fields must be filled."),
    UPDATE_BOOK_VALIDATION_ERRORS("Price and Stock Quantity must be positive."),
    BOOK_ALREADY_EXIST("This book already exists. Change isbn value."),
    BOOK_NOT_FOUND("Book not found."),
    BOOK_DELETED("The book has been successfully deleted."),
    USER_HAS_NO_ORDER("This user has no order."),
    USER_NOT_FOUND("User not found."),
    INSUFFICIENT_STOCK("Insufficient stock for the book number : "),
    WRONG_ISBN("The book belonging to this isbn could not be found : "),
    MINIMUM_PRICE_FOR_ORDER("The total price of the order must be greater than 25."),
    ORDER_NOT_FOUND("Order not found.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
