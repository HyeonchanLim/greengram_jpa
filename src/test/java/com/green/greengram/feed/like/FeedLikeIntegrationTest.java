package com.green.greengram.feed.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.BaseIntegrationTest;
import com.green.greengram.WithAuthUser;
import com.green.greengram.feed.like.model.FeedLikeReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithAuthUser(signedUserId = 2L)
public class FeedLikeIntegrationTest extends BaseIntegrationTest {
    @Autowired
    ObjectMapper objectMapper; //JSON사용
    @Autowired
    MockMvc mockMvc; //요청(보내고)-응답(받기) 처리
    @MockBean
    FeedLikeService feedLikeService; //가짜 객체를 만들고 빈등록한다.

    final String BASE_URL = "/api/feed/like";
    final long feedId_2 = 2L;
    FeedLikeTestCommon common;

    @BeforeEach
    void setUp(){
        common = new FeedLikeTestCommon(objectMapper);
    }

    @Test
    @DisplayName("좋아요 등록")
    void feedLikeReg() throws Exception {
        final int regSuccessResult = 1;
        final long feedIdNotExisted = 5L;
        feedLikeToggle(regSuccessResult,feedIdNotExisted);
    }

    @Test
    @DisplayName("좋아요 취소")
    void feedLikeCancel() throws Exception {
        final int cancelSuccessResult = 0;
        final long feedIdExisted = 2L;
        feedLikeToggle(cancelSuccessResult,feedIdExisted);
    }
     void feedLikeToggle(final int result ,final long feedId) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(BASE_URL).queryParams(common.getParameter(feedId)));

        String expectedResJson = common.getExpectedResJson(result);
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

    }
}
