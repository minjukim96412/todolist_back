package com.todolist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
public class MemberEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEM_ID")
    private Integer memId;  // Primary Key
    
    @Column(name = "EMAIL", length = 50)
    private String email;
    
    @Column(name = "NICKNAME", length = 50)
    private String nickname;
    
    @Column(name = "TOKEN_ID", length = 255)
    private String tokenId;  
    
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TodoEntity> todos;

}
