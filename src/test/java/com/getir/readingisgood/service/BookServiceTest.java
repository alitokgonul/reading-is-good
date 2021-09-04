package com.getir.readingisgood.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.getir.readingisgood.controller.model.BookDTO;
import com.getir.readingisgood.entity.Author;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void save_success() {
        // given
        final Book book = createBook();
        given(bookRepository.findByName(anyString())).willReturn(null);
        given(bookRepository.save(any())).willReturn(book);

        // when
        BookDTO bookDTO = bookService.save(book);

        // then
        assertEquals(bookDTO.getId(), 1L);
        assertEquals(bookDTO.getPrice(), book.getPrice());
        assertNotNull(book.getAuthor());
        verify(bookRepository).save(book);
        verify(bookRepository).findByName(anyString());
    }

    @Test
    void save_fail() {
        // given
        final Book book = createBook();
        given(bookRepository.findByName(anyString())).willReturn(book);

        // when / then
        assertThrows(CustomException.class, () -> bookService.save(book));
        verify(bookRepository).findByName(anyString());
    }

    @Test
    void update_success() {
        // given
        final Book book = createBook();
        final Integer newQuantity = 4;
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
        given(bookRepository.save(any())).willReturn(book);

        // when
        BookDTO bookDTO = bookService.update(1L, newQuantity);

        // then
        assertEquals(bookDTO.getId(), 1L);
        assertEquals(bookDTO.getPrice(), book.getPrice());
        assertEquals(bookDTO.getQuantity(), newQuantity);
        assertNotNull(book.getAuthor());
        verify(bookRepository).save(book);
    }

    @Test
    void update_fail() {
        // given
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThrows(CustomException.class, () -> bookService.update(1L, 5));
        verify(bookRepository).findById(anyLong());
    }

    private Book createBook() {
        Book book = new Book();
        book.setName("book name");
        book.setPrice(new BigDecimal(12));
        book.setQuantity(3);
        book.setId(1L);
        book.setPublishDate(LocalDate.of(1999, 12, 12));

        Author author = new Author();
        author.setName("name");
        author.setLastName("last name");
        book.setAuthor(author);

        return book;
    }
}