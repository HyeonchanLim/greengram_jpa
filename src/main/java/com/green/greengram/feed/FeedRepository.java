package com.green.greengram.feed;

import com.green.greengram.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이건 자동 생성이라 굳이 안 써줘도 괜찮음
public interface FeedRepository extends JpaRepository<Feed,Long> {
}
