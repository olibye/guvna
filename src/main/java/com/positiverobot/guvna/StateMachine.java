package com.positiverobot.guvna;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Not threadsafe.
 */
public class StateMachine<S,E> {
    private final Logger LOG = LoggerFactory.getLogger(StateMachine.class);

    private S _currentState;
    private StateMachinePlan<S,E> _plan;
    private Queue<E> _eventQueue = new LinkedList<>();
    private Map<S, Action<S,E>> _entryActions = new HashMap<>();
    private Map<S, Action<S,E>> _leaveActions = new HashMap<>();

    public StateMachine(StateMachinePlan<S,E> plan, S aStartState) {
        _plan = plan;
        _currentState = aStartState;
    }

    public S getState() {
        return _currentState;
    }

    /**
     * @param event
     *            will cause a runtime exception
     */
    public void queue(E event) {
        if (event == null) {
            throw new IllegalArgumentException("Null events are invalid");
        }
        _eventQueue.add(event);
    }

    private void performTransition(S nextState, E event) {
        Action<S,E> leaveAction = _leaveActions.get(_currentState);
        if (leaveAction != null) {
            leaveAction.apply(this, event, nextState);
        }

        Action<S,E> entryAction = _entryActions.get(nextState);
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
        E event = _eventQueue.poll();

        // Ignore null events as we can't tell if they're real
        // or indicate the queue is empty
        if (event == null) {
            return hasMoreEvents();
        }

        int indexOfEvent = _plan._inputs.indexOf(event);

        if (indexOfEvent >= 0) {
            // don't ignore loopback event in column zero

            LOG.debug("{} received event type:[{}]", this, event);

            List<S> list = _plan._transitions.get(_currentState);
            if (list != null) {
                if (list.size() > indexOfEvent) {
                    S nextState = computeNextState(list.get(indexOfEvent));
                    if (nextState != null) {
                        performTransition(nextState, event);
                        LOG.debug("{} processed event type:[{}]", this, event);
                    } else {
                        LOG.debug(
                                "{} has no transition for registered event:[{}]",
                                this, event);
                    }
                } else {
                    LOG.debug("{} has no transition for registered event:[{}]",
                            this, event);
                }
            } else {
                LOG.error("Unknown state:[{}]", _currentState);
            }
        } else {
            LOG.error("{} received Unknown event type:[{}]", this, event);
        }
        return hasMoreEvents();
    }

    protected S computeNextState(S object) {
        return object;
    }

    public boolean hasMoreEvents() {
        return _eventQueue.peek() != null;
    }

    public void processOutstandingEvents() {
        while (processNextEvent()) {
        }
    }

    public void entryAction(S state, Action<S,E> action) {
        Object previousAction = _entryActions.put(state, action);
        if (previousAction != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous entry Action for [%s] is not allowed",
                            previousAction));
        }
    }

    public void leaveAction(S state, Action<S,E> action) {
        Object previousAction = _leaveActions.put(state, action);
        if (previousAction != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous leave Action for [%s] is not allowed",
                            previousAction));
        }
    }
    
    @Override
    public String toString() {
        return "StateMachine [_currentState=" + _currentState
                + ", _eventQueue=" + _eventQueue + "]";
    }
}