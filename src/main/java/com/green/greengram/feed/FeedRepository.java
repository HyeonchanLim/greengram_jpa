package com.green.greengram.feed;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findByFeedIdAndWriterUser(Long feedId, User writerUser);

    //쿼리 메소드로 delete, update는 비추천
    // 왜냐하면 위에서 find 로 select 한 다음 delete 만 추가하면 되기 때문에 굳이 delete 를 따로 또 만드는 방법은 효율성이 낮음
    // 그리고 select 를 통해 검증을 거치고 delete 실행이 가능 + 만약 없는 데이터라면 예외처리도 추가적으로 실행 가능
    // select 를 통해서 검증을 하고 jpa 의 delete 만 추가적으로 사용하면 예외처리까지 가능하기 때문에 delete 쿼리 메소드 생성은 비추천
    int deleteByFeedIdAndWriterUser(Long feedId, User writerUser);

    //JPQL (Java Persistence Query Language)
    @Modifying //이 애노테이션이 있어야 insert or delete or update JPQL, 리턴타입은 void or int
    @Query("delete from Feed f where f.feedId=:feedId AND f.writerUser.userId=:writerUserId")
    int deleteFeed(Long feedId, Long writerUserId);
    /*
    Feed (대문자로 시작) - 클래스명 작성해야 함

    feedId=1, writerUserId=2 가정하에 아래 SQL문이 만들어진다.

    DELETE f FROM feed f
    WHERE f.feed_id = 1
    AND f.user_id = 2
     */
    @Modifying
    @Query(value = "delete from feed f where f.feed_id=:feedId and f.writer_user_id=:writerUserId",nativeQuery = true)
    int deleteFeedSql(Long feedId , Long writerUserId);
    // jpql 사용할 경우 유지보수성이 높아짐 -> entity 에서 멤버필드 이름 refactor 했을 경우 여기서도 같이 적용되어 변경됨
    // 그로 인해 유지보수성이 높아져서 편해짐
}
