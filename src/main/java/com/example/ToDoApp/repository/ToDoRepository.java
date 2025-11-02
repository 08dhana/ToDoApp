package com.example.ToDoApp.repository;

import com.example.ToDoApp.model.Status;
import com.example.ToDoApp.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findByStatusOrderByCreatedAtDesc(Status status);
    List<ToDo> findAllByOrderByCreatedAtDesc();
}
