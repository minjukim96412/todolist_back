package com.todolist.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CALENDAR")
@Getter
@Setter
public class CalendarEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAL_ID")
	private Integer calId;
	
	@Column(name = "CALDATE")
	private Timestamp calDate;
	
	@ManyToOne
    @JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID") 
    private MemberEntity member;

	@ManyToOne
    @JoinColumn(name = "TODO_ID", referencedColumnName = "TODO_ID") 
    private TodoEntity todo;
	
}
