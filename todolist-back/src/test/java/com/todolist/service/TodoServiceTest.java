package com.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todolist.model.CalendarEntity;
import com.todolist.model.MemberEntity;
import com.todolist.model.TodoEntity;
import com.todolist.repository.CalendarRepository;
import com.todolist.repository.MemberRepository;
import com.todolist.repository.TodoRepository;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TodoService todoService;

    private TodoEntity mockTodo;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        // 테스트용 mock MemberEntity 생성
        memberEntity = new MemberEntity();
        memberEntity.setMemId("user1");
        memberEntity.setEmail("user1@example.com");
        memberEntity.setNickname("User One");

        // 테스트용 mock TodoEntity 생성
        mockTodo = new TodoEntity(1, "Test Todo", "Details", Timestamp.valueOf(LocalDateTime.now()), null, null, null, false, memberEntity);
    }

    @Test
    void testAddTodo() {
        // Given
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(mockTodo);

        // When
        TodoEntity savedTodo = todoService.addTodo(mockTodo);

        // Then
        assertNotNull(savedTodo);
        assertEquals("Test Todo", savedTodo.getTitle());
        verify(calendarRepository, times(1)).save(any(CalendarEntity.class)); // CalendarRepository의 save 메서드가 한 번 호출되었는지 확인
    }

    @Test
    void testGetTodos() {
        // Given
        String date = "2024-12-11";
        List<TodoEntity> mockTodos = List.of(mockTodo);
        when(todoRepository.findByStartDate(date)).thenReturn(mockTodos);

        // When
        List<TodoEntity> todos = todoService.getTodos(date);

        // Then
        assertNotNull(todos);
        assertEquals(1, todos.size());
        assertEquals("Test Todo", todos.get(0).getTitle());
    }

    @Test
    void testUpdateTodo() {
        Long todoId = 1L;
        TodoEntity updatedTodo = new TodoEntity(1, "Updated Todo", "Updated Details", Timestamp.valueOf(LocalDateTime.now()), null, null, null, false, memberEntity);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(mockTodo));
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(updatedTodo);

        // When
        TodoEntity result = todoService.updateTodo(todoId, updatedTodo);

        // Then
        assertNotNull(result);
        assertEquals("Updated Todo", result.getTitle());
        assertEquals("Updated Details", result.getContent());
    }

    @Test
    void testDeleteTodo() {
        // Given
        Long todoId = 1L;
        doNothing().when(todoRepository).deleteById(todoId);

        // When
        todoService.deleteTodo(todoId);

        // Then
        verify(todoRepository, times(1)).deleteById(todoId);
    }
}
