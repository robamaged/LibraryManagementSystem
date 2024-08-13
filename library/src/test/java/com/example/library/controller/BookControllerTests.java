package com.example.library.controller;

import com.example.library.book.Book;
import com.example.library.book.BookController;
import com.example.library.book.BookRepository;
import com.example.library.book.BookService;
import com.example.library.config.ApplicationConfig;
import com.example.library.config.JwtService;
import com.example.library.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
//@WebMvcTest(controllers = BookController.class)
@Import(ApplicationConfig.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookService bookService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper = new ObjectMapper();


    private Book book;


    @BeforeEach
    public void setUp() throws Exception {
        book = new Book("Title1", "Author1", 2021, "1234567890");
        // Mock data
        List<Book> books = List.of(
                new Book("Title1", "Author1", 2021, "1234567890"),
                new Book("Title2", "Author2", 2021, "1234567891"),
                new Book("Title3", "Author3", 2021, "1234567892")
        );

        when(bookService.getAllBooks()).thenReturn(books);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void getAllBooks_success() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }



    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void postBook_success() throws Exception {

        Book book = new Book("New Title", "New Author", 2024, "1234567893");


        String bookJson = objectMapper.writeValueAsString(book);


        when(bookService.saveBook(any(Book.class))).thenReturn(book);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
                        .with(csrf())) // Include CSRF token if CSRF protection is enabled
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("New Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234567893"));
    }




    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void getBookById_success() throws Exception {
        // Mock the service to return a book when findBookById is called
        when(bookService.getBookById(1L)).thenReturn(book);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2021))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234567890"));
    }




    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void deleteBook_success() throws Exception {
        Long bookId = 1L;

        // Mock the bookService delete method
        doNothing().when(bookService).deleteBook(bookId);

        // Perform the DELETE request with CSRF token
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/books/{id}", bookId)
                        .with(csrf())) // Include CSRF token
                .andExpect(MockMvcResultMatchers.status().isOk()); // Expect status 200 OK
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void updateBook_success() throws Exception {
        Long bookId = 1L;

        // Create a book with updated details
        Book updatedBook = new Book("Updated Title", "Updated Author", 2024, "1234567894");

        // Mock the bookService update method
        when(bookService.updateBook(eq(bookId), any(Book.class))).thenReturn(updatedBook);

        // Convert updated book object to JSON
        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        // Perform the PUT request with CSRF token
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson)
                        .with(csrf())) // Include CSRF token
                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect status 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Updated Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234567894"));
    }




}