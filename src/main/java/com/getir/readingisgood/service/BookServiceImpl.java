package com.getir.readingisgood.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.getir.readingisgood.controller.model.BookDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookDTO save(final Book book) {
        if (bookRepository.findByName(book.getName()) != null) {
            throw new CustomException("Book is already in use, quantity could be updated", HttpStatus.BAD_REQUEST);
        }
        return modelMapper.map(bookRepository.save(book), BookDTO.class);
    }

    @Override
    @Transactional
    public BookDTO update(final Long id, final Integer quantity) {
        Book book = bookRepository.findById(id)
                                  .orElseThrow(() -> new CustomException("Book could not be found in db.",
                                                                         HttpStatus.BAD_REQUEST));

        book.setQuantity(quantity);
        return modelMapper.map(bookRepository.save(book), BookDTO.class);
    }

    @Override
    public Optional<Book> getById(final Long id) {
        return bookRepository.findById(id);
    }
}
