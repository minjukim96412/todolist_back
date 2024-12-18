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


    @PutMapping("/{id}")
    public TodoEntity updateTodo(@PathVariable Long id, @RequestBody TodoEntity todo) {
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }
}
