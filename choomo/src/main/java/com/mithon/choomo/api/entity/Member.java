package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", updatable = false, length = 50, nullable = false, unique = true)
    @Schema(description = "회원 이메일")
    private String email;

    @Column(name = "name", length = 25, nullable = false)
    @Schema(description = "회원이름")
    private String name;

    @Column(name = "passwd", length = 100, nullable = false)
    @Schema(description = "비빌번호")
    private String passwd;

    @Column(name = "gender", length = 10, nullable = false)
    @Schema(description = "성별")
    private String gender;

    @Column(name = "age", length = 10, nullable = false)
    @Schema(description = "나이")
    private int age;



    public Member() {

    }

//    @PrePersist
//    public void prePersist() {
//        //사전 초기화 작업이 필요한 참조형 변수가 있다면
//        //여기에서 초기화.
//    }
//
//
//    //updateEntity from Dto  or from String(이름...)
//
//    public void updateEntity(String name) {
//        this.setName(name);
//    }

}
