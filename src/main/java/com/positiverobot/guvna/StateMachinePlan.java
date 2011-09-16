package com.positiverobot.guvna;

import java.util.*;

public class StateMachinePlan {

	List<?> _inputs;
	Map<Object, List<?>> _transitions = new HashMap<Object, List<?>>();
	Map<Object, Action> _entryActions = new HashMap<Object, Action>();

	public void ri(Object... inputs) {
		_inputs = Arrays.asList(inputs);
	}

	public void at(Object... theTransitions) {
		List<Object> transitions = Arrays.asList(theTransitions);
		Object previousState = _transitions
				.put(transitions.get(0), transitions);
		if (previousState != null) {
			throw new IllegalStateException(
					String.format(
							"Replacing the previous transition map for [%s] is not allowed",
							previousState));
		}
	}

	public void entryAction(Object state, Action action) {
		Object previousState = _entryActions.put(state, action);
		if (previousState != null) {
			throw new IllegalStateException(
					String.format(
							"Replacing the previous entry action for [%s] is not allowed",
							previousState));
		}
	}
}