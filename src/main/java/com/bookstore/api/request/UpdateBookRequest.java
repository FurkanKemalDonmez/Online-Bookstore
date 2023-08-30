package com.bookstore.api.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class UpdateBookRequest {

    private String title;

    private String author;

    @Positive
    private BigDecimal price;

    @Positive
    private Integer stockQuantity;

    public UpdateBookRequest() {
    }

    public UpdateBookRequest(String title, String author, BigDecimal price, Integer stockQuantity) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
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
