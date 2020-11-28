package com.mleczey.todo.task.boundary;

import com.mleczey.todo.task.control.TagDataAccess;
import com.mleczey.todo.task.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagDataAccess {
}
