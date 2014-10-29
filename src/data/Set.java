package data;

import java.util.Stack;

public class Set {
	private Stack<Integer> a;
	private Stack<Integer> b;
	private Stack<Integer> c;
	private int size;
	
	public Set(int size) {
		a = new Stack<Integer>();
		b = new Stack<Integer>();
		c = new Stack<Integer>();
		this.size = size;
		
		for(int i = size; i > 0; i--)
			a.push(i);
	}
	
	public int size() {
		return size;
	}
	
	public boolean isComplete() {
		return a.empty() && b.empty() && !c.empty();
	}
	
	public int[] getPegArray(char s) {
		Stack<Integer> peg = getSFC(s);
		Object[] diskObjects = peg.toArray();
		int[] toReturn = new int[diskObjects.length];
		for(int i = 0; i < diskObjects.length; i++)
			toReturn[i] = ((Integer)diskObjects[i]);
		return toReturn;
	}
	
	private Stack<Integer> getSFC(char s) {
		if(s == 'A')
			return a;
		else if(s == 'B')
			return b;
		else if(s == 'C')
			return c;
		return null;
	}
	
	public boolean empty(char s) {
		return getSFC(s).empty();
	}
	
	public int pop(char s) {
		return getSFC(s).pop();
	}
	
	public int peek(char s) {
		return getSFC(s).peek();
	}
	
	public void push(char s, int v) {
		getSFC(s).push(v);
	}
	
	public boolean canPush(char s, int v) {
		return getSFC(s).peek() > v;
	}
	
	public void move(char fromChar, char toChar) {
		getSFC(toChar).push(getSFC(fromChar).pop());
	}
	
	public boolean isValidMove(char fromChar, char toChar) {
		Stack<Integer> fromStack = getSFC(fromChar);
		Stack<Integer> toStack = getSFC(toChar);
		return !fromStack.empty() && (toStack.empty() || fromStack.peek() <= toStack.peek());
	}
	
	@Override
	public String toString() {
		return a.toString() + "\n" + b.toString() + "\n" + c.toString();
	}
}
