
package se.taggy.tests.util;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

public class TOrJson<T> {

	private final T t;
	private final JsonNode json;

	public TOrJson(T t) {
		this.t = t;
		this.json = null;
	}

	public TOrJson(JsonNode json) {
		this.t = null;
		this.json = json;
	}

	public boolean hasT() {
		return t != null;
	}

	public T getT() {
		Objects.requireNonNull(t);
		return t;
	}

	public JsonNode getJson() {
		Objects.requireNonNull(json);
		return json;
	}
}
