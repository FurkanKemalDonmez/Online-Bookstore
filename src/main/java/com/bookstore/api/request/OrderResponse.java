package com.bookstore.api.request;

import com.bookstore.api.entity.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Integer orderId;

    private BigDecimal totalPrice;

    private LocalDate orderDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Book> listOfBooks;

    public OrderResponse() {
    }

    public OrderResponse(Integer orderId, BigDecimal totalPrice, LocalDate orderDate, LocalDateTime createdAt, LocalDateTime updatedAt, List<Book> listOfBooks) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.listOfBooks = listOfBooks;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Book> getListOfBooks() {
        return listOfBooks;
    }

    public void setListOfBooks(List<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }
}
