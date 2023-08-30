package com.bookstore.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AddBookRequest {

    @NotNull
    private Integer isbn;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer stockQuantity;

    public AddBookRequest() {
    }

    public AddBookRequest(Integer isbn, String title, String author, BigDecimal price, Integer stockQuantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
