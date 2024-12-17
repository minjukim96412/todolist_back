package com.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;  // 추가된 import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // Optional: content 체크용

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk());
    }
}

