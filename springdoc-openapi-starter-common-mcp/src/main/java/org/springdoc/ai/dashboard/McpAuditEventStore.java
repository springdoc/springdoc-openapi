/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.ai.dashboard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springdoc.ai.mcp.McpAuditLogger;

/**
 * In-memory ring buffer that stores the most recent MCP audit events produced by
 * {@link McpAuditLogger}. Events are stored as serialized JSON strings exactly as they
 * were written to the audit logger, so the dashboard can replay them without
 * re-serialisation.
 *
 * <p>
 * At most {@value #MAX_EVENTS} events are retained; older entries are discarded
 * automatically.
 *
 * @author bnasslahsen
 */
public class McpAuditEventStore {

	/**
	 * Maximum number of audit events kept in memory.
	 */
	static final int MAX_EVENTS = 500;

	/**
	 * Guarded by {@code this}. Most-recent entry is at the head.
	 */
	private final Deque<String> events = new ArrayDeque<>(MAX_EVENTS + 1);

	/**
	 * Registers this store with {@link McpAuditLogger} so that every audit event is also
	 * pushed into the in-memory buffer.
	 */
	@PostConstruct
	void register() {
		McpAuditLogger.setEventSink(this::add);
	}

	/**
	 * Unregisters this store from {@link McpAuditLogger} on bean destruction.
	 */
	@PreDestroy
	void unregister() {
		McpAuditLogger.setEventSink(null);
	}

	/**
	 * Adds a new JSON audit event to the front of the deque, evicting the oldest entry if
	 * the capacity limit is reached.
	 * @param json the serialized JSON audit event
	 */
	synchronized void add(String json) {
		events.addFirst(json);
		if (events.size() > MAX_EVENTS) {
			events.pollLast();
		}
	}

	/**
	 * Returns the most recent audit events, newest first.
	 * @param limit maximum number of events to return (clamped to {@value #MAX_EVENTS})
	 * @return immutable snapshot list
	 */
	public synchronized List<String> getRecentEvents(int limit) {
		int cap = Math.min(limit, MAX_EVENTS);
		List<String> result = new ArrayList<>(Math.min(cap, events.size()));
		int count = 0;
		for (String e : events) {
			if (count++ >= cap) {
				break;
			}
			result.add(e);
		}
		return List.copyOf(result);
	}

	/**
	 * Clears all stored audit events.
	 */
	public synchronized void clear() {
		events.clear();
	}

}
