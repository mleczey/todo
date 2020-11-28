package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.Tag;
import java.util.Optional;

public interface TagDataAccess {

  Tag saveAndFlush(final Tag tag);

  Optional<Tag> findByName(final String name);
}
