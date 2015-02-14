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

    /**
     * @param event
     *            will cause a runtime exception
     */
    public void queue(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Null events are invalid");
        }
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

        // Ignore null events as we can't tell if they're real
        // or indicate the queue is empty
        if (event == null) {
            return hasMoreEvents();
        }

        int indexOfEvent = _plan._inputs.indexOf(event);

        if (indexOfEvent >= 0) {
            // don't ignore loopback event in column zero

            LOG.debug("{} received event type:[{}]", this, event);

            List<?> list = _plan._transitions.get(_currentState);
            if (list != null) {
                if (list.size() > indexOfEvent) {
                    Object nextState = computeNextState(list.get(indexOfEvent));
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

    protected Object computeNextState(Object object) {
        return object;
    }

    public boolean hasMoreEvents() {
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