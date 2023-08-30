package com.bookstore.api.controller;

import com.bookstore.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @Operation(summary = "Get Order List By User Id", description = " Get all orders for a specific user ordered by update date DESC.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrderListByUserId(@PathVariable Integer userId){
        return orderService.getOrderListByUserId(userId);
    }

    @Operation(summary = "Create an order.",description = "Place a new order for a user with a minimum price of 25$.")
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody List<Integer> booksIsbns){
        return orderService.placeOrder(booksIsbns);
    }

    @Operation(summary = "Get Order By Id",description = "Get details of a specific order by its ID with the books under that order. ")
    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId){

        return orderService.getOrderById(orderId);
    }


}
