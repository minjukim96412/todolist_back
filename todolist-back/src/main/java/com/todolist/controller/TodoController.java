package com.todolist.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.model.MemberEntity;
import com.todolist.model.TodoDTO;
import com.todolist.model.TodoEntity;
import com.todolist.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<TodoEntity> getTodos(@RequestParam String date) throws ParseException {
        // 날짜 문자열을 Timestamp로 변환
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = format.parse(date);
        Timestamp startDate = new Timestamp(parsedDate.getTime());

        return todoService.getTodos(startDate);
    }

    @GetMapping("/mem/{memId}")
    public ResponseEntity<List<TodoEntity>> getTodosByMember(@PathVariable Long memId) {
        List<TodoEntity> todos = todoService.getTodosByMemId(memId);
        return ResponseEntity.ok(todos); 
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoEntity> getTodoByTodoId(@PathVariable Long todoId) {
        TodoEntity todo = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todo);
    }

    
    @PostMapping
    public ResponseEntity<TodoEntity> addTodo(@RequestBody TodoDTO todoDTO) {
        TodoEntity todo = new TodoEntity();
        todo.setTitle(todoDTO.getTitle());
        todo.setContent(todoDTO.getContent());
        todo.setStartDate(todoDTO.getStartDate());
        todo.setEndDate(todoDTO.getEndDate());
        
        // MEM_ID를 MemberEntity로 변환하여 저장
        MemberEntity member = new MemberEntity();
        member.setMemId(todoDTO.getMemId());
        todo.setMember(member);

        TodoEntity savedTodo = todoService.addTodo(todo);
        return ResponseEntity.ok(savedTodo);
    }


    @PutMapping("/{todoId}")
    public TodoEntity updateTodo(@PathVariable Long todoId, @RequestBody TodoEntity todo) {
        return todoService.updateTodo(todoId, todo);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
    }
    
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoEntity> patchTodo(@PathVariable Long todoId, @RequestBody TodoDTO todoDTO) {
        // 기존 일정 가져오기
        TodoEntity existingTodo = todoService.getTodoById(todoId);

        if (todoDTO.getCompleteYn() != null) {
            existingTodo.setCompleteYn(todoDTO.getCompleteYn());
        }
        
        // 다른 필드들 업데이트
        if (todoDTO.getTitle() != null) {
            existingTodo.setTitle(todoDTO.getTitle());
        }
        if (todoDTO.getContent() != null) {
            existingTodo.setContent(todoDTO.getContent());
        }
        if (todoDTO.getStartDate() != null) {
            existingTodo.setStartDate(todoDTO.getStartDate());
        }
        if (todoDTO.getEndDate() != null) {
            existingTodo.setEndDate(todoDTO.getEndDate());
        }

        // 수정된 일정 저장
        TodoEntity updatedTodo = todoService.updateTodo(todoId, existingTodo);
        
        return ResponseEntity.ok(updatedTodo);
    }


}
