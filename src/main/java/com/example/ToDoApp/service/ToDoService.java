package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Status;
import com.example.ToDoApp.model.ToDo;
import com.example.ToDoApp.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    private final ToDoRepository repo;

    public ToDoService(ToDoRepository repo) {
        this.repo = repo;
    }

    public List<ToDo> listAll(String filter) {
        if ("pending".equalsIgnoreCase(filter)) {
            return repo.findByStatusOrderByCreatedAtDesc(Status.PENDING);
        }
        if ("done".equalsIgnoreCase(filter)) {
            return repo.findByStatusOrderByCreatedAtDesc(Status.DONE);
        }
        return repo.findAllByOrderByCreatedAtDesc();
    }

   
    public ToDo save(ToDo todo) {
        if (todo.getCreatedAt() == null) {
            todo.setCreatedAt(LocalDateTime.now());
        }
        
        if (todo.getStatus() == null) {
            todo.setStatus(Status.PENDING);
        }
        return repo.save(todo);
    }

    public Optional<ToDo> get(Long id) {
        return repo.findById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void markDone(Long id) {
        repo.findById(id).ifPresent(t -> {
            t.setStatus(Status.DONE);
            repo.save(t);
        });
    }
}