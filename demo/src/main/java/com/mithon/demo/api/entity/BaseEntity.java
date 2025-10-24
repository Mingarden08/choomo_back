package com.mithon.demo.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

//    @CreatedBy
//    @Column(name = "createdBy", length = 40, nullable = true, updatable = false)
//    private Long createdBy;

    @CreatedDate
    @Column(name = "regTime", updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP(6)")
    private LocalDateTime regTime;

    @PrePersist
    public void prePersist() {
        if (regTime == null) {
            regTime = LocalDateTime.now();
        }
        //사전 초기화 작업이 필요한 참조형 변수가 있다면
        //여기에서 초기화.
    }

//    @LastModifiedBy
//    @Column(name = "modifiedBy", length = 40, nullable = true, updatable = true)
//    private String modifiedBy;

//    @LastModifiedDate
//    @Column(name = "updateTime", updatable = true)
//    @ColumnDefault("CURRENT_TIMESTAMP(6)")
//    private LocalDateTime updateTime;
}
