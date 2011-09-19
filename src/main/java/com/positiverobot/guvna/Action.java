package com.positiverobot.guvna;

public interface Action<T> {
	public void apply(T target, Object event, Object futureState);
}
