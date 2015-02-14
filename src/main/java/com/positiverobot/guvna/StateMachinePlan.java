package com.positiverobot.guvna;

import java.util.*;

public class StateMachinePlan<T, S, E> {

    List<E> _inputs;
    Map<S, List<S>> _transitions = new HashMap<S, List<S>>();
    Map<S, Action<T,S,E>> _entryActions = new HashMap<S, Action<T,S,E>>();
    Map<S, Action<T,S,E>> _leaveActions = new HashMap<S, Action<T,S,E>>();

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

    public void entryAction(S state, Action<T,S,E> action) {
        Object previousAction = _entryActions.put(state, action);
        if (previousAction != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous entry Action for [%s] is not allowed",
                            previousAction));
        }
    }

    public void leaveAction(S state, Action<T,S,E> action) {
        Object previousAction = _leaveActions.put(state, action);
        if (previousAction != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous leave Action for [%s] is not allowed",
                            previousAction));
        }
    }
}