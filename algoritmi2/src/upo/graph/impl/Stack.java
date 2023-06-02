package upo.graph.impl;

import java.util.ArrayDeque;

@SuppressWarnings("serial")
public class Stack<E> extends ArrayDeque<E> {
	private final ArrayDeque<E> stack = new ArrayDeque<>();

	@Override
	public boolean add(E e) {
		stack.addLast(e);
		return true;
	}

	@Override
	public E peek(){
		return stack.peekLast();
	}

	@Override
	public E poll() {
		return stack.pollLast();
	}
}
