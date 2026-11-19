package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// AC3.3.3: handling sign-up and login never writes the typed password to the console.
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class ConsoleOutputTest {

    private static final String PASSWORD = "pw-console-distinct-5821";

    @Autowired
    private MockMvc mvc;

    @Test
    void signupAndLoginNeverPrintThePassword(CapturedOutput output) throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ConsoleUser\",\"password\":\"" + PASSWORD + "\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ConsoleUser\",\"password\":\"" + PASSWORD + "\"}"))
                .andExpect(status().isOk());
        // A failed attempt with the same password is also kept out of the output.
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"NobodyHere\",\"password\":\"" + PASSWORD + "\"}"))
                .andExpect(status().isUnauthorized());
        assertFalse(output.getAll().contains(PASSWORD));
    }
}
