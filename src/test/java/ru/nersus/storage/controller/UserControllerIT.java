package ru.nersus.storage.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUser_Success() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/sign-up")
                        //when
                        .content("""
                                {
                                    "username" : "user",
                                    "password" : "12345"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "username" : "user"
                                }
                                """),
                        cookie().exists("session")
                )
                .andReturn();
        Cookie session = result.getResponse().getCookie("session");
        assertNotNull(session);

        mockMvc.perform(get("/api/user/me")
                        //when
                        .cookie(session))
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "username" : "user"
                                }
                                """)
                );
    }
}