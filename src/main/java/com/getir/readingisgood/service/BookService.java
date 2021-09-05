package com.getir.readingisgood.service;

import java.util.Optional;

import com.getir.readingisgood.controller.model.BookDTO;
import com.getir.readingisgood.entity.Book;

public interface BookService {

    BookDTO save(final Book book);

    BookDTO update(final Long id, final Integer quantity);

    Optional<Book> getById(Long id);
}
