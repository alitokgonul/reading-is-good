package com.getir.readingisgood.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getir.readingisgood.controller.model.AuthorDTO;
import com.getir.readingisgood.controller.model.BookDTO;
import com.getir.readingisgood.entity.Author;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String BOOK_ENDPOINT = "/api/v1/books";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService, modelMapper)).build();
    }

    @Test
    void save_success() throws Exception {
        // given
        final BookDTO bookDTO = createBookDTO(2, new BigDecimal(12), "name");
        given(bookService.save(any(Book.class))).willReturn(bookDTO);
        given(modelMapper.map(any(), any())).willReturn(createBook());
        String createRequestStr = objectMapper.writeValueAsString(bookDTO);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BOOK_ENDPOINT + "/save").contentType(MediaType.APPLICATION_JSON)
                                                                           .content(createRequestStr))
                                     .andExpect(status().isOk())
                                     .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        BookDTO dto = objectMapper.readValue(contentAsString, BookDTO.class);

        assertEquals(dto.getQuantity(), 2);
        assertNotNull(dto.getAuthor());
        assertEquals(dto.getAuthor().getName(), "name");
        assertEquals(dto.getPrice(), new BigDecimal(12));
    }

    @Test
    void save_invalid_quantity() throws Exception {
        // given
        final BookDTO bookDTO = createBookDTO(-2, new BigDecimal(12), "name");
        String createRequestStr = objectMapper.writeValueAsString(bookDTO);

        // when / then
        mockMvc.perform(post(BOOK_ENDPOINT + "/save").contentType(MediaType.APPLICATION_JSON).content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void save_invalid_price() throws Exception {
        // given
        final BookDTO bookDTO = createBookDTO(2, null, "name");
        String createRequestStr = objectMapper.writeValueAsString(bookDTO);

        // when / then
        mockMvc.perform(post(BOOK_ENDPOINT + "/save").contentType(MediaType.APPLICATION_JSON).content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void save_invalid_author_name() throws Exception {
        // given
        final BookDTO bookDTO = createBookDTO(2, new BigDecimal(1.7), null);
        String createRequestStr = objectMapper.writeValueAsString(bookDTO);

        // when / then
        mockMvc.perform(post(BOOK_ENDPOINT + "/save").contentType(MediaType.APPLICATION_JSON).content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void update_success() throws Exception {
        // given
        final BookDTO bookDTO = createBookDTO(2, new BigDecimal(12), "name");
        given(bookService.update(anyLong(), anyInt())).willReturn(bookDTO);

        // when
        MvcResult mvcResult =
            mockMvc.perform(put(BOOK_ENDPOINT + "/update/1?quantity=2").contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        BookDTO dto = objectMapper.readValue(contentAsString, BookDTO.class);

        assertEquals(dto.getQuantity(), 2);
        assertNotNull(dto.getAuthor());
        assertEquals(dto.getAuthor().getName(), "name");
        assertEquals(dto.getPrice(), new BigDecimal(12));
    }

    @Test
    void update_without_quantity() throws Exception {
        // when / then
        mockMvc.perform(put(BOOK_ENDPOINT + "/update/1").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    private BookDTO createBookDTO(Integer quantity, BigDecimal price, String authorName) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("book name");
        bookDTO.setPrice(price);
        bookDTO.setQuantity(quantity);
        bookDTO.setPublishDate(LocalDate.of(1999, 12, 12));

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName(authorName);
        authorDTO.setLastName("last name");

        bookDTO.setAuthor(authorDTO);

        return bookDTO;
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