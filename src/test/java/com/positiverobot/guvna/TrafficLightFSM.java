package com.positiverobot.guvna;

/**
 * Here's the way I want to layout my state machines. I use the eclipse @formatter
 * on/off in comments around my state transition matrix.
 * 
 * In Eclipse make your own code formatting profile and enable the formatter
 * on/off tags
 * 
 * It's a pedestrian friendly English traffic light
 * 
 * @author byeo
 * 
 */
public class TrafficLightFSM extends StateMachine<TrafficLightFSM.s,TrafficLightFSM.e> {

	private static final class TrafficLightSequence extends StateMachinePlan<s, e> {
		{
			/**
			 * <pre>
			 * This is what the project is all about.
			 * A state transition matrix in a grid.
			 * 
			 * I want be able to see a 10x10 state transition matrix on one screen.
			 * </pre>
			 */
			// @formatter:off
			ri(null      , e.PushButn, e.EndBeeps, e.WaitTime);
			at(s.Red     , null      , s.RedAmber, null      );
			at(s.RedAmber, null      , null      , s.Green   );
			at(s.Green   , s.Amber   , null      , null      );
			at(s.Amber   , null      , null      , s.Red     );
			// @formatter:on
		}
	}

	/**
	 * States
	 */
	public enum s {
		Red, RedAmber, Green, Amber
	}

	/**
	 * Events
	 */
	public enum e {
		PushButn, EndBeeps, WaitTime
	};

	/**
	 * Static so that it's only constructed once on class load
	 * You obviously shouldn't store individual state in the plan.
	 * Your FSM will be passed into the transition Actions
	 */
	private static final StateMachinePlan<s,e> sPLAN = new TrafficLightSequence();

	Action<s,e> startBeepTimer = new Action<s,e>() {
		public void apply(StateMachine<s,e> fsm, e event,s nextState) {
		}
	};
	
	Action<s,e> startWaitTimer = new Action<s,e>() {
		public void apply(StateMachine<s,e> fsm, e event, s nextState) {
		}
	};

	public TrafficLightFSM(s aStartState) {
		super(sPLAN, aStartState);
		
		entryAction(s.Red, startBeepTimer);
		entryAction(s.RedAmber, startWaitTimer);
		entryAction(s.Amber, startWaitTimer);	
	}
}
