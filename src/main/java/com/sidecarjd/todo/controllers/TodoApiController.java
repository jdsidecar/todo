package com.sidecarjd.todo.controllers;

import java.util.List;
import java.util.Optional;

import com.sidecarjd.todo.data.TodoItem;
import com.sidecarjd.todo.services.TodoItemService;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j2
@RestController
public class TodoApiController {
    @Autowired
    private TodoItemService todoService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/todos")
    public ResponseEntity<List<TodoItem>> getAllTodoItems() {
        List<TodoItem> todoItems = todoService.findAll();

        HttpStatus statusCode = todoItems.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        ResponseEntity<List<TodoItem>> response = 
            new ResponseEntity<>(todoItems, statusCode);
        return response;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/todos/{id}")
    public ResponseEntity<TodoItem> getTodoItem(@PathVariable Long id, @RequestBody TodoItem requestTodoItem) {
        Optional<TodoItem> todoItem = todoService.findById(id);
        if (!todoItem.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(todoItem.get(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/todos")
    public ResponseEntity<TodoItem> createTodoItem(@RequestBody TodoItem requestTodoItem) {
        TodoItem responseTodoItem = todoService.create(requestTodoItem);

        ResponseEntity<TodoItem> response = new ResponseEntity<>(responseTodoItem, HttpStatus.OK);
        return response;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/todos/{id}")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long id, @RequestBody TodoItem requestTodoItem) {
        TodoItem responseTodoItem = todoService.update(id, requestTodoItem);

        return new ResponseEntity<>(responseTodoItem, HttpStatus.OK);
    }
}