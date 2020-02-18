package com.a5sys.apps.tl_rest_api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.a5sys.apps.tl_common.entities.Todo;
import com.a5sys.apps.tl_services.services.TodoService;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.a5sys.apps")
@EntityScan(basePackages = { "com.a5sys.apps.tl_common.entities" })
@EnableJpaRepositories(basePackages = { "com.a5sys.apps.tl_services.repositories" })
public class TlRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlRestApiApplication.class, args);
	}

	@Bean
	CommandLineRunner loadTodos(TodoService todoService) {
		final List<String> tasks = Arrays.asList(
			"Learn Maven",
			"Learn Java 8",
			"Learn Spring Boot",
			"Learn TypeScript",
			"Learn Angular",
			"Learn React"
		);

		final AtomicLong idGenerator = new AtomicLong();

		return args -> {
			final List<Todo> todos = tasks.stream()
					.map(task -> new Todo(idGenerator.incrementAndGet(), task))
					.collect(Collectors.toList());

			todoService.addTodos(todos);
		};
	}

}
