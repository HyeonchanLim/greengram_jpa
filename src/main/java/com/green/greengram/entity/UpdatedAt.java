package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // Entity 부모역할
@EntityListeners(AuditingEntityListener.class) // 이벤트 연결(binding), insert가 될 때 현재일시값을 넣자.
public class UpdatedAt extends CreatedAt { // 상속하는 이유는 createdAt이 있어야 해서 그럼
    @LastModifiedDate //insert,update 되었을 때 현재일시값을 넣는다.
    @Column(nullable = false) // not null 주고 싶다면 이렇게 사용 필수
    // Entity있을 때 이 애노테이션은 자동으로 작성이 되는데 설정을 좀 더 해주고 싶다면 이 애노테이션을 붙여야 한다.
    private LocalDateTime updatedAt;
}
