package com.project.sokdak2.api.config;

import com.project.sokdak2.api.domain.common.Visits;
import com.project.sokdak2.api.repository.VisitsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class VisitInterceptorTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    VisitsRepository visitsRepository;

    @BeforeEach
    void clean(){
        this.visitsRepository.deleteAll();
    }

    @DisplayName("/resume 요청시 Visits에 기록된다.")
    @Test
    void test() throws Exception{
        // when
        this.mockMvc.perform(get("/resume"))
                .andExpect(status().isOk());
        // then
        Assertions.assertThat(this.visitsRepository.count())
                .isEqualTo(1);
    }

    @DisplayName("visits 엔티티에 요청한 uri 대로 저장된다.")
    @Test
    void test2() throws Exception {
        //given
        String uri = "/resume";
        // when
        this.mockMvc.perform(get(uri))
                .andExpect(status().isOk());
        // then
        List<Visits> visits
                = this.visitsRepository.findAllByUriContaining(uri);
        Assertions.assertThat(visits.isEmpty()).isFalse();
        Assertions.assertThat(visits.get(0).getUri()).startsWith(uri);
    }

    @DisplayName("js, css, png는 제외된다.")
    @Test
    void test3()  throws Exception {
        // css uri
        String uri = "/css/css.css";

        // when
        this.mockMvc.perform(get(uri))
                .andExpect(status().isOk());
        // then
        Assertions.assertThat(this.visitsRepository.count())
                .isEqualTo(0);
    }
}
