package com.positiverobot.guvna;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Rule;
import org.junit.Test;

public class StateMachineTestCase {
	public Mockery _mockery = new Mockery();

	@Test
	public void testArraysAsList() {
		String[] stringArrary = { "one", "two", "three" };
		List<String> asList = Arrays.asList(stringArrary);

		assertEquals(3, asList.size());
	}

	@Test
	public void testEntryAction() {

		final Action stateOneEntry = _mockery.mock(Action.class,
				"stateOneEntryAction");
		final Action stateTwoEntry = _mockery.mock(Action.class,
				"stateTwoEntryAction");

		final Object target = new Object();

		final Sequence transitions = _mockery
				.sequence("State transition order");

		StateMachinePlan builder = new StateMachinePlan();
		builder.ri(null, "nudge");
		builder.at("state1", "state2");
		builder.at("state2", "state1");
		final StateMachine unit = new StateMachine(builder, "state1");

		_mockery.checking(new Expectations() {
			{
				oneOf(stateOneEntry).apply(unit,null);
				inSequence(transitions);

				oneOf(stateTwoEntry).apply(unit,"nudge");
				inSequence(transitions);

				oneOf(stateOneEntry).apply(unit,"nudge");
				inSequence(transitions);
			}
		});


		builder.entryAction("state1", stateOneEntry);
		builder.entryAction("state2", stateTwoEntry);


		// queue the loopback event to trigger the entry action for the start
		// state
		unit.queue(null);
		unit.processNextEvent();

		unit.queue("nudge");
		unit.queue("nudge");
		unit.processOutstandingEvents();
		
		_mockery.assertIsSatisfied();
	}

	@Test
	public void testTrafficLightTransitionMatrix() {

		final Object sRED_____ = "Red";
		final Object sRedAmber = "Red & Amber";
		final Object sGreen___ = "Green";
		final Object sAmber___ = "Amber";

		final Object e________ = null;
		final Object ePushButn = new Object();
		final Object eEndBeeps = new Object();
		final Object eWaitTime = new Object();

		class TrafficLightPlan extends StateMachinePlan {
			Action startBeepTimer = new Action() {
				public void apply(Object aSubject, Object eve) {
				}
			};

			Action startWaitTimer = new Action() {
				public void apply(Object aSubject, Object event) {
				}
			};

			TrafficLightPlan() {
				ri(e________, ePushButn, eEndBeeps, eWaitTime);

				at(sRED_____, e________, sRedAmber, e________);
				at(sRedAmber, e________, e________, sGreen___);
				at(sGreen___, sAmber___, e________, e________);
				at(sAmber___, e________, e________, sRED_____);

				entryAction(sRED_____, startBeepTimer);
				entryAction(sRedAmber, startWaitTimer);
				entryAction(sAmber___, startWaitTimer);
			}
		}

		StateMachine unit = new StateMachine(new TrafficLightPlan(), sGreen___);

		assertEquals("Wrong state", sGreen___, unit.getState());

		unit.queue(ePushButn);
		unit.processNextEvent();
		assertEquals("Wrong state", sAmber___, unit.getState());

		unit.queue(eWaitTime);
		unit.processNextEvent();
		assertEquals("Wrong state", sRED_____, unit.getState());

		unit.queue(eEndBeeps);
		unit.processNextEvent();
		assertEquals("Wrong state", sRedAmber, unit.getState());

		unit.queue(eWaitTime);
		unit.processNextEvent();
		assertEquals("Wrong state", sGreen___, unit.getState());
		
		_mockery.assertIsSatisfied();
	}
}
