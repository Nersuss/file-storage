package ru.nersus.storage.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.nersus.storage.TestcontainersConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AuthControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void registrationAndAuthentication_Success() throws Exception {
        //given
        mockMvc.perform(post("/api/auth/sign-up")
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

                );
        //given
        mockMvc.perform(post("/api/auth/sign-in")
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
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "username" : "user"
                                }
                                """),
                        cookie().exists("session")

                );
    }

    @Disabled
    @Test
    void registration_Error() throws Exception {
        //given
        mockMvc.perform(post("/api/auth/sign-up")
        //when
                        .content("""
                                {
                                    "username" : "q",
                                    "password" : "12345"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
        //then
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    void authentication_Error() throws Exception {
        //given
        mockMvc.perform(post("/api/auth/sign-in")
                        //when
                        .content("""
                                {
                                    "username" : "notExists",
                                    "password" : "void"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpectAll(
                        status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "message" : "User with email: notExists doesn't exists"
                                }
                                """)
                );
    }

    @Test
    void logout() {
    }
}