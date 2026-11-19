package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerTest {

    private static final String FAILURE = "{\"success\":false}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserStore store;

    private MvcResult signup(String body) throws Exception {
        return mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andReturn();
    }

    @Test
    void validSignupReturns201Success() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser1\",\"password\":\"pw-test-1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"success\":true}", true));
    }

    @Test
    void createdUserExistsInTheStore() throws Exception {
        signup("{\"username\":\"StoredUser\",\"password\":\"pw-test-1234\"}");
        RegisteredUser stored = store.findByKey("storeduser");
        assertNotNull(stored);
        assertEquals("StoredUser", stored.username());
    }

    @Test
    void nullUsernameReturns400() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":null,\"password\":\"pw-test-1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void nullPasswordReturns400() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"nullpw1\",\"password\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
        assertNull(store.findByKey("nullpw1"));
    }

    @Test
    void invalidUsernameReturns400WithUsernameCode() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ab\",\"password\":\"pw-test-1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"success\":false,\"errors\":[\"USERNAME_INVALID\"]}", true));
        assertNull(store.findByKey("ab"));
    }

    @Test
    void invalidPasswordReturns400WithPasswordCode() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shortpw1\",\"password\":\"1234567\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"success\":false,\"errors\":[\"PASSWORD_INVALID\"]}", true));
        assertNull(store.findByKey("shortpw1"));
    }

    @Test
    void bothRulesFailingReturn400WithBothCodesInOrder() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"bad name\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        "{\"success\":false,\"errors\":[\"USERNAME_INVALID\",\"PASSWORD_INVALID\"]}", true));
        assertNull(store.findByKey("bad name"));
    }

    @Test
    void nullFieldsStay400WithoutErrors() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":null,\"password\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void createdAnswerHasNoErrorsField() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"clean1\",\"password\":\"pw-test-1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void malformedJsonReturns400() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON).content("not json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void wrongContentTypeIsRefused() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.TEXT_PLAIN)
                        .content("{\"username\":\"plain1\",\"password\":\"pw-test-1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
        assertNull(store.findByKey("plain1"));
    }

    @Test
    void noCookieOrTokenAndPasswordNeverInTheAnswer() throws Exception {
        MvcResult result = mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"quiet1\",\"password\":\"pw-secret-9876\"}"))
                .andExpect(header().doesNotExist("Set-Cookie"))
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("pw-secret-9876"));
    }

    @Test
    void corsAllowedForConfiguredOriginOnly() throws Exception {
        String body = "{\"username\":\"corsuser1\",\"password\":\"pw-test-1234\"}";
        mvc.perform(options("/api/signup").header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
        mvc.perform(post("/api/signup").header("Origin", "http://localhost:5173")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
        mvc.perform(post("/api/signup").header("Origin", "http://evil.example")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
