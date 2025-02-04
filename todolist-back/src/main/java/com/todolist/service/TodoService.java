package com.todolist.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public TodoEntity updateTodo(Integer todoId, TodoEntity todo) {
        Optional<TodoEntity> existingTodoOptional = todoRepository.findById(todoId);
        
        if (existingTodoOptional.isPresent()) {
            TodoEntity existingTodo = existingTodoOptional.get();
            
            // 기존 Calendar 데이터 삭제
            calendarRepository.deleteByTodo(existingTodo);
            
            // Todo 정보 업데이트
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setContent(todo.getContent());
            existingTodo.setStartDate(todo.getStartDate());
            existingTodo.setEndDate(todo.getEndDate());
            existingTodo.setCompleteYn(todo.getCompleteYn());
            
            // 수정 시간 업데이트
            existingTodo.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            
            // Todo 저장
            TodoEntity savedTodo = todoRepository.save(existingTodo);
            
            // 새로운 Calendar 데이터 추가
            addDatesToCalendar(savedTodo);
            
            return savedTodo;
        } else {
            throw new RuntimeException("Todo not found with id: " + todoId);
        }
    }

    // 할 일 삭제
    public void deleteTodo(Integer todoId) {
        todoRepository.deleteById(todoId);
    }
    
    private void addDatesToCalendar(TodoEntity todo) {
        LocalDateTime startDateTime = todo.getStartDate().toLocalDateTime();
        LocalDateTime endDateTime = todo.getEndDate().toLocalDateTime();
        
        // 시작일 저장 (원래 시간 유지)
        CalendarEntity startCalendar = new CalendarEntity();
        startCalendar.setCalDate(todo.getStartDate());
        startCalendar.setTodo(todo);
        startCalendar.setMember(todo.getMember());
        calendarRepository.save(startCalendar);
        
        // 중간 날짜들 저장 (00:00:00으로 설정)
        LocalDateTime currentDate = startDateTime.toLocalDate().plusDays(1).atStartOfDay();
        LocalDate lastDate = endDateTime.toLocalDate();
        
        while (currentDate.toLocalDate().isBefore(lastDate)) {
            CalendarEntity calendar = new CalendarEntity();
            calendar.setCalDate(Timestamp.valueOf(currentDate));
            calendar.setTodo(todo);
            calendar.setMember(todo.getMember());
            calendarRepository.save(calendar);
            
            currentDate = currentDate.plusDays(1);
        }
        
        // 종료일 저장 (원래 시간 유지)
        if (!startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
            CalendarEntity endCalendar = new CalendarEntity();
            endCalendar.setCalDate(todo.getEndDate());
            endCalendar.setTodo(todo);
            endCalendar.setMember(todo.getMember());
            calendarRepository.save(endCalendar);
        }
    }
    
    public MemberEntity getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

	public TodoEntity getTodoById(Integer todoId) {
		return todoRepository.getTodoByTodoId(todoId);
	}

	public Map<String, List<TodoEntity>> getTodosCalendarByMemId(Integer memId) {
	    // 1. 해당 회원의 전체 TodoList 조회
	    List<TodoEntity> allTodos = todoRepository.findByMember_MemId(memId);
	    
	    // 2. TodoList의 모든 todoId 수집
	    List<Integer> todoIds = allTodos.stream()
	        .map(TodoEntity::getTodoId)
	        .collect(Collectors.toList());
	    
	    // 3. Calendar 테이블에서 해당 todoId들에 해당하는 모든 날짜 정보 조회
	    List<CalendarEntity> calendarEntries = calendarRepository.findByTodo_TodoIdIn(todoIds);
	    
	    // 4. 날짜별로 할일을 그룹화
	    Map<String, List<TodoEntity>> calendarTodos = new HashMap<>();
	    
	    for (CalendarEntity calendar : calendarEntries) {
	        // Timestamp를 'YYYY-MM-DD' 형식으로 변환
	        String dateKey = new java.sql.Date(calendar.getCalDate().getTime())
	            .toString();
	        TodoEntity todo = calendar.getTodo();
	        
	        // 해당 날짜의 리스트가 없으면 새로 생성
	        if (!calendarTodos.containsKey(dateKey)) {
	            calendarTodos.put(dateKey, new ArrayList<>());
	        }
	        
	        // 이미 추가된 todo인지 확인 (중복 방지)
	        List<TodoEntity> todosForDate = calendarTodos.get(dateKey);
	        if (todosForDate.stream().noneMatch(t -> t.getTodoId().equals(todo.getTodoId()))) {
	            todosForDate.add(todo);
	        }
	    }
	    
	    return calendarTodos;
	}
}
