package com.positiverobot.guvna;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Not threadsafe.
 */
public class StateMachine {
	private final Logger LOG = LoggerFactory.getLogger(StateMachine.class);

	private Object _currentState;
	private StateMachinePlan _plan;
	private Queue<Object> _eventQueue = new LinkedList<Object>();

	public StateMachine(StateMachinePlan plan, Object aStartState) {
		_plan = plan;
		_currentState = aStartState;
	}

	public Object getState() {
		return _currentState;
	}

	public void queue(Object event) {
		_eventQueue.add(event);
	}

	private void performTransition(Object nextState, Object event) {
		Action leaveAction = _plan._leaveActions.get(_currentState);
		if (leaveAction != null) {
			leaveAction.apply(this, event, nextState);
		}

		Action entryAction = _plan._entryActions.get(nextState);
		if (entryAction != null) {
			entryAction.apply(this, event, nextState);
		}
		// entry actions performed before state change
		_currentState = nextState;
	}

	/**
	 * 
	 * @return true if more exist
	 */
	public boolean processNextEvent() {
		Object event = _eventQueue.poll();

		// allow null events, usually the reentry event
		int indexOfEvent = _plan._inputs.indexOf(event);

		if (indexOfEvent >= 0) {
			// don't ignore reentry event

			LOG.info("{} received event type:[{}]", this, event);

			List<?> list = _plan._transitions.get(_currentState);
			if (list != null) {
				if (list.size() > indexOfEvent) {
					Object nextState = list.get(indexOfEvent);
					if (nextState != null) {
						performTransition(nextState, event);
						LOG.info("{} processed event type:[{}]", this, event);
					} else {
						LOG.info(
								"{} has no transition for registered event:[{}]",
								this, event);
					}
				} else {
					LOG.info("{} has no transition for registered event:[{}]",
							this, event);
				}
			} else {
				LOG.error("Unknown state:[{}]", _currentState);
			}
		} else {
			LOG.error("{} received Unknown event type:[{}]", this, event);
		}
		return _eventQueue.peek() != null;
	}

	public void processOutstandingEvents() {
		while (processNextEvent()) {
		}
	}

	@Override
	public String toString() {
		return "StateMachine [_currentState=" + _currentState
				+ ", _eventQueue=" + _eventQueue + "]";
	}
}