package kr.ac.kumoh.illdang100.tovalley.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("인증 테스트")
    void authentication_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/test"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();

        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);

        //then
        assertThat(httpStatusCode).isEqualTo(401);
    }

    @Test
    @DisplayName("인가 테스트")
    void authorization_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/admin/test"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();

        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);

        //then
        assertThat(httpStatusCode).isEqualTo(401);

    }
}
