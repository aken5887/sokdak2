package com.project.sokdak2.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("/download 요청시 파일이 없는 경우에는 FileNotFoundException이 발생한다.")
    @Test
    void download_exception() throws Exception {
        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.get("/download/1"))
                
                .andExpect(status().isOk())
                .andExpect(view().name("/errors/error"))
                .andExpect(model().attribute("error", hasProperty("code", is("404"))));
    }

}