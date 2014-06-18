package net.strong.lang;

public interface Each<T> {

	void invoke(int i, T ele, int length) throws ExitLoop, LoopException;

}
