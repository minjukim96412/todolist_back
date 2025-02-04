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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "To-Do API", description = "To-Do 관리 API")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Operation(summary = "특정 날짜의 To-Do 목록 조회", description = "지정된 날짜에 해당하는 모든 To-Do 목록을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @GetMapping
    public List<TodoEntity> getTodos(
        @io.swagger.v3.oas.annotations.Parameter(description = "YYYY-MM-DD 형식의 날짜", required = true) @RequestParam String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = format.parse(date);
        Timestamp startDate = new Timestamp(parsedDate.getTime());
        return todoService.getTodos(startDate);
    }

    @Operation(summary = "특정 회원의 To-Do 목록 조회", description = "회원 ID로 To-Do 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원의 To-Do 목록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @GetMapping("/mem/{memId}/calendar")
    public ResponseEntity<Map<String, List<TodoEntity>>> getTodosCalendarByMember(
        @Parameter(description = "회원 ID", required = true) @PathVariable Integer memId) {
        Map<String, List<TodoEntity>> calendarTodos = todoService.getTodosCalendarByMemId(memId);
        return ResponseEntity.ok(calendarTodos);
    }

    @Operation(summary = "특정 To-Do 조회", description = "To-Do ID로 특정 To-Do의 상세 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 상세 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoEntity> getTodoByTodoId(
        @Parameter(description = "To-Do ID", required = true) @PathVariable Integer todoId) {
        TodoEntity todo = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todo);
    }

    @Operation(summary = "새로운 To-Do 추가", description = "새로운 To-Do를 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 추가 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @PostMapping
    public ResponseEntity<TodoEntity> addTodo(
        @Parameter(description = "To-Do 생성에 필요한 데이터", required = true) @RequestBody TodoDTO todoDTO) {
        TodoEntity todo = new TodoEntity();
        todo.setTitle(todoDTO.getTitle());
        todo.setContent(todoDTO.getContent());
        todo.setStartDate(todoDTO.getStartDate());
        todo.setEndDate(todoDTO.getEndDate());

        MemberEntity member = new MemberEntity();
        member.setMemId(todoDTO.getMemId());
        todo.setMember(member);

        TodoEntity savedTodo = todoService.addTodo(todo);
        return ResponseEntity.ok(savedTodo);
    }

    @Operation(summary = "To-Do 수정", description = "기존 To-Do의 내용을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @PutMapping("/{todoId}")
    public TodoEntity updateTodo(
        @Parameter(description = "수정할 To-Do ID", required = true) @PathVariable Integer todoId,
        @Parameter(description = "수정할 To-Do 데이터", required = true) @RequestBody TodoEntity todo) {
        return todoService.updateTodo(todoId, todo);
    }

    @Operation(summary = "To-Do 삭제", description = "To-Do ID로 특정 To-Do를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 삭제 성공")
    })
    @DeleteMapping("/{todoId}")
    public void deleteTodo(
        @Parameter(description = "삭제할 To-Do ID", required = true) @PathVariable Integer todoId) {
        todoService.deleteTodo(todoId);
    }

    @Operation(summary = "To-Do 특정 필드 업데이트", description = "To-Do의 일부 필드만 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "To-Do 필드 업데이트 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoEntity.class)))
    })
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoEntity> patchTodo(
        @Parameter(description = "수정할 To-Do ID", required = true) @PathVariable Integer todoId,
        @Parameter(description = "수정할 필드를 포함하는 데이터", required = true) @RequestBody TodoDTO todoDTO) {
        TodoEntity existingTodo = todoService.getTodoById(todoId);

        if (todoDTO.getCompleteYn() != null) {
            existingTodo.setCompleteYn(todoDTO.getCompleteYn());
        }
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

        TodoEntity updatedTodo = todoService.updateTodo(todoId, existingTodo);
        return ResponseEntity.ok(updatedTodo);
    }
}
