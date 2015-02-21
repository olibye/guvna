package com.positiverobot.guvna.stack;

import java.util.Stack;

import com.positiverobot.guvna.StateMachine;

public class StackStateMachine<S,E> extends StateMachine<S, E> {

    protected final Stack<S> _stack = new Stack<S>();

    static final class Pop<S,E> implements Transition<S,E> {
        public S apply(StackStateMachine<S,E> ssm) {
            return ssm._stack.pop();
        }
    }

    static final class Push<S,E> implements Transition<S,E> {
        private final S newState;

        Push(S newState) {
            this.newState = newState;
        }

        public S apply(StackStateMachine<S,E> ssm) {
            return ssm._stack.push(newState);
        }
    }

    static final class Swap<S,E> implements Transition<S,E> {
        private final S newState;

        Swap(S newState) {
            this.newState = newState;
        }

        public S apply(StackStateMachine<S,E> ssm) {
            ssm._stack.pop();
            return ssm._stack.push(newState);
        }
    }

    public abstract static interface Transition<S,E> {
        public abstract S apply(StackStateMachine<S,E> stackStateMachine);
    }

    public StackStateMachine(StackStateMachinePlan<S, E> plan, S aStartState) {
        super(plan, aStartState);
    }

}
