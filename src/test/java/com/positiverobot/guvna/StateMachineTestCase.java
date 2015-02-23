package com.positiverobot.guvna;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class StateMachineTestCase {

    @Rule
    public Mockery _mockery = new JUnitRuleMockery();

    @Test
    public void testArraysAsList() {
        String[] stringArrary = { "one", "two", "three" };
        List<String> asList = Arrays.asList(stringArrary);

        assertEquals(3, asList.size());
    }

    @Test
    public void testEntryAction() {

        final Action<String, String> stateOneEntry = _mockery.mock(
                Action.class, "stateOneEntryAction");
        final Action<String, String> stateTwoEntry = _mockery.mock(
                Action.class, "stateTwoEntryAction");

        final Sequence transitions = _mockery
                .sequence("State transition order");

        StateMachinePlan<String, String> builder = new StateMachinePlan<String, String>();
        builder.ri("loopback", "nudge");
        builder.at("state1", "state2");
        builder.at("state2", "state1");
        final StateMachine<String, String> unit = new StateMachine<String, String>(builder,
                "state1");

        _mockery.checking(new Expectations() {
            {
                oneOf(stateOneEntry).apply(unit, "loopback", "state1");
                inSequence(transitions);

                oneOf(stateTwoEntry).apply(unit, "nudge", "state2");
                inSequence(transitions);

                oneOf(stateOneEntry).apply(unit, "nudge", "state1");
                inSequence(transitions);
            }
        });

        unit.entryAction("state1", stateOneEntry);
        unit.entryAction("state2", stateTwoEntry);

        // queue the loopback event to trigger the entry action for the start
        // state
        unit.queue("loopback");
        unit.processNextEvent();

        unit.queue("nudge");
        unit.queue("nudge");
        unit.processOutstandingEvents();
    }

    @Test
    public void testLeaveAction() {

        final Action<String, String> stateOneLeave = _mockery.mock(
                Action.class, "stateOneLeaveAction");
        final Action<String, String> stateTwoLeave = _mockery.mock(
                Action.class, "stateTwoLeaveAction");

        final Sequence transitions = _mockery
                .sequence("State transition order");

        StateMachinePlan<String, String> builder = new StateMachinePlan<String, String>();
        // @formatter:off
		builder.ri(null    , "nudge");
		builder.at("state1", "state2");
		builder.at("state2", "state1");
		// @formatter:on
        final StateMachine<String, String> unit = new StateMachine<String, String>(builder,
                "state1");

        _mockery.checking(new Expectations() {
            {
                oneOf(stateOneLeave).apply(unit, "nudge", "state2");
                inSequence(transitions);

                oneOf(stateTwoLeave).apply(unit, "nudge", "state1");
                inSequence(transitions);
            }
        });

        unit.leaveAction("state1", stateOneLeave);
        unit.leaveAction("state2", stateTwoLeave);

        unit.queue("nudge");
        unit.queue("nudge");
        unit.processOutstandingEvents();
    }
}
