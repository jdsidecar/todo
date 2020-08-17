package com.sidecarjd.todo.repos;

import com.sidecarjd.todo.data.TodoItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepo extends JpaRepository<TodoItem, Long> {
    
}