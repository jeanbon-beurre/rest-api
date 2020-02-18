package com.a5sys.apps.tl_services.services;

import java.util.List;

import com.a5sys.apps.tl_common.entities.Todo;

public interface TodoService {

	List<Todo> findAllTodos();

	List<Todo> findActiveTodos();

	List<Todo> findCompletedTodos();

	Todo findTodoById(long id);

	boolean existsTodoByLabelIgnoreCase(String label);

	Todo addTodo(final Todo todo);

	List<Todo> addTodos(final Iterable<Todo> todos);

	Todo updateTodo(final Todo todo);

	void deleteTodo(final Todo todo);

	void deleteCompletedTodos();

}
