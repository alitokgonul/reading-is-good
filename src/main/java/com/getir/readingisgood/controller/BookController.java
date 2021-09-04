package com.getir.readingisgood.controller;

import javax.validation.Valid;

import com.getir.readingisgood.controller.model.BookDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Api(tags = { "Book" })
@SwaggerDefinition(tags = { @Tag(name = "Book", description = "Book management") })
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "Add new book")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The new book is successfully added."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @PostMapping("/save")
    public ResponseEntity<BookDTO> save(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.save(modelMapper.map(bookDTO, Book.class)));
    }

    @ApiOperation(value = "Update book's stock")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The book is successfully updated."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @PutMapping("/update/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(bookService.update(id, quantity));
    }
}
