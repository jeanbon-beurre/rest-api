package com.a5sys.apps.tl_rest_api;

import org.springframework.http.HttpStatus;

public final class Payload {

	private final long status;
	
	private final String message;

	public Payload(final HttpStatus httpStatus, String message) {
		this.status = httpStatus.value();
		this.message = message;
	}

	public long getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 17;
		result = prime * result + (int) (status ^ (status >>> 32));
		result = prime * result + ((message == null) ? 0 : message.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Payload)) {
			return false;
		}

		final Payload that = (Payload) other;

		if (status != that.status) {
			return false;
		}

		if (message == null) {
			if (that.message != null) {
				return false;
			}
		} else if (!message.equals(that.message)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Payload [status=" + status + ", message=" + message + "]";
	}

}
