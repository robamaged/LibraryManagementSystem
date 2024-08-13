package com.example.library.controller;



import com.example.library.Patron.Patron;
import com.example.library.Patron.PatronController;
import com.example.library.Patron.PatronService;
import com.example.library.book.BookService;
import com.example.library.config.ApplicationConfig;
import com.example.library.config.JwtService;
import com.example.library.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PatronController.class)
@Import(ApplicationConfig.class)
public class PatronControllerTests {




        @MockBean
        private UserRepository userRepository;


        @MockBean
        private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Patron patron;

    @BeforeEach
    public void setUp() {
        patron = new Patron("John Doe", "johndoe@example.com");
        // Mock data
        when(patronService.getAllPatrons()).thenReturn(List.of(
                new Patron("John Doe", "johndoe@example.com"),
                new Patron("Jane Smith", "janesmith@example.com")
        ));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void getAllPatrons_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void getPatronById_success() throws Exception {
        when(patronService.getPatronById(1L)).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/patrons/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.contactInformation").value("johndoe@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void createPatron_success() throws Exception {
        Patron newPatron = new Patron("New Patron", "newpatron@example.com");

        when(patronService.savePatron(any(Patron.class))).thenReturn(newPatron);

        String patronJson = objectMapper.writeValueAsString(newPatron);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson)
                        .with(csrf())) // Include CSRF token if CSRF protection is enabled
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Patron"))
                .andExpect(jsonPath("$.contactInformation").value("newpatron@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void updatePatron_success() throws Exception {
        Long patronId = 1L;
        Patron updatedPatron = new Patron("Updated Patron", "updatedpatron@example.com");

        when(patronService.updatePatron(eq(patronId), any(Patron.class))).thenReturn(updatedPatron);

        String updatedPatronJson = objectMapper.writeValueAsString(updatedPatron);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPatronJson)
                        .with(csrf())) // Include CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Patron"))
                .andExpect(jsonPath("$.contactInformation").value("updatedpatron@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void deletePatron_success() throws Exception {
        Long patronId = 1L;

        doNothing().when(patronService).deletePatron(patronId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/patrons/{id}", patronId)
                        .with(csrf())) // Include CSRF token
                .andExpect(status().isNoContent());
    }
}
