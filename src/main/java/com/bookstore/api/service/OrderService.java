package com.bookstore.api.service;

import com.bookstore.api.entity.Book;
import com.bookstore.api.entity.Order;
import com.bookstore.api.entity.User;
import com.bookstore.api.enums.ErrorMessage;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.repository.OrderRepository;
import com.bookstore.api.repository.UserRepository;
import com.bookstore.api.request.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getOrderListByUserId(Integer userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        List<OrderResponse> orderResponseList = new ArrayList<>();

        if (optionalUser.isPresent()) {
            User theUser = optionalUser.get();
            List<Order> optionalOrders = orderRepository.findByUser(theUser).orElse(null);

            if (optionalOrders != null && !optionalOrders.isEmpty()) {
                optionalOrders.sort(Comparator.comparing(Order::getUpdatedAt).reversed());

                for (Order order : optionalOrders) {
                    OrderResponse orderResponse = fillOrderResponse(order);
                    orderResponseList.add(orderResponse);
                }
                return ResponseEntity.ok(orderResponseList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.USER_HAS_NO_ORDER.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(ErrorMessage.USER_NOT_FOUND.getMessage());
        }
    }

    public ResponseEntity<?> placeOrder(List<Integer> booksIsbns) {

        Order theOrder = new Order();

        List<Book> bookList = new ArrayList<>();

        String errorMessage = "";

        for (Integer isbn : booksIsbns) {
            Optional<Book> optionalBook = bookRepository.findById(isbn);

            if (optionalBook.isPresent()) {
                Book theBook = optionalBook.get();
                if (theBook.getStockQuantity() > 0) {
                    bookList.add(theBook);
                    theBook.setStockQuantity(theBook.getStockQuantity() - 1);
                } else {
                    errorMessage += ErrorMessage.INSUFFICIENT_STOCK.getMessage() + isbn + "\n";
                }
            } else {
                errorMessage += ErrorMessage.WRONG_ISBN.getMessage() + isbn + "\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        theOrder.setListOfBooks(bookList);

        if (calculateTotalPrice(theOrder).compareTo(BigDecimal.valueOf(25)) < 0) {
            return ResponseEntity.badRequest().body(ErrorMessage.MINIMUM_PRICE_FOR_ORDER.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + " : " + userEmail));

        theOrder.setUser(user);
        theOrder.setTotalPrice(calculateTotalPrice(theOrder));
        theOrder.setOrderDate(LocalDate.now());
        theOrder.setCreatedAt(LocalDateTime.now());
        theOrder.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(theOrder);

        OrderResponse orderResponse = fillOrderResponse(theOrder);

        return ResponseEntity.ok(orderResponse);
    }

    public BigDecimal calculateTotalPrice(Order theOrder) {

        BigDecimal total = BigDecimal.ZERO;

        for (Book book : theOrder.getListOfBooks()) {
            total = total.add(book.getPrice());
        }
        return total;
    }

    public ResponseEntity<?> getOrderById(Integer orderId) {

        Order theOrder = orderRepository.findById(orderId).orElse(null);

        if (theOrder != null) {
            OrderResponse orderResponse = fillOrderResponse(theOrder);
            return ResponseEntity.ok(orderResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ORDER_NOT_FOUND.getMessage());
        }
    }

    public static OrderResponse fillOrderResponse(Order theOrder) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setOrderId(theOrder.getOrderId());
        orderResponse.setTotalPrice(theOrder.getTotalPrice());
        orderResponse.setOrderDate(theOrder.getOrderDate());
        orderResponse.setCreatedAt(theOrder.getCreatedAt());
        orderResponse.setUpdatedAt(theOrder.getUpdatedAt());
        orderResponse.setListOfBooks(theOrder.getListOfBooks());

        return orderResponse;
    }
}
