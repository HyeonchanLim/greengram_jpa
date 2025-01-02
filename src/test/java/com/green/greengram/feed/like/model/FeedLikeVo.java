package com.green.greengram.feed.like.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/*
immutable(불변성) 하게 객체를 만들고 싶다. 그러면 setter를 빼야함
private한 멤버필드에 값 넣는 방법 2가지(생성자 , setter)
setter 를 빼기로 했기 때문에 남은 선택지는 생성자만 남았다.
생성자를 이용해서 객체 생성을 해야 하는데 멤버필드값을 셋팅하는
경우의 수가 많을 수 있음
1. feedId 만 셋팅한다.
2. userId 만 셋팅한다.
3. createdAt 만 셋팅한다.
4. feedId , userId 만 셋팅한다.
5. feedId , createdAt 만 셋팅한다.
6. userId , createdAt 만 셋팅한다.
7. feedId , userId , createdAt 만 셋팅한다.
8. 하나도 셋팅하지 않는다.

 */

@Getter
@EqualsAndHashCode
// equals 는 동등성을 구분함 / 여기서 오버라이딩 추가 , == 비교 구문
// ex) 똑같은 값을 가지고 있는 객체 2개가 있을 경우 구분

// 이뮤터블(객체의 불변성)하게 사용하는 방법은 vo
// 변경 가능하게 설정하는 방법은 dto
public class FeedLikeVo {
    private long feedId;
    private long userId;
    private String createdAt;

}
