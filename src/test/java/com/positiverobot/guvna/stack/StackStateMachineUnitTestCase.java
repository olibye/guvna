package com.positiverobot.guvna.stack;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.positiverobot.guvna.stack.StackStateMachine;
import com.positiverobot.guvna.stack.StackStateMachinePlan;

public class StackStateMachineUnitTestCase {

    @Rule
    public JUnitRuleMockery mockery = new JUnitRuleMockery();

    @SuppressWarnings("unchecked")
    public static final class MouseMind extends StackStateMachinePlan<s, e> {
        {
            // @formatter:off
            ri(null, e.Wake, e.Wet, e.Dry);
            at(s.Hungry, null, push(s.Paddling), null);
            at(s.Paddling, null, null, pop());
        }
    }

    /**
     * States
     */
    public enum s {
        Sleeping, Hungry, Eating, Content, Paddling, Scared
    }

    /**
     * Events
     */
    public enum e {
        Wake, Wet, Dry
    };

    @Test
    public void checkPushTest() {
        StackStateMachinePlan<s, e> mouseMind = new MouseMind();

        StackStateMachine<s, e> mouse = new StackStateMachine<s, e>(mouseMind, s.Hungry);
        mouse.queue(e.Wet);
        mouse.processNextEvent();
        assertEquals(s.Paddling, mouse.getState());
    }

    @Test
    public void checkEmptyPopTest() {
        StackStateMachinePlan<s, e> mouseMind = new MouseMind();

        StackStateMachine<s, e> mouse = new StackStateMachine<s, e>(mouseMind, s.Paddling);
        mouse.queue(e.Dry);
        mouse.processNextEvent();
        assertEquals(s.Paddling, mouse.getState());
    }

    @Test
    public void checkPushPopTest() {
        StackStateMachinePlan<s, e> mouseMind = new MouseMind();

        StackStateMachine<s, e> mouse = new StackStateMachine<s, e>(mouseMind, s.Hungry);
        mouse.queue(e.Wet);
        mouse.processNextEvent();
        assertEquals(s.Paddling, mouse.getState());

        mouse.queue(e.Dry);
        mouse.processNextEvent();
        assertEquals(s.Hungry, mouse.getState());
    }
}
