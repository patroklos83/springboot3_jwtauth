package com.patroclos.springboot3.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.patroclos.springboot3.pojo.Book;
import com.patroclos.springboot3.repository.BookRepository;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Book", description = "The Book API. Contains all the operations that can be performed on a book.")
@RequestMapping("/api/book")
public class BookController {

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found the book", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = Book.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
        content = @Content), 
      @ApiResponse(responseCode = "404", description = "Book not found", 
        content = @Content) })
    @GetMapping("/{id}")
    public Book findById(@PathVariable long id) {
    	Book book1 = new Book();
    	book1.setAuthor("test");
    	book1.setBookName("testBook");
        return book1;
    }

    @GetMapping("/")
    public Collection<Book> findBooks() {
    	Book book1 = new Book();
    	book1.setAuthor("test");
    	book1.setBookName("testBook");
    	
    	Book book2 = new Book();
    	book2.setAuthor("test");
    	book2.setBookName("testBook2");
        return List.of(new Book[] { book1, book2 });
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(
      @PathVariable("id") final String id, @RequestBody final Book book) {
        return book;
    }
}