package com.todolist.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
	public List<TodoEntity> findByStartDate(Timestamp startDate);

	public List<TodoEntity> findByMember_MemId(Integer memId);

	public TodoEntity getTodoByTodoId(Integer todoId);

	
}
