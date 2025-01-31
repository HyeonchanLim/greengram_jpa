package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feed extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @ManyToOne // 일대다 관계 설정해주는 애노테이션 - 아래의 user가 one , 위의 feed 가 many
    @JoinColumn(name = "writer_user_id", nullable = false)
    private User user; // user 테이블과 연계되는 방식이라 관계 설정을 위해서 Long 이 아닌 User 를 사용함 - 객체지향적

    @Column(length = 1_000)
    private String contents;

    @Column(length = 30)
    private String location;
}
