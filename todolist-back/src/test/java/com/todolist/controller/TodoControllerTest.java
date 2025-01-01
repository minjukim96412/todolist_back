package com.todolist.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.model.MemberEntity;
import com.todolist.model.TodoDTO;
import com.todolist.model.TodoEntity;
import com.todolist.service.TodoService;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTodos() throws Exception {
        Timestamp date = Timestamp.valueOf("2024-12-20 00:00:00");
        TodoEntity todo = new TodoEntity();
        todo.setTodoId(1);
        todo.setTitle("Test Todo");
        todo.setStartDate(date);
        
        List<TodoEntity> todos = Arrays.asList(todo);
        when(todoService.getTodos(date)).thenReturn(todos);

        mockMvc.perform(get("/api/todos")
                .param("date", "2024-12-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Todo")));

        verify(todoService, times(1)).getTodos(date);
    }

    @Test
    void testAddTodo() throws Exception {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("New Todo");
        todoDTO.setContent("Content");
        todoDTO.setMemId(1);

        MemberEntity member = new MemberEntity();
        member.setMemId(1);

        TodoEntity todo = new TodoEntity();
        todo.setTodoId(1);
        todo.setTitle("New Todo");
        todo.setContent("Content");
        todo.setMember(member);

        when(todoService.addTodo(ArgumentMatchers.<TodoEntity>any())).thenReturn(todo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Todo")))
                .andExpect(jsonPath("$.content", is("Content")));

        verify(todoService, times(1)).addTodo(ArgumentMatchers.<TodoEntity>any());
    }

    @Test
    void testUpdateTodo() throws Exception {
        TodoEntity updatedTodo = new TodoEntity();
        updatedTodo.setTodoId(1);
        updatedTodo.setTitle("Updated Todo");

        when(todoService.updateTodo(eq(1), ArgumentMatchers.<TodoEntity>any())).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Todo")));

        verify(todoService, times(1)).updateTodo(eq(1), ArgumentMatchers.<TodoEntity>any());
    }

    @Test
    void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk());

        verify(todoService, times(1)).deleteTodo(1);
    }

    @Test
    void testPatchTodo() throws Exception {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setCompleteYn(true);

        TodoEntity patchedTodo = new TodoEntity();
        patchedTodo.setTodoId(1);
        patchedTodo.setCompleteYn(true);

        when(todoService.getTodoById(1)).thenReturn(patchedTodo);
        when(todoService.updateTodo(eq(1), ArgumentMatchers.<TodoEntity>any())).thenReturn(patchedTodo);

        mockMvc.perform(patch("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completeYn", is(true)));

        verify(todoService, times(1)).getTodoById(1);
        verify(todoService, times(1)).updateTodo(eq(1), ArgumentMatchers.<TodoEntity>any());
    }
}
 