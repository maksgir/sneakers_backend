package com.maksgir.sneakers.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksgir.sneakers.domain.Seller;
import com.maksgir.sneakers.domain.User;
import com.maksgir.sneakers.repository.SellerRepository;
import com.maksgir.sneakers.repository.UserRepository;
import com.maksgir.sneakers.service.dto.MessageResponse;
import com.maksgir.sneakers.service.dto.SignupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @MockBean
    private PasswordEncoder encoder;

    @AfterEach
    public void clear() {
        sellerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void allAccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/test/all")
                )
                .andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Public content",json);
    }

    @Test
    public void userAccess_denied() throws Exception {
        mockMvc.perform(
                        get("/api/test/user")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void sellerAccess_denied() throws Exception {
        mockMvc.perform(
                        get("/api/test/seller")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registration_seller() throws Exception {

        SignupRequest request = new SignupRequest(
                "valera", "valera@mail.ru",
                "456", "Valera",
                "Bariga", "+7952812455",
                true
        );

        Mockito.when(encoder.encode("456")).thenReturn("444555666");

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        MessageResponse messageResponse = objectMapper.readValue(json, MessageResponse.class);

        Assertions.assertEquals("User registered successfully!", messageResponse.message());

        List<User> users = userRepository.findAll();
        List<Seller> sellers = sellerRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(1, sellers.size());

        User user = users.get(0);
        Assertions.assertEquals(request.firstName(), user.getFirstName());
        Assertions.assertEquals(request.lastName(), user.getLastName());
        Assertions.assertEquals(request.username(), user.getUsername());
        Assertions.assertEquals(request.email(), user.getEmail());
        Assertions.assertEquals("444555666", user.getPassword());
        Assertions.assertEquals(request.phone(), user.getPhone());

        Assertions.assertEquals(sellers.get(0).getUserId(), user.getId());
    }

    @Test
    public void registration_user() throws Exception {

        SignupRequest request = new SignupRequest(
                "valera", "valera@mail.ru",
                "456", "Valera",
                "Bariga", "+7952812455",
                false
        );

        Mockito.when(encoder.encode("456")).thenReturn("444555666");

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        MessageResponse messageResponse = objectMapper.readValue(json, MessageResponse.class);

        Assertions.assertEquals("User registered successfully!", messageResponse.message());

        List<User> users = userRepository.findAll();
        List<Seller> sellers = sellerRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(0, sellers.size());
    }
}
