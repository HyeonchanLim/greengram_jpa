package com.green.greengram.user;

import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// <연결할 엔터티 , pk 타입>
public interface UserRepository extends JpaRepository<User , Long> {

    User findByUid(String uid); // uid 가 있는 메소드를 자동으로 만들어줌
}
