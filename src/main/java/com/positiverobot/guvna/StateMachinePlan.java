package com.positiverobot.guvna;

import java.util.*;

public class StateMachinePlan<S, E> {

    List<E> _inputs;
    Map<S, List<S>> _transitions = new HashMap<S, List<S>>();

    /**
     * Register Inputs. Short method name to same space in the matrix, the first
     * input is the "loopback" which will cause reentry to the current state
     * 
     * @param inputs
     */
    @SuppressWarnings("unchecked")
    public void ri(E... inputs) {
        _inputs = Arrays.asList(inputs);
    }

    /**
     * Transitions
     * 
     * @param theTransitions
     */
    @SuppressWarnings("unchecked")
    public void at(S... theTransitions) {
        List<S> transitions = Arrays.asList(theTransitions);
        Object previousState = _transitions
                .put(transitions.get(0), transitions);
        if (previousState != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous transition map for [%s] is not allowed",
                            previousState));
        }
    }
}