package com.project.sokdak2.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

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