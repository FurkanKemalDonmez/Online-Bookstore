package com.bookstore.api;

import com.bookstore.api.entity.Book;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.request.AddBookRequest;
import com.bookstore.api.request.UpdateBookRequest;
import com.bookstore.api.service.BookService;
import com.bookstore.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setup() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        ReflectionTestUtils.setField(bookService, "bookRepository", bookRepository);
        ReflectionTestUtils.setField(bookService, "orderService", orderService);
    }

    @Test
    void testAddBook() {
        AddBookRequest request = new AddBookRequest(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54);

        ResponseEntity<?> response = bookService.addBook(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBookByIsbn() {
        int isbn = 1;
        when(bookRepository.findById(isbn)).thenReturn(Optional.of(new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now())));

        ResponseEntity<?> response = bookService.getBookByIsbn(isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateBook() {
        UpdateBookRequest request = new UpdateBookRequest("exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54);
        int isbn = 1;
        when(bookRepository.findById(isbn)).thenReturn(Optional.of(new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now())));

        ResponseEntity<?> response = bookService.updateBook(request, isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteBook() {
        int isbn = 1;
        when(bookRepository.findById(isbn)).thenReturn(Optional.of(new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now())));

        ResponseEntity<String> response = bookService.deleteBook(isbn);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testGetAllBooksSortedByCreatedAtDesc() {

        Book theBook = new Book(1,"exampleTıtle","exampleAuthor", BigDecimal.valueOf(25),54, LocalDateTime.now(),LocalDateTime.now());
        List<Book> bookList = new ArrayList<>();
        bookList.add(theBook);
        Page<Book> samplePage = new PageImpl<>( bookList, PageRequest.of(0, 10), 10);
        when(bookRepository.findAllByOrderByCreatedAtDesc(any())).thenReturn(samplePage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> result = bookService.getAllBooksSortedByCreatedAtDesc(pageable);

        assertEquals(10, result.getTotalElements());
    }
}
