package com.example.library.controller;



import com.example.library.Borrowing.BorrowingRecord;
import com.example.library.Borrowing.BorrowingRecordController;
import com.example.library.Borrowing.BorrowingRecordService;
import com.example.library.Patron.Patron;
import com.example.library.Patron.PatronRepository;
import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.config.ApplicationConfig;
import com.example.library.config.JwtService;
import com.example.library.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(BorrowingRecordController.class)
@Import(ApplicationConfig.class)
public class BorrowingRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private PatronRepository patronRepository;
    @MockBean
    private UserRepository userRepository;


    @MockBean
    private JwtService jwtService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Test Patron");

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testReturnBook() throws Exception {
        borrowingRecord.setReturnDate(LocalDate.now());

        Mockito.when(borrowingRecordService.returnBook(anyLong(), anyLong())).thenReturn(borrowingRecord);

        mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(borrowingRecord.getId().intValue())))
                .andExpect(jsonPath("$.returnDate", is(borrowingRecord.getReturnDate().toString())));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testBorrowBook_BookAlreadyBorrowed() throws Exception {
        Mockito.when(borrowingRecordService.borrowBook(anyLong(), anyLong()))
                .thenThrow(new IllegalStateException("Book is already borrowed by someone else."));

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Book is already borrowed by someone else.")));
    }




    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testBorrowBook() throws Exception {
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        Mockito.when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        Mockito.when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenReturn(borrowingRecord);

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(borrowingRecord.getId().intValue())))
                .andExpect(jsonPath("$.book.title", is(book.getTitle())))
                .andExpect(jsonPath("$.patron.name", is(patron.getName())));
    }
}



