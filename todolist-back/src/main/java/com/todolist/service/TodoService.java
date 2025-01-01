package com.todolist.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.model.CalendarEntity;
import com.todolist.model.MemberEntity;
import com.todolist.model.TodoEntity;
import com.todolist.repository.CalendarRepository;
import com.todolist.repository.MemberRepository;
import com.todolist.repository.TodoRepository;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MemberRepository memberRepository; 
    
    public TodoEntity addTodo(TodoEntity todo) {
        TodoEntity savedTodo = todoRepository.save(todo);

        // 일정의 start_date와 end_date를 기준으로 Calendar 테이블에 날짜를 추가
        addDatesToCalendar(savedTodo);

        return savedTodo;
    }
    
    // 특정 날짜에 해당하는 할 일 목록 조회
    public List<TodoEntity> getTodos(Timestamp startDate) {
        return todoRepository.findByStartDate(startDate);
    }

    // 특정 memId로 해당 멤버의 모든 일정 조회
    public List<TodoEntity> getTodosByMemId(Integer memId) {
        List<TodoEntity> todos = todoRepository.findByMember_MemId(memId);
        System.out.println("Retrieved Todos: " + todos);
        return todos;
    }

 

    // 할 일 수정
    public TodoEntity updateTodo(Integer todoId, TodoEntity todo) {
        Optional<TodoEntity> existingTodoOptional = todoRepository.findById(todoId);
        
        if (existingTodoOptional.isPresent()) {
            TodoEntity existingTodo = existingTodoOptional.get();
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setContent(todo.getContent());
            existingTodo.setStartDate(todo.getStartDate());
            existingTodo.setEndDate(todo.getEndDate());
            existingTodo.setCompleteYn(todo.getCompleteYn());
            return todoRepository.save(existingTodo);
        } else {
            throw new RuntimeException("Todo not found with id: " + todoId);
        }
    }

    // 할 일 삭제
    public void deleteTodo(Integer todoId) {
        todoRepository.deleteById(todoId);
    }
    
    private void addDatesToCalendar(TodoEntity todo) {
        LocalDateTime startDate = todo.getStartDate().toLocalDateTime();
        LocalDateTime endDate = todo.getEndDate() != null ? todo.getEndDate().toLocalDateTime() : startDate;

        // start_date부터 end_date까지의 날짜를 Calendar 테이블에 추가
        for (LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            CalendarEntity calendar = new CalendarEntity();
            calendar.setCalDate(Timestamp.valueOf(date));
            calendar.setTodo(todo);
            calendarRepository.save(calendar);
        }
    }
    
    public MemberEntity getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

	public TodoEntity getTodoById(Integer todoId) {
		return todoRepository.getTodoByTodoId(todoId);
	}
}
