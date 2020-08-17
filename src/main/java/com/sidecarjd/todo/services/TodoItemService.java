package com.sidecarjd.todo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.sidecarjd.todo.data.TodoItem;
import com.sidecarjd.todo.repos.TodoItemRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoItemService {
    @Autowired
    private TodoItemRepo todoRepo;

    public List<TodoItem> findAll() {
        return todoRepo.findAll();
    }

    public Optional<TodoItem> findById(Long id) {
        return todoRepo.findById(id);
    }

    public TodoItem create(TodoItem todoItem) {
        Date now = new Date();

        todoItem.setCreatedAt(now);
        todoItem.setUpdatedAt(now);

        return todoRepo.save(todoItem);
    }

    public TodoItem update(TodoItem todoItem) {
        todoItem.setUpdatedAt(new Date());

        return todoRepo.save(todoItem);
    }
}