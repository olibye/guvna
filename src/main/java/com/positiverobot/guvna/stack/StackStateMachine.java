package com.positiverobot.guvna.stack;

import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.positiverobot.guvna.StateMachine;

/**
 * Support a stack based state machine
 */
public class StackStateMachine<S, E> extends StateMachine<S, E> {
    private static final Logger LOG = LoggerFactory.getLogger(StackStateMachine.class);

    protected final Stack<S> _stack = new Stack<S>();
    private StackStateMachinePlan<S, E> _splan;

    public StackStateMachine(StackStateMachinePlan<S, E> plan, S aStartState) {
        super(plan, aStartState);
        this._splan = plan;
        _stack.push(aStartState);
    }

    @Override
    protected S computeNextState(E event) {
        int indexOfEvent = _plan.indexOf(event);

        if (indexOfEvent >= 0) {
            // don't ignore loopback event in column zero
            List<Transition<S, E>> list = _splan._stackTransitions.get(_currentState);
            if (list != null) {
                if (list.size() > indexOfEvent) {
                    return list.get(indexOfEvent).apply(this);
                } else {
                    LOG.debug("{} has no transition for registered event:[{}]",
                            this, event);
                }
            } else {
                return super.computeNextState(event);
            }
        } else {
            LOG.error("{} received Unknown event type:[{}]", this, event);
        }
        return null;
    }

    /**
     * Allow the plan to pop the stack into the current state
     */
    static final class Pop<S, E> implements Transition<S, E> {
        @Override
        public S apply(StackStateMachine<S, E> ssm) {
            if (ssm._stack.size() == 1) {
                LOG.error("Top of stack is the current state");
                return null;
            }
            ssm._stack.pop();
            return ssm._stack.peek();
        }
    }

    /**
     * Allow the plan to push a new state onto the stack
     */
    static final class Push<S, E> implements Transition<S, E> {
        private final S newState;

        Push(S newState) {
            this.newState = newState;
        }

        @Override
        public S apply(StackStateMachine<S, E> ssm) {
            return ssm._stack.push(newState);
        }
    }

    /**
     * Swap the current state at the top of the stack for a new state
     */
    static final class Swap<S, E> implements Transition<S, E> {
        private final S newState;

        Swap(S newState) {
            this.newState = newState;
        }

        @Override
        public S apply(StackStateMachine<S, E> ssm) {
            ssm._stack.pop();
            return ssm._stack.push(newState);
        }
    }

    public abstract static interface Transition<S, E> {
        public abstract S apply(StackStateMachine<S, E> stackStateMachine);
    }

}
