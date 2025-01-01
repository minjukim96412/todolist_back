package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
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
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TodoService todoService;

    private TodoEntity todo;
    private MemberEntity member;

    @BeforeEach
    void setUp() {
        member = new MemberEntity();
        member.setMemId(1);
        member.setEmail("test@example.com");

        todo = new TodoEntity();
        todo.setTodoId(1);
        todo.setTitle("Test Todo");
        todo.setContent("Test Content");
        todo.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        todo.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        todo.setMember(member);
    }

    @Test
    void testAddTodo() {
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(todo);

        TodoEntity savedTodo = todoService.addTodo(todo);

        assertNotNull(savedTodo);
        assertEquals(todo.getTitle(), savedTodo.getTitle());
        verify(todoRepository, times(1)).save(todo);
        verify(calendarRepository, times(2)).save(any(CalendarEntity.class)); // 두 날짜가 추가되었는지 확인
    }

    @Test
    void testGetTodos() {
        Timestamp startDate = todo.getStartDate();
        when(todoRepository.findByStartDate(startDate)).thenReturn(Arrays.asList(todo));

        List<TodoEntity> todos = todoService.getTodos(startDate);

        assertNotNull(todos);
        assertEquals(1, todos.size());
        assertEquals(todo.getTitle(), todos.get(0).getTitle());
        verify(todoRepository, times(1)).findByStartDate(startDate);
    }

    @Test
    void testGetTodosByMemId() {
        when(todoRepository.findByMember_MemId(member.getMemId())).thenReturn(Arrays.asList(todo));

        List<TodoEntity> todos = todoService.getTodosByMemId(member.getMemId());

        assertNotNull(todos);
        assertEquals(1, todos.size());
        assertEquals(todo.getTitle(), todos.get(0).getTitle());
        verify(todoRepository, times(1)).findByMember_MemId(member.getMemId());
    }

    @Test
    void testUpdateTodo() {
        TodoEntity updatedTodo = new TodoEntity();
        updatedTodo.setTitle("Updated Title");
        updatedTodo.setContent("Updated Content");
        updatedTodo.setStartDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        updatedTodo.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        updatedTodo.setCompleteYn(true);

        when(todoRepository.findById(todo.getTodoId())).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(updatedTodo);

        TodoEntity result = todoService.updateTodo(todo.getTodoId(), updatedTodo);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.getCompleteYn());
        verify(todoRepository, times(1)).findById(todo.getTodoId());
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void testDeleteTodo() {
        doNothing().when(todoRepository).deleteById(todo.getTodoId());

        todoService.deleteTodo(todo.getTodoId());

        verify(todoRepository, times(1)).deleteById(todo.getTodoId());
    }

    @Test
    void testGetMemberByEmail() {
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(member);

        MemberEntity result = todoService.getMemberByEmail(member.getEmail());

        assertNotNull(result);
        assertEquals(member.getEmail(), result.getEmail());
        verify(memberRepository, times(1)).findByEmail(member.getEmail());
    }

    @Test
    void testGetTodoById() {
        when(todoRepository.getTodoByTodoId(todo.getTodoId())).thenReturn(todo);

        TodoEntity result = todoService.getTodoById(todo.getTodoId());

        assertNotNull(result);
        assertEquals(todo.getTitle(), result.getTitle());
        verify(todoRepository, times(1)).getTodoByTodoId(todo.getTodoId());
    }
}

