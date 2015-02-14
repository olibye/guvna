package com.positiverobot.guvna;

public interface Action<S,E> {
	public void apply(StateMachine<S,E> target, E event, S futureState);
}
