package com.bookstore.api;

import com.bookstore.api.entity.Book;
import com.bookstore.api.entity.Order;
import com.bookstore.api.entity.Role;
import com.bookstore.api.entity.User;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.repository.OrderRepository;
import com.bookstore.api.repository.UserRepository;
import com.bookstore.api.request.OrderResponse;
import com.bookstore.api.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrderListByUserId() {
        User user = new User(1,"exampleName","exampleEmail","examplePassword",LocalDateTime.now(),LocalDateTime.now(), Role.USER);

        Order order1 = new Order(1,user,BigDecimal.valueOf(60),LocalDate.now(),LocalDateTime.now(),LocalDateTime.now(),null);
        Order order2 = new Order(2,user,BigDecimal.valueOf(60),LocalDate.now(),LocalDateTime.now(),LocalDateTime.now(),null);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Optional.of(orders));

        ResponseEntity<?> response = orderService.getOrderListByUserId(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<OrderResponse> responseList = (List<OrderResponse>) response.getBody();
        assertEquals(2, responseList.size());
    }

    @Test
    void testPlaceOrder() {
        List<Integer> bookIsbns = new ArrayList<>();
        bookIsbns.add(1);
        bookIsbns.add(2);

        Book book1 = new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now());
        Book book2 = new Book(2,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now());
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        mockBooks.add(book2);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(2)).thenReturn(Optional.of(book2));

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");

        User user = new User();
        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = orderService.placeOrder(bookIsbns);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof OrderResponse);
        OrderResponse orderResponse = (OrderResponse) response.getBody();
        assertNotNull(orderResponse);
        assertEquals(2, orderResponse.getListOfBooks().size());
        assertEquals(BigDecimal.valueOf(50), orderResponse.getTotalPrice());
    }

    @Test
    void testCalculateTotalPrice() {
        Order order = new Order();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now()));
        bookList.add(new Book(2,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now()));
        order.setListOfBooks(bookList);

        BigDecimal totalPrice = orderService.calculateTotalPrice(order);
        assertEquals(BigDecimal.valueOf(50), totalPrice);
    }

    @Test
    void testFillOrderResponse() {
        Order order = new Order();
        order.setOrderId(1);
        order.setTotalPrice(BigDecimal.valueOf(50));
        order.setOrderDate(LocalDate.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        OrderResponse response = orderService.fillOrderResponse(order);

        assertNotNull(response);
        assertEquals(1, response.getOrderId());
        assertEquals(BigDecimal.valueOf(50), response.getTotalPrice());
        assertEquals(LocalDate.now(), response.getOrderDate());
        assertEquals(order.getCreatedAt(), response.getCreatedAt());
        assertEquals(order.getUpdatedAt(), response.getUpdatedAt());
    }
}
