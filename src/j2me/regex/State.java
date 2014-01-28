package j2me.regex;

import java.util.Hashtable;

/**
 * State and your transitions.
 * 
 * @author Thiago Galves Moretto.
 * @version 0.1b
 */
public class State {

	private short index = -1;
	
	private Hashtable transitions = new Hashtable();
	
	protected State() {
		
	}
	
	protected void addTransition(char symbol, State to) { 
		transitions.put(new Character(symbol), to);
	}
	
	protected void setTransitionTable (Hashtable transitions) {
		this.transitions = transitions;
	}
	
	protected Hashtable getTransitions() {
		return transitions;
	}

	protected short getIndex() {
		return index;
	}

	protected void setIndex(short index) {
		this.index = index;
	}
	
	public State next(Character symbol) {
		return (State) transitions.get(symbol);
	}
	
	public String toString() {
		return "{"+index+"}{"+super.toString()+"}";
	}
}
