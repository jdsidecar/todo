package com.sidecarjd.todo.controllers;

import com.sidecarjd.todo.TodoApplication;
import com.sidecarjd.todo.data.TodoItem;
import com.sidecarjd.todo.repos.TodoItemRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {TodoApplication.class})
@TestPropertySource(locations = {"classpath:application-test.properties"})
@AutoConfigureMockMvc
public class TodoApiControllerTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TodoItemRepo repository;

    @BeforeEach
    public void createTestTodoItem() {
        repository.deleteAll();
    }

    @Test
    public void testGetAllTodoItems() throws Exception {
        List<TodoItem> items = getAllTodoItems();

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    public void testCreateTodoItem() throws Exception {
        TodoItem responseItem = createTodoItem("item1");
        
        assertEquals("item1", responseItem.getText());
        assertFalse(responseItem.isCompleted());
        assertNotNull(responseItem.getCreatedAt());
        assertNotNull(responseItem.getUpdatedAt());
    }

    @Test
    public void testUpdateItem() throws Exception {
        TodoItem todoItem = createTodoItem("item2");
        todoItem.setCompleted(true);

        TodoItem updatedTodo = updateTodoItem(todoItem);

        assertEquals(todoItem.getId(), updatedTodo.getId());
        assertTrue(updatedTodo.isCompleted());
        assertTrue(updatedTodo.getUpdatedAt().after(todoItem.getUpdatedAt()));
    }

    private List<TodoItem> getAllTodoItems() throws Exception {
        MvcResult result = mvc.perform(get("/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
       
        String resultString = result.getResponse().getContentAsString();

        return this.mapper.readValue(resultString, new TypeReference<List<TodoItem>>() {});
    }

    private TodoItem createTodoItem(String text) throws Exception {
        TodoItem requestItem = new TodoItem();
        requestItem.setText(text);
        requestItem.setCompleted(false);

        String json = this.mapper.writeValueAsString(requestItem);
        MvcResult result = mvc.perform(post("/todos").content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();

        String resultString = result.getResponse().getContentAsString();
        return this.mapper.readValue(resultString, new TypeReference<TodoItem>() {});
    }

    private TodoItem updateTodoItem(TodoItem requestItem) throws Exception {
        String json = this.mapper.writeValueAsString(requestItem);
        String url = "/todos/" + requestItem.getId();
        MvcResult result = mvc.perform(put(url).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();

        String resultString = result.getResponse().getContentAsString();
        return mapper.readValue(resultString, new TypeReference<TodoItem>() {});
    }
}