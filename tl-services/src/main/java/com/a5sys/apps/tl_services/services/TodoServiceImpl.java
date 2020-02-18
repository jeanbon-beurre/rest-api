package com.a5sys.apps.tl_services.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a5sys.apps.tl_common.entities.Todo;
import com.a5sys.apps.tl_services.repositories.TodoRepository;

@Service
class TodoServiceImpl implements TodoService {

	private final TodoRepository todoRepository;

	@Autowired
	TodoServiceImpl(final TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public List<Todo> findAllTodos() {
		return (List<Todo>) todoRepository.findAll();
	}

	@Override
	public List<Todo> findActiveTodos() {
		return todoRepository.findByCompleted(false);
	}

	@Override
	public List<Todo> findCompletedTodos() {
		return todoRepository.findByCompleted(true);
	}

	@Override
	public Todo findTodoById(long id) {
		return todoRepository.findById(id).orElse(null);
	}

	@Override
	public boolean existsTodoByLabelIgnoreCase(String label) {
		return !todoRepository.findByLabelIgnoreCase(label).isEmpty();
	}

	@Override
	public Todo addTodo(final Todo todo) {
		todo.setCompleted(false);
		return todoRepository.save(todo);
	}

	@Override
	public List<Todo> addTodos(final Iterable<Todo> todos) {
		todos.forEach(todo -> todo.setCompleted(false));
		return (List<Todo>) todoRepository.saveAll(todos);
	}

	@Override
	public Todo updateTodo(final Todo todo) {
		return todoRepository.save(todo);
	}

	@Override
	public void deleteTodo(final Todo todo) {
		todoRepository.delete(todo);
	}

	@Override
	@Transactional
	public void deleteCompletedTodos() {
		todoRepository.deleteByCompleted(true);
	}

}
