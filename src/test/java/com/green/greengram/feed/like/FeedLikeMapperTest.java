package com.green.greengram.feed.like;

import com.green.greengram.TestUtils;
import com.green.greengram.feed.like.model.FeedLikeReq;
import com.green.greengram.feed.like.model.FeedLikeVo;
import com.green.greengram.user.follow.UserFollowMapper;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
// static 메소드 전부 이름만 써도 사용 가능하게 해줌

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // yaml 저굥ㅇ되는 파일 선택 (application-test.yml)
@MybatisTest // Mybatis Mapper Test 이기 때문에 작성 >> Mapper 들이 전부 객체화
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 테스트를 기본적으로 메모리 데이터베이스 (H2) 를 사용해서 하는데 메모리 데이터베이스로 교체하지 않겠다.
// 즉 우리가 원래 쓰는 데이터 베이스로 테스트를 진행하겠다.
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
// 메소드 마다 인스턴스 메소드를 만들겠다 , 기본값은 PER_METHOD 다
// PER_CLASS 사용할 경우 TEST 메소드마다 static 안 써도 괜찮음 , 테스트 객체를 딱 하나만 만든다
// 만약 PER_METHOD 라면 TEST 메소드마다 static 사용
class FeedLikeMapperTest {
    @Autowired // todo : 스프링 컨테이너가 di 해주는게 맞는지 확인
    FeedLikeMapper feedLikeMapper; // DI 가 된다. 위에서 mapper 객체화 되서 그럼

    @Autowired
    FeedLikeTestMapper feedLikeTestMapper;

        static final long FEED_ID_1 = 1L;
        static final long FEED_ID_5 = 5L;
        static final long USER_ID_2 = 2L;



        static final FeedLikeReq existedData = new FeedLikeReq();
        static final FeedLikeReq notExistedData = new FeedLikeReq();

        /*
        @BeforeAll - 모든 테스트 실행 전에 최초 한번 실행
        ---
        @BeforeEach - 각 테스트 실행 전에 실행
        @Test
        @AfterEach - 각 테스트 실행 후에 실행
        // 위의 3가지는 static 사용 불가
        ---
        @AfterAll - 모든 테스트 실행 후에 최초 한번 실행
         */

        // @BeforeAll - 테스트 메소드 실행되는 시점 최초 딱 한번 실행이 되는 메소드
        // 테스트 메소드 마다 테스트 객체가 만들어지면 beforeall 메소드는 static 메소드여야 한다.
        // 한 테스트 객체가 만들어지면 non-static 메소드일 수 한다.
        @BeforeAll
        static void initDate(){
            // static 메소드는 static 객체만 다룰 수 있음 만약 멤버필드 static 없으면 사용 불가
            existedData.setFeedId(FEED_ID_1);
            existedData.setUserId(USER_ID_2);

            notExistedData.setFeedId(FEED_ID_5);
            notExistedData.setUserId(USER_ID_2);

        }

        // @BeforeEach - 테스트 메소드 마다 테스트 메소드 실행 전에 실행되는 before메소드
        // Before 메소드

    // 현재 필드 주입 방식임
    // 중복된 데이터 입력시 DuplicateKeyException 발생 체크
    @Test
    @DisplayName("중복된 데이터 입력시 DuplicateKeyException 발생 체크")
    void insFeedLikeDuplicateDataThrowDuplicateKeyException() {
            // 아래의 assert 에서 exception 이 발생하면 실행되는 부분
        assertThrows(DuplicateKeyException.class, () -> {
            feedLikeMapper.insFeedLike(existedData);
        }, "데이터 중복시 에러 발생되지 않음 > primary key(feed_id, user_id) 확인 바람");
    }

    @Test
    void insFeedLike () {
        // when (실행)
        List<FeedLikeVo> actualFeedLikeListBefore = feedLikeTestMapper.selFeedLikeAll(); // insert 전 기존 튜플 수
        FeedLikeVo actualFeedLikeVoBefore = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData);
        // insert 전 WHERE 절에 PK 로 데이터를 가져옴

        int actualAffectedRows = feedLikeMapper.insFeedLike(notExistedData);

        FeedLikeVo actualFeedLikeVoAfter = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData);
        // insert 후 WHERE 절에 PK 로 데이터를 가져옴
        List<FeedLikeVo> actualFeedLikeListAfter = feedLikeTestMapper.selFeedLikeAll(); // insert 후 튜플 수

        // then (단언 , 체크)
        assertAll(
                () -> TestUtils.assertCurrentTimestamp(actualFeedLikeVoAfter.getCreatedAt())
                , () -> assertEquals(actualFeedLikeListBefore.size()+1 ,actualFeedLikeListAfter.size())
                , () -> assertNull(actualFeedLikeVoBefore) // 내가 insert 하려고 하는 데이터가 없었는지 단언
                , () -> assertNotNull(actualFeedLikeVoAfter) // 실제 insert 가 내가 원하는 데이터로 되었는지 단언
                , () -> assertEquals(1, actualAffectedRows)
                , () -> assertEquals(notExistedData.getFeedId(),actualFeedLikeVoAfter.getFeedId())
                // 내가 원하는 데이터로 insert 되었는지 더블 체크 만약에 insert 부분이 틀리지 않았으면 굳이 안 써도 됨
                , () -> assertEquals(notExistedData.getUserId(),actualFeedLikeVoAfter.getUserId())
                // 내가 원하는 데이터로 insert 되었는지 더블 체크 만약에 insert 부분이 틀리지 않았으면 굳이 안 써도 됨
        );
    }
    // TEST 라서 롤백이 되고 있으니 초기화가 가능
    // 실제 파일에서는 insert 후 데이터 삭제 작업 필요 - 그래야 서로 영향을 안 받음
    @Test
    void delFeedLikeNoData (){
//        FeedLikeReq givenParam = new FeedLikeReq();
//        givenParam.setFeedId(FEED_ID_5);
//        givenParam.setFeedId(USER_ID_2);

        int actualAffectedRow = feedLikeMapper.delFeedLike(notExistedData);
        assertEquals(0,actualAffectedRow);
    }
    // 검증 바꿔서 다른 방식으로 풀어보기
    @Test
    void delFeedLikeNormal(){
        FeedLikeVo actualFeedLikeVoBefore = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);
        int actualAffectedRows = feedLikeMapper.delFeedLike(existedData);
        FeedLikeVo actualFeedLikeVoAfter = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);

        assertEquals(1, actualAffectedRows);

        assertAll(
                () -> assertEquals(1, actualAffectedRows)
                , () -> assertNotNull(actualFeedLikeVoBefore) // 내가 insert 하려고 하는 데이터가 없었는지 단언
                , () -> assertNull(actualFeedLikeVoAfter) // 실제 insert 가 내가 원하는 데이터로 되었는지 단언
        );
    }
}