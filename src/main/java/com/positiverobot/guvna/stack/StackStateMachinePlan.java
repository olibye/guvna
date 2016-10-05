package com.positiverobot.guvna.stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.positiverobot.guvna.StateMachinePlan;
import com.positiverobot.guvna.stack.StackStateMachine.Pop;
import com.positiverobot.guvna.stack.StackStateMachine.Push;
import com.positiverobot.guvna.stack.StackStateMachine.Swap;
import com.positiverobot.guvna.stack.StackStateMachine.Transition;

/**
 * My plans can include pop() and push(state)
 * 
 * @author byeo
 */
public class StackStateMachinePlan<S, E> extends StateMachinePlan<S, E> {
    // To allow extension
    protected Map<S, List<Transition<S, E>>> _stackTransitions = new HashMap<S, List<Transition<S, E>>>();

    /**
     * Swap the active for a new state
     * 
     * @return the new active state
     */
    protected Transition<S, E> swap(final S newState) {
        return new Swap<S, E>(newState);
    }

    /**
     * Push a new active state onto the stack
     * 
     * @return the new active state
     */
    protected Transition<S, E> push(final S newState) {
        return new Push<S, E>(newState);
    }

    /**
     * Pop and return next state as the current state
     */
    protected Transition<S, E> pop() {
        return new Pop<S, E>();
    }

    /**
     * Transitions
     * 
     * @param theTransitions
     */
    public void at(S fromState, Transition<S, E>... theTransitions) {
        // prepend null transition to match original API
        ArrayList<Transition<S, E>> transitionsList = new ArrayList<Transition<S, E>>(
                theTransitions.length + 1);
        transitionsList.addAll(Arrays.asList(theTransitions));
        transitionsList.add(0, swap(fromState));
        nullMeansSwap(fromState, transitionsList);

        List<Transition<S, E>> previousState = _stackTransitions.put(fromState,
                transitionsList);
        if (previousState != null) {
            throw new IllegalStateException(
                    String.format(
                            "Replacing the previous transition map for [%s] is not allowed",
                            previousState));
        }
    }

    private void nullMeansSwap(S fromState,
            List<Transition<S, E>> transitionList) {
        
        for (int i = 0; i < transitionList.size(); i++) {
            Transition<S, E> transition = transitionList.get(i);
            transitionList.set(i, transition == null ? swap(fromState)
                    : transition);
        }
    }
}
