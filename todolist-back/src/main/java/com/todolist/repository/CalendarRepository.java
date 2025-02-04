package com.todolist.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.model.CalendarEntity;
import com.todolist.model.MemberEntity;
import com.todolist.model.TodoEntity;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
    List<CalendarEntity> findByCalDate(Timestamp calDate);
    void deleteByTodo(TodoEntity todo);
	List<CalendarEntity> findByTodo_TodoIdIn(List<Integer> todoIds);

}

