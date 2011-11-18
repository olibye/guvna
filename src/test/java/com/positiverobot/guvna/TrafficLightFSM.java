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
public class TrafficLightFSM extends StateMachine {

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

	private static final StateMachinePlan sPLAN = new StateMachinePlan() {
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

			Action<TrafficLightFSM> startBeepTimer = new Action<TrafficLightFSM>() {
				public void apply(TrafficLightFSM fsm, Object event,
						Object nextState) {
				}
			};

			Action<TrafficLightFSM> startWaitTimer = new Action<TrafficLightFSM>() {
				public void apply(TrafficLightFSM fsm, Object event,
						Object nextState) {
				}
			};

			entryAction(s.Red, startBeepTimer);
			entryAction(s.RedAmber, startWaitTimer);
			entryAction(s.Amber, startWaitTimer);

		}
	};

	public TrafficLightFSM(Object aStartState) {
		super(sPLAN, aStartState);
	}

}
