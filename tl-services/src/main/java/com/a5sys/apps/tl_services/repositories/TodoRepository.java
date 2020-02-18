package com.a5sys.apps.tl_services.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.a5sys.apps.tl_common.entities.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {

	List<Todo> findByCompleted(boolean completed);

	List<Todo> findByLabelIgnoreCase(String label);

	void deleteByCompleted(boolean completed);

}
