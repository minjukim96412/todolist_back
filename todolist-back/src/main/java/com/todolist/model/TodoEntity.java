package com.todolist.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TODOLIST")
@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@ToString
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private Integer todoId; 
    
    @Column(name = "TITLE", length = 200)
    private String title;
    
    @Column(name = "CONTENT", length = 4000)
    private String content;
    
    @Column(name = "START_DATE")
    private Timestamp startDate; 
    
    @Column(name = "END_DATE")
    private Timestamp endDate; 
    
    @Column(name = "CREATE_DATE", updatable = false)
    private Timestamp createDate; 
    
    @Column(name = "UPDATE_DATE", insertable = false)
    private Timestamp updateDate; 
    
    @Column(name = "COMPLETEYN", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean completeYn = false;
    
    @ManyToOne
    @JoinColumn(name = "MEM_ID")
    @JsonIgnore
    private MemberEntity member;

}
