package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // 이 애노테이션을 사용해줘야 테이블을 만들고 DML 때 사용 가능함
// Entity 사용하면 pk 무조건 있어야 함
public class User extends UpdatedAt{
    @Id // 이걸 사용해서 pk 값 설정함
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정
    private long userId;


    @Column(nullable = false , length = 30 , unique = true) // varchar -> 길이 30 설정 , unique 설정
    private String uid;

    @Column(nullable = false , length = 100)
    private String upw;

    @Column(length = 100)
    private String nickName;

    @Column(length = 100)
    private String pic;

}
