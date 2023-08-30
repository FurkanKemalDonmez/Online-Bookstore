package com.bookstore.api.service;

import com.bookstore.api.entity.Book;
import com.bookstore.api.entity.Order;
import com.bookstore.api.enums.ErrorMessage;
import com.bookstore.api.repository.BookRepository;
import com.bookstore.api.request.AddBookRequest;
import com.bookstore.api.request.UpdateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookService {


    private final BookRepository bookRepository;

    private final OrderService orderService;

    @Autowired
    public BookService(BookRepository bookRepository, OrderService orderService) {
        this.bookRepository = bookRepository;
        this.orderService = orderService;
    }

    public ResponseEntity<?> addBook(AddBookRequest bookRequest) {

        Optional<Book> optionalBook = bookRepository.findById(bookRequest.getIsbn());

        if (optionalBook.isPresent()) {
            return ResponseEntity.badRequest().body(ErrorMessage.BOOK_ALREADY_EXIST.getMessage());
        } else {

            Book theBook = new Book();

            theBook.setIsbn(bookRequest.getIsbn());
            theBook.setTitle(bookRequest.getTitle());
            theBook.setAuthor(bookRequest.getAuthor());
            theBook.setPrice(bookRequest.getPrice());
            theBook.setStockQuantity(bookRequest.getStockQuantity());
            theBook.setCreatedAt(LocalDateTime.now());
            theBook.setUpdatedAt(LocalDateTime.now());

            return ResponseEntity.ok(bookRepository.save(theBook));
        }
    }

    public ResponseEntity<?> getBookByIsbn(Integer isbn) {

        Optional<Book> optionalBook = bookRepository.findById(isbn);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.BOOK_NOT_FOUND.getMessage());
        }
    }

    public ResponseEntity<?> updateBook(UpdateBookRequest bookRequest, Integer isbn) {

        Optional<Book> optionalBook = bookRepository.findById(isbn);

        if (optionalBook.isPresent()) {
            Book theBook = optionalBook.get();

            if (bookRequest.getTitle() != null) {
                theBook.setTitle(bookRequest.getTitle());
            }
            if (bookRequest.getAuthor() != null) {
                theBook.setAuthor(bookRequest.getAuthor());
            }
            if (bookRequest.getPrice() != null) {
                theBook.setPrice(bookRequest.getPrice());
            }
            if (bookRequest.getStockQuantity() != null) {
                theBook.setStockQuantity(bookRequest.getStockQuantity());
            }

            theBook.setUpdatedAt(LocalDateTime.now());

            if (theBook.getOrders() != null){
                for (Order order : theBook.getOrders()) {
                    order.setTotalPrice(orderService.calculateTotalPrice(order));
                    order.setUpdatedAt(LocalDateTime.now());
                }
            }


            return ResponseEntity.ok(bookRepository.save(theBook));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.BOOK_NOT_FOUND.getMessage());
        }
    }

    public ResponseEntity<String> deleteBook(Integer isbn) {

        try {
            Book bookToDelete = bookRepository.findById(isbn).get();

            for (Order order : bookToDelete.getOrders()) {
                order.getListOfBooks().remove(bookToDelete);
                order.setTotalPrice(orderService.calculateTotalPrice(order));
                order.setUpdatedAt(LocalDateTime.now());
            }
            bookRepository.delete(bookToDelete);
            return ResponseEntity.ok(ErrorMessage.BOOK_DELETED.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.BOOK_NOT_FOUND.getMessage());
        }
    }

    public Page<Book> getAllBooksSortedByCreatedAtDesc(Pageable pageable) {
        return bookRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
