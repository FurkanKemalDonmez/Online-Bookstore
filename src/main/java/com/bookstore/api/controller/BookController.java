package com.bookstore.api.controller;

import com.bookstore.api.entity.Book;
import com.bookstore.api.enums.ErrorMessage;
import com.bookstore.api.request.AddBookRequest;
import com.bookstore.api.request.UpdateBookRequest;
import com.bookstore.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @Operation(summary = "Get All Books", description = "Retrieves a list of all books ordered by creation date DESC.")
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam @Positive int page,
            @RequestParam @Positive int size
    ) {

        if (page <= 0 || size <= 0) {
            return ResponseEntity.badRequest().body(ErrorMessage.INVALID_PAGE_SIZE_REQUEST.getMessage());
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Book> books = bookService.getAllBooksSortedByCreatedAtDesc(pageable);

        if (page > books.getTotalPages()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.NO_DATA.getMessage());
        }

        return ResponseEntity.ok(books.getContent());

    }

    @Operation(summary = "Get Book By Isbn", description = "Retrieves details of a book by ISBN.")
    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable Integer isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @Operation(summary = "Add Book", description = "Adds a new book.")
    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody AddBookRequest bookRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ErrorMessage.ADD_BOOK_VALIDATION_ERRORS.getMessage());
        }

        return bookService.addBook(bookRequest);
    }

    @Operation(summary = "Update Book", description = "Updates details of a book.")
    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody UpdateBookRequest bookRequest, BindingResult bindingResult, @PathVariable Integer isbn) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ErrorMessage.UPDATE_BOOK_VALIDATION_ERRORS.getMessage());
        }

        return bookService.updateBook(bookRequest, isbn);
    }

    @Operation(summary = "Delete Book", description = "Deletes a book by ISBN.")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer isbn) {
        return bookService.deleteBook(isbn);
    }
}
