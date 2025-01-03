package com.green.greengram.feed;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import com.green.greengram.feed.model.FeedGetReq;
import com.green.greengram.feed.model.FeedGetRes;
import com.green.greengram.feed.model.FeedWithPicCommentDto;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FeedServiceForGetFeedListTest extends FeedServiceParentTest{
    //feedId = 3 , 댓글 3개
    static List<FeedCommentDto> feedId3Comments = new ArrayList<>(3);
    static List<FeedCommentDto> feedId4Comments = new ArrayList<>(4);

    static long FEED_ID_3 = 3L;
    static long FEED_ID_4 = 4L;

    @BeforeAll
    static void setFeedCommentDto(){
        for (int i=1; i<=3; i++){
            FeedCommentDto dto = new FeedCommentDto();
            feedId3Comments.add(dto);
            dto.setFeedId(FEED_ID_3);
            dto.setFeedCommentId(i);
            dto.setComment(String.format("댓글 %d",i));
            dto.setWriterUserId(i); // 1~5랜덤값
            dto.setWriterNm(String.format("홍길동%d",i));
            dto.setWriterPic(String.format("프로파일%d.jpg",i));
        }
        for (int i=4; i<=7; i++){
            FeedCommentDto dto = new FeedCommentDto();
            feedId4Comments.add(dto);
            dto.setFeedId(FEED_ID_4);
            dto.setFeedCommentId(i);
            dto.setComment(String.format("댓글 %d",i));
            dto.setWriterUserId(i); // 1~5랜덤값
            dto.setWriterNm(String.format("홍길동%d",i));
            dto.setWriterPic(String.format("프로파일%d.jpg",i));
        }
    }

    FeedWithPicCommentDto expectedFeedCommentDto;
    FeedWithPicCommentDto expectedFeedCommentDto2;
    @BeforeEach
    void setFeedWithPicCommentDto(){
        expectedFeedCommentDto = FeedWithPicCommentDto.builder()
                .feedId(3L)
                .contents("컨텐츠")
                .location("위치")
                .createdAt("2025-01-03 11:03:03")
                .writerUserId(10L)
                .writerNm("홍길동")
                .writerPic("프로필사진.jpg")
                .isLike(1)
                .pics(Arrays.asList("a.jpg","b.jpg","c.jpg"))
                .commentList(new ArrayList<>(3))
                .build();
        expectedFeedCommentDto = FeedWithPicCommentDto.builder()
                .feedId(4L)
                .contents("컨텐츠")
                .location("위치")
                .createdAt("2025-01-03 11:03:03")
                .writerUserId(14L)
                .writerNm("홍길동")
                .writerPic("프로필사진.jpg")
                .isLike(1)
                .pics(Arrays.asList("a.jpg","b.jpg","c.jpg"))
                .commentList(new ArrayList<>(3))
                .build();
    }

    @Test
    @DisplayName("FeedGetRes commentList 가 size 3 , 사이즈 유지")
    void objFeedGetRes_1(){
        List<FeedCommentDto> list = expectedFeedCommentDto.getCommentList();
        list.addAll(feedId3Comments);

        FeedGetRes actualResult = new FeedGetRes(expectedFeedCommentDto);
        assertAll(
                ()->assertEquals(expectedFeedCommentDto.getFeedId(),actualResult.getFeedId())
                , () -> assertEquals(expectedFeedCommentDto.getContents(),actualResult.getContents())
                , () -> assertEquals(expectedFeedCommentDto.getLocation(),actualResult.getLocation())
                , () -> assertEquals(expectedFeedCommentDto.getCreatedAt(),actualResult.getCreatedAt())
                , () -> assertEquals(expectedFeedCommentDto.getWriterUserId(),actualResult.getWriterUserId())
                , () -> assertEquals(expectedFeedCommentDto.getWriterNm(),actualResult.getWriterNm())
                , () -> assertEquals(expectedFeedCommentDto.getWriterPic(),actualResult.getWriterPic())
                , () -> assertEquals(expectedFeedCommentDto.getIsLike(),actualResult.getIsLike())
                , () -> assertEquals(expectedFeedCommentDto.getPics(),actualResult.getPics())
                , () -> assertFalse(actualResult.getComment().isMoreComment())
                , () -> assertEquals(expectedFeedCommentDto.getCommentList(),actualResult.getComment().getCommentList())

        );

    }

    @Test
    @DisplayName("FeedGetRes commentList 가 size 4였을 때 size 3으로 처리")
    void objFeedGetRes_2(){
        List<FeedCommentDto> list = expectedFeedCommentDto.getCommentList();
        list.addAll(feedId4Comments);

        FeedGetRes actualResult = new FeedGetRes(expectedFeedCommentDto);

        assertAll(
                ()->assertEquals(expectedFeedCommentDto.getFeedId(),actualResult.getFeedId())
                , () -> assertEquals(expectedFeedCommentDto.getContents(),actualResult.getContents())
                , () -> assertEquals(expectedFeedCommentDto.getLocation(),actualResult.getLocation())
                , () -> assertEquals(expectedFeedCommentDto.getCreatedAt(),actualResult.getCreatedAt())
                , () -> assertEquals(expectedFeedCommentDto.getWriterUserId(),actualResult.getWriterUserId())
                , () -> assertEquals(expectedFeedCommentDto.getWriterNm(),actualResult.getWriterNm())
                , () -> assertEquals(expectedFeedCommentDto.getWriterPic(),actualResult.getWriterPic())
                , () -> assertEquals(expectedFeedCommentDto.getIsLike(),actualResult.getIsLike())
                , () -> assertEquals(expectedFeedCommentDto.getPics(),actualResult.getPics())
                , () -> assertTrue(actualResult.getComment().isMoreComment())
                , () -> assertEquals(3, actualResult.getComment().getCommentList().size())
                , () -> {
                    List<FeedCommentDto> commentDtoList = actualResult.getComment().getCommentList();
                    for (int i=0; i<commentDtoList.size(); i++){
                        assertEquals(feedId4Comments.get(i) , commentDtoList.get(i));
                        // 원본의 데이터(feedId4Comments) - actualResult 데이터 비교 ->
                        // res 에서 remove 다르게 할 경우 오류 발생
                    }
                }
        );

        System.out.println(expectedFeedCommentDto.getCommentList().size());
        System.out.println(feedId4Comments.size());
    }

    @Test
    @DisplayName("getFeedList4 테스트 FeedWithPicCommentDto >> FeedGetRes")
    void getFeedList4 (){
        expectedFeedCommentDto.getCommentList().addAll(feedId3Comments);
        expectedFeedCommentDto2.getCommentList().addAll(feedId4Comments);

        List<FeedWithPicCommentDto> givenList = Arrays.asList(expectedFeedCommentDto,expectedFeedCommentDto2);

        given(feedMapper.selFeedWithPicAndCommentLimit4List(any())).willReturn(givenList);

        List<FeedGetRes> expectedList = Arrays.asList(
            new FeedGetRes(expectedFeedCommentDto),
            new FeedGetRes(expectedFeedCommentDto2)
        );

        List<FeedGetRes> actualList = feedService.getFeedList4(new FeedGetReq(1,2,null));

        assertEquals(expectedList,actualList);

    }
}
