package com.green.greengram.feed.model;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor // 기본생성자 - 순서 지켜야함
@AllArgsConstructor // 순서 상관없이 사용 가능
// Builder 사용 -> 위의 어노테이션 2개 써줘야 함
@EqualsAndHashCode
public class FeedWithPicCommentDto {
    private long feedId;
    private String contents;
    private String location;
    private String createdAt;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private int isLike;
    private List<String> pics;
    private List<FeedCommentDto> commentList;
}
