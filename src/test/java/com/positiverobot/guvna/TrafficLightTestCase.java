package com.positiverobot.guvna;

import static org.junit.Assert.assertEquals;

import org.jmock.Mockery;
import org.junit.Test;

import com.positiverobot.guvna.TrafficLightFSM.e;
import com.positiverobot.guvna.TrafficLightFSM.s;

public class TrafficLightTestCase {
    public Mockery _mockery = new Mockery();

    @Test
    public void testTrafficLightTransitionMatrix() {

        StateMachine<s, e> unit = new TrafficLightFSM(s.Green);

        assertEquals("Wrong state", s.Green, unit.getState());

        unit.queue(e.PushButn);
        unit.processNextEvent();
        assertEquals("Wrong state", s.Amber, unit.getState());

        unit.queue(e.WaitTime);
        unit.processNextEvent();
        assertEquals("Wrong state", s.Red, unit.getState());

        unit.queue(e.EndBeeps);
        unit.processNextEvent();
        assertEquals("Wrong state", s.RedAmber, unit.getState());

        unit.queue(e.WaitTime);
        unit.processNextEvent();
        assertEquals("Wrong state", s.Green, unit.getState());

        _mockery.assertIsSatisfied();
    }
}
