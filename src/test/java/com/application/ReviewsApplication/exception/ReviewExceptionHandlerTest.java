package com.application.ReviewsApplication.exception;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testInvalidRatingParamType() throws Exception {
        mockMvc.perform(get("/api/reviews")
                        .param("rating", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid input parameter")));
    }

    @Test
    void testInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/reviews")
                        .param("date", "wrong-format"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid date format")));
    }

}
