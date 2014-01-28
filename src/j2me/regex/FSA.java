package j2me.regex;

import java.util.Enumeration;
import java.util.Vector;

public abstract class FSA {

	abstract class Transition {
		State next;
		public Transition(State next ) {
			this.next = next;
		}
		public State getNext() {
			return next;
		}
		public void setNext(State next) {
			this.next = next;
		}
	}
	
	class FreeTransition extends Transition {
		public FreeTransition(State next) {
			super(next);
		}
	}
	
	class SymbolTransition extends Transition  {
		public SymbolTransition(char symbol, State next) {
			super(next);
		}
	}
	
	class State {
		private Vector transitions = new Vector();
		
		public void addTransition( Transition t ) {
			if (!transitions.contains(t))
				transitions.addElement(t);
		}
		
		public Vector getTransitions() {
			return transitions;
		}
	}
	
	public class NFSA extends FSA {
		protected void addTransition(State s, Transition t) {
			if (!contains(s))
				throw new IllegalArgumentException("State not in FSA");
			putTransition(s, t);
		}
	
		public Enumeration enumerateFreeTransitions( State s ) {
			if (!contains(s))
				throw new IllegalArgumentException("State not in FSA");
			Vector free = new Vector();
			
			Enumeration e = enumerateTransitions(s);
			Transition t;
			while (e.hasMoreElements()) {
				t = (Transition) e.nextElement();
				if (t instanceof FreeTransition)
					free.addElement(t);
			}
			return free.elements();
		}
	}

	class DFSA extends FSA {
		protected void addTransition(State s, Transition t) {
			if (t == null)
				throw new IllegalArgumentException("Transition can't be null");
			if (! ( t instanceof SymbolTransition )){
				throw new IllegalArgumentException("Transition class exception");
			} if (!contains(s))
				throw new IllegalArgumentException("State not in FSA");
			putTransition(s, t);
		}
	}

	/**
	 *  Final states
	 *   
	 */
	private Vector f_states = new Vector();
	
	/**
	 * All states (with final's and inicial) 
	 *  
	 */
	private Vector a_states = new Vector();
	
	/**
	 * Initial
	 *
	 */
	private State initial;
	
	public FSA() {
		
	}
	
	public void setInitial( State s ) {
		initial = s;
		put(s);
	}
	
	public void add( State s , boolean isfinal ) {
		if (!contains(s) && isfinal)
			putFinal(s);
		else if(!contains(s))
			put(s);
	}
	
	public void add( State s ) {
		put(s);
	}
	
	private void putFinal( State s ) {
		if(!f_states.contains(s))
			f_states.addElement(s);
		put(s);
	}
	
	private void put( State s ) {
		if(!a_states.contains(s))
			a_states.addElement(s);
	}
	
	private boolean contains( State s ) {
		return a_states.contains(s);
	}

	public State getInitial() {
		return initial;
	}
	
	private void putTransition( State s, Transition t ) {
		s.addTransition( t );
	}
	
	protected abstract void addTransition(State s, Transition t);
	
	public Enumeration enumerateTransitions( State s ) {
		if (!contains(s))
			throw new IllegalArgumentException("State not in FSA");
		return s.getTransitions().elements();
	}
}

