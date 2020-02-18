package com.a5sys.apps.tl_rest_api.controllers;

import static com.a5sys.apps.tl_common.enums.TodoCompletionStatus.ACTIVE;
import static com.a5sys.apps.tl_common.enums.TodoCompletionStatus.COMPLETED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.a5sys.apps.tl_common.entities.Todo;
import com.a5sys.apps.tl_common.enums.TodoCompletionStatus;
import com.a5sys.apps.tl_rest_api.Payload;
import com.a5sys.apps.tl_services.services.TodoService;

@RestController
@RequestMapping("/api/v1/todos")
@CrossOrigin("http://localhost:4200")
public final class TodoListController {

	private final TodoService todoService;

	@Autowired
	public TodoListController(final TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping(produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getTodos(@RequestParam(name = "status", required = false) String completionStatus) {
		if (completionStatus == null) {
			// No filter requested : fetch all todos
			return ResponseEntity.ok(todoService.findAllTodos());
		}

		final Set<TodoCompletionStatus> supportedCompletionStatuses = EnumSet.of(ACTIVE, COMPLETED);

		try {
			final TodoCompletionStatus todoCompletionStatus = TodoCompletionStatus.valueOf(completionStatus.toUpperCase());

			if (!supportedCompletionStatuses.contains(todoCompletionStatus)) {
				throw new IllegalArgumentException();
			}

			if (todoCompletionStatus == ACTIVE) {
				return ResponseEntity.ok(todoService.findActiveTodos());
			}

			return ResponseEntity.ok(todoService.findCompletedTodos());
		} catch (final IllegalArgumentException e) {
			final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

			final String message = String.format(
				"Value [%1$s] is not supported for request parameter 'status' : only %2$s are allowed",
				completionStatus,
				supportedCompletionStatuses.stream()
					.map(status -> String.format("[%1$s]", status.name().toLowerCase()))
					.collect(Collectors.joining(" and "))
			);

			final Payload payload = new Payload(httpStatus, message);

			return new ResponseEntity<Payload>(payload, httpStatus);
		}
	}

	@PostMapping(consumes = { APPLICATION_JSON_VALUE }, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addTodo(@RequestBody final Todo todo, UriComponentsBuilder uriComponentsBuilder) {
		final String todoLabel = todo.getLabel();

		if (todoService.existsTodoByLabelIgnoreCase(todoLabel)) {
			final HttpStatus httpStatus = HttpStatus.CONFLICT;

			final String message = String.format(
				"Addition of todo [%1$s] has failed : a todo with the same label (case insensitive) already exists",
				todoLabel
			);

			final Payload payload = new Payload(httpStatus, message);

			return new ResponseEntity<Payload>(payload, httpStatus);
		}

		final Todo createdTodo = todoService.addTodo(todo);

		final URI location = uriComponentsBuilder.path("/api/v1/todos/{id}")
				.buildAndExpand(createdTodo.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping(value = "/{id}", consumes = { APPLICATION_JSON_VALUE }, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<?> toggleTodoCompletion(@PathVariable long id, @RequestBody final Todo updates) {
		final Todo todo = todoService.findTodoById(id);

		if (todo == null) {
			return ResponseEntity.notFound().build();
		}

		todo.setCompleted(updates.isCompleted());

		final Todo updatedTodo = todoService.updateTodo(todo);

		return ResponseEntity.ok(updatedTodo);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteTodo(@PathVariable long id) {
		final Todo todo = todoService.findTodoById(id);

		if (todo == null) {
			return ResponseEntity.notFound().build();
		}

		todoService.deleteTodo(todo);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/completed")
	public ResponseEntity<Object> clearCompletedTodos() {
		todoService.deleteCompletedTodos();

		return ResponseEntity.noContent().build();
	}

}
