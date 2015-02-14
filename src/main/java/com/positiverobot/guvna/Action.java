package com.positiverobot.guvna;

public interface Action<T,S,E> {
	public void apply(T target, E event, S futureState);
}
