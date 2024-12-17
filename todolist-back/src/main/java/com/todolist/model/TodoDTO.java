package com.todolist.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDTO {

	private Integer todoId;
    private String title;
    private String content;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Boolean completeYn;
    private Integer memId;
	
}
