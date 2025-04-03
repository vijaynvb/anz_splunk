package com.company.todoapp.services.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.todoapp.models.Todo;
import com.company.todoapp.repository.TodoJPARepository;
import com.company.todoapp.services.ITodoService;

@Service
public class TodoDBService implements ITodoService {

    private static final Logger logger = LogManager.getLogger(TodoDBService.class);

    @Autowired
    private TodoJPARepository todoDbRepo;

    @Override
    public List<Todo> getAllTodos() {
        try {
            logger.info("Fetching all todos");
            var todos = todoDbRepo.findAll();
            logger.debug("Todos retrieved: {}", todos.size());
            return todos;
        } catch (Exception e) {
            logger.error("TodoError fetching all todos", e);
            return null;
        }
    }

    @Override
    public Todo getById(int id) {
        try {
            logger.info("Fetching todo by ID: {}", id);
            Optional<Todo> todos = todoDbRepo.findById(id);
            
         // Force an exception for testing
            if (id == -1) {
                throw new RuntimeException("Forced exception for testing");
            }
            
            if (todos.isPresent()) {
                logger.debug("Todo found: {}", todos.get());
                return todos.get();
            } else {
                logger.warn("TodoWarn with ID {} not found", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("TodoError fetching todo by ID: {}", id, e);
            return null;
        }
    }

    @Override
    public Todo addTodo(Todo todo) {
        try {
            logger.info("Adding new todo: {}", todo.getTitle());
            Todo savedTodo = todoDbRepo.save(todo);
            logger.debug("Todo added with ID: {}", savedTodo.getId());
            return savedTodo;
        } catch (Exception e) {
            logger.error("TodoError adding todo: {}", todo.getTitle(), e);
            return null;
        }
    }

    @Override
    public Todo updateTodo(int id, Todo todo) {
        try {
            logger.info("Updating todo with ID: {}", id);
            Optional<Todo> existingTodo = todoDbRepo.findById(id);
            if (existingTodo.isPresent()) {
                Todo updateTodo = existingTodo.get();
                updateTodo.setTitle(todo.getTitle());
                todoDbRepo.save(updateTodo);
                logger.debug("Todo updated: {}", updateTodo);
                return updateTodo;
            } else {
                logger.warn("Todo with ID {} not found for update", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("TodoError updating todo with ID: {}", id, e);
            return null;
        }
    }

    @Override
    public Todo deleteTodo(int id) {
        try {
            logger.info("Deleting todo with ID: {}", id);
            Optional<Todo> existingTodo = todoDbRepo.findById(id);
            if (existingTodo.isPresent()) {
                todoDbRepo.deleteById(id);
                logger.debug("Todo deleted: {}", existingTodo.get());
                return existingTodo.get();
            } else {
                logger.warn("TodoWarn with ID {} not found for deletion", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("TodoError deleting todo with ID: {}", id, e);
            return null;
        }
    }

    @Override
    public List<Todo> getByTitle(String title) {
        try {
            logger.info("Fetching todos by title: {}", title);
            var todos = todoDbRepo.findByTitle(title);
            logger.debug("Todos found: {}", todos.size());
            return todos;
        } catch (Exception e) {
            logger.error("TodoError fetching todos by title: {}", title, e);
            return null;
        }
    }
}
