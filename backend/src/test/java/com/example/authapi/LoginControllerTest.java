package com.example.authapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class LoginControllerTest {

    private static final String FAILURE = "{\"success\":false}";

    @Autowired
    private MockMvc mvc;

    private MvcResult login(String body) throws Exception {
        return mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
    }

    @Test
    void validCredentialReturns200Success() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"demopassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}", true));
    }

    @Test
    void wrongCredentialReturns401Failure() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"nope\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void emptyValuesReturn401() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void missingBodyReturns400() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void nonJsonBodyReturns400() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content("not json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void wrongShapeReturns400() throws Exception {
        // Missing field, wrong field type, and an array instead of an object.
        for (String body : new String[] {"{\"username\":\"demouser\"}",
                "{\"username\":123,\"password\":\"demopassword\"}", "[1,2]"}) {
            mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(FAILURE, true));
        }
    }

    @Test
    void wrongContentTypeReturns400() throws Exception {
        mvc.perform(post("/api/login").contentType(MediaType.TEXT_PLAIN)
                        .content("{\"username\":\"demouser\",\"password\":\"demopassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(FAILURE, true));
    }

    @Test
    void failureBodyIsIdenticalForEveryCauseAndNamesNoField() throws Exception {
        String wrong = login("{\"username\":\"x\",\"password\":\"y\"}").getResponse().getContentAsString();
        String empty = login("{\"username\":\"\",\"password\":\"\"}").getResponse().getContentAsString();
        String malformed = login("garbage").getResponse().getContentAsString();
        assertEquals(wrong, empty);
        assertEquals(wrong, malformed);
        assertEquals(FAILURE, wrong);
    }

    @Test
    void noCookieOrTokenOnSuccessOrFailure() throws Exception {
        for (String body : new String[] {"{\"username\":\"demouser\",\"password\":\"demopassword\"}",
                "{\"username\":\"a\",\"password\":\"b\"}"}) {
            mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(header().doesNotExist("Set-Cookie"))
                    .andExpect(header().doesNotExist("Authorization"))
                    .andExpect(jsonPath("$.token").doesNotExist());
        }
    }

    @Test
    void corsAllowedForConfiguredOriginOnly() throws Exception {
        String body = "{\"username\":\"demouser\",\"password\":\"demopassword\"}";
        mvc.perform(post("/api/login").header("Origin", "http://localhost:5173")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
        mvc.perform(post("/api/login").header("Origin", "http://evil.example")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
