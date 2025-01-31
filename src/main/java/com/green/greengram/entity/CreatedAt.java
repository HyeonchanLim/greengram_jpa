package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // Entity 부모역할
@EntityListeners(AuditingEntityListener.class) // 이벤트 연결(binding), insert가 될 때 현재일시값을 넣자.
// auditing 기능 활성화 할려면 GreenGramApplication 에 @EnableJpaAuditing 애노테이션 추가해놔야 함
public class CreatedAt {
    @CreatedDate // insert가 되었을 때 현재일시값을 넣는다. 이 애노테이션이 작동을 하려면 @EntityListeners셋팅이 되어 있어야 한다.
    @Column(nullable = false) // not null 주고 싶다면 이렇게 사용 필수
    // Entity있을 때 이 애노테이션은 자동으로 작성이 되는데 설정을 좀 더 해주고 싶다면 이 애노테이션을 붙여야 한다.
    private LocalDateTime createdAt;

}
