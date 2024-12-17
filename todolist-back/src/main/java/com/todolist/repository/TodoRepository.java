package com.todolist.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
	public List<TodoEntity> findByStartDate(Timestamp startDate);

	List<TodoEntity> findByMember_MemId(Long memId);
	
}
