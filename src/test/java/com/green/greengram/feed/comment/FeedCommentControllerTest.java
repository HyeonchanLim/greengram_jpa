package com.green.greengram.feed.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.feed.comment.model.*;
import com.green.greengram.feed.like.model.FeedLikeReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers =  FeedCommentController.class
        , excludeAutoConfiguration =  SecurityAutoConfiguration.class
)
class FeedCommentControllerTest {
    final String URL = "/api/feed/comment";
    // key = value
    // feed_id=2 & start_idx=1 & size=10

    @Autowired ObjectMapper objectMapper;
    @Autowired MockMvc mockMvc;
    @MockBean FeedCommentService feedCommentService;

    final long feedId_2 = 2L;
    final long feedCommentId_3 = 3L;
    final long writerUserId__4 = 4L;
    final long SIZE = 20L;
    final String BASE_URL = "/api/feed/comment";

    @Test
    void getFeedComment() throws Exception{
        FeedCommentGetReq givenParam = new FeedCommentGetReq(feedId_2,1,20);


        FeedCommentDto feedCommentDto = new FeedCommentDto();
        feedCommentDto.setFeedId(feedId_2);
        feedCommentDto.setFeedCommentId(feedCommentId_3);
        feedCommentDto.setComment("comment");
        feedCommentDto.setWriterUserId(writerUserId__4);
        feedCommentDto.setWriterNm("작성자");
        feedCommentDto.setWriterPic("profile.jpg");

        FeedCommentGetRes expectedResult = new FeedCommentGetRes();
        expectedResult.setMoreComment(false);
        expectedResult.setCommentList(List.of(feedCommentDto));

        //service.getFeedComment 에 임무부여
        given(feedCommentService.getFeedComment(givenParam)).willReturn(expectedResult);

        LinkedMultiValueMap<String , String> req = new LinkedMultiValueMap<>();
        req.add("feed_id",String.valueOf(givenParam.getFeedId()));
        req.add("start_idx",String.valueOf(givenParam.getStartIdx()));
        req.add("size",String.valueOf(SIZE));

        ResultActions resultActions = mockMvc.perform(get(URL).queryParams(req));

        String expectedResJson = getExpectedResJson(expectedResult);
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));
        verify(feedCommentService).getFeedComment(givenParam);

    }
    private String getExpectedResJson(FeedCommentGetRes res) throws Exception{
        ResultResponse expectedRes = ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
        return objectMapper.writeValueAsString(expectedRes);

    }

    @Test
    void postFeedComment() throws  Exception {
        FeedCommentPostReq givenParam = new FeedCommentPostReq();
        givenParam.setFeedId(feedId_2);
        givenParam.setComment("comment");

        given(feedCommentService.postFeedComment(givenParam)).willReturn(feedCommentId_3);

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        ResultResponse response = ResultResponse.<Long>builder()
                .resultMessage("댓글 등록 완료")
                .resultData(feedCommentId_3)
                .build();

        String expectedResJson = objectMapper.writeValueAsString(response);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));
    }

    @Test
    @DisplayName("피드 댓글 삭제")
    void delFeedComment () throws Exception{
        final int RESULT = 3;
        FeedCommentDelReq givenParam = new FeedCommentDelReq(feedCommentId_3);
        given(feedCommentService.delFeedComment(givenParam)).willReturn(RESULT);

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL).queryParam("feedCommentId",String.valueOf(feedCommentId_3)));

        String expectedResJson = objectMapper.writeValueAsString(ResultResponse.<Integer>builder()
                .resultMessage("댓글 삭제가 완료되었습니다.")
                .resultData(RESULT)
                .build());

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(feedCommentService).delFeedComment(givenParam);

    }
}