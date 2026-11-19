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

    // AC3.1.1: a user registered through sign-up logs in, also in another username case.
    @Test
    void userRegisteredThroughSignupLogsInWith200() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"LoginUser\",\"password\":\"pw-login-1234\"}"))
                .andExpect(status().isCreated());
        for (String name : new String[] {"LoginUser", "LOGINUSER"}) {
            mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"" + name + "\",\"password\":\"pw-login-1234\"}"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"success\":true}", true));
        }
    }

    // AC3.1.2, AC3.1.3: wrong password and unknown username give the same 401 body.
    @Test
    void wrongPasswordAndUnknownUsernameGiveIdentical401() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"SameFail\",\"password\":\"pw-same-1234\"}"))
                .andExpect(status().isCreated());
        MvcResult wrongPassword = login("{\"username\":\"SameFail\",\"password\":\"pw-other-1234\"}");
        MvcResult unknownUser = login("{\"username\":\"NoSuchUser\",\"password\":\"pw-same-1234\"}");
        assertEquals(401, wrongPassword.getResponse().getStatus());
        assertEquals(401, unknownUser.getResponse().getStatus());
        assertEquals(wrongPassword.getResponse().getContentAsString(),
                unknownUser.getResponse().getContentAsString());
        assertEquals(FAILURE, wrongPassword.getResponse().getContentAsString());
    }

    // AC3.2.1: the demo credential still logs in after other users have signed up.
    @Test
    void demoCredentialStillLogsInAfterOtherSignups() throws Exception {
        for (String name : new String[] {"DemoSideOne", "DemoSideTwo"}) {
            mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"" + name + "\",\"password\":\"pw-side-1234\"}"))
                    .andExpect(status().isCreated());
        }
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"demopassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}", true));
    }

    // AC3.2.1: the demo match stays exact and case-sensitive (name and password).
    @Test
    void demoCredentialInAnotherCaseReturns401() throws Exception {
        for (String body : new String[] {"{\"username\":\"DEMOUSER\",\"password\":\"demopassword\"}",
                "{\"username\":\"demouser\",\"password\":\"DEMOPASSWORD\"}"}) {
            mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(FAILURE, true));
        }
    }

    // AC3.2.1, AC3.2.2: signing up with the demo name is refused and the demo login is unchanged.
    @Test
    void signupWithDemoNameIsRefusedAndDemoStillLogsIn() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"pw-take-1234\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"success\":false,\"errors\":[\"USERNAME_TAKEN\"]}", true));
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"demopassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}", true));
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demouser\",\"password\":\"pw-take-1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(FAILURE, true));
    }

    // AC3.3.1: a registered user's login issues no cookie, Authorization header or token.
    @Test
    void registeredUserLoginHasNoCookieOrToken() throws Exception {
        mvc.perform(post("/api/signup").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"PlainLogin\",\"password\":\"pw-plain-1234\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"PlainLogin\",\"password\":\"pw-plain-1234\"}"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Set-Cookie"))
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    // AC3.1.4: null fields are still a malformed request.
    @Test
    void nullFieldsStillReturn400() throws Exception {
        for (String body : new String[] {"{\"username\":null,\"password\":\"x\"}",
                "{\"username\":\"x\",\"password\":null}"}) {
            mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(FAILURE, true));
        }
    }
}
