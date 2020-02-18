package com.a5sys.apps.tl_common.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class Todo implements Comparable<Todo> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String label;

	private boolean completed;

	public Todo() {}

	public Todo(long id, String label) {
		this.id = id;
		this.label = label;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 17;
		result = prime * result + (int) (id ^ (id >>> 32));

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Todo)) {
			return false;
		}

		final Todo that = (Todo) other;

		return id == that.id;
	}

	@Override
	public int compareTo(Todo that) {
		if (completed) {
			if (!that.completed) {
				return 1;
			}
		} else if (that.completed) {
			return -1;
		}

		return label.compareTo(that.label);
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", label=" + label + ", completed=" + completed + "]";
	}

}
