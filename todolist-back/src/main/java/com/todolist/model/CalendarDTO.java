package com.todolist.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDTO {

	private Integer calId;
    private Timestamp calDate;
    private String memId; 
    private Integer todoId;
	
}
