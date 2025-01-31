package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedPic extends CreatedAt{
    @EmbeddedId // 이렇게 하면 복합키가 됨
    private FeedPicIds feedPicIds;

    @ManyToOne
    @MapsId("feedId") // 관계 연결을 위해서 속성명 작성 
    @JoinColumn(name = "feed_id")
    private Feed feed;

}
