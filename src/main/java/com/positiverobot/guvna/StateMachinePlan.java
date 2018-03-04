package com.positiverobot.guvna;

import java.util.*;

public class StateMachinePlan<S, E> {

    protected List<E> _inputs;
    protected Map<S, List<S>> _transitions = new HashMap<S, List<S>>();

    /**
     * Register Inputs. Short method name to same space in the matrix, the first
     * input is the "loopback" which will cause reentry to the current state
     *
     * @param inputs the possible input events
     */
    public void ri(E... inputs) {
        _inputs = Arrays.asList(inputs);
    }

    /**
     * Transitions
     *
     * @param theTransitions the state transitions
     */
    public void at(S... theTransitions) {
        List<S> transitions = Arrays.asList(theTransitions);
        List<S> previousState = _transitions
                .put(transitions.get(0), transitions);
        if (previousState != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous transition map for [%s] is not allowed",
                            previousState));
        }
    }

    public int indexOf(E event) {
        return _inputs.indexOf(event);
    }
}