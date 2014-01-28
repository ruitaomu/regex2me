package j2me.regex;

import java.util.Enumeration;
import java.util.Hashtable;

public class Matcher {

	public static final int MATCH_ALL 		= 1;
	
	public static final int MATCH_SEARCH 	= 2;
	
	private String input;
	
	private DFAAutomata fsa;
	
	private int[][] transitionTable;
	
	private int matchMode = MATCH_ALL;
	
	protected Matcher( DFAAutomata fsa) {
		this.fsa = fsa;
	}
	
	protected Matcher( DFAAutomata fsa, int matchMode ) {
		this.fsa = fsa;
		this.matchMode = matchMode;
	}
	
	private void makeTransitionTable() {
		Hashtable transitions;
		Hashtable states = fsa.getStates();
		transitionTable = new int[states.size()+3][256];
		
		Enumeration et;
		Enumeration e = states.keys();
		
		Character symbol;
		State s, next;
		while (e.hasMoreElements()) {
			s = (State) e.nextElement();
			
			transitions = s.getTransitions();
			
			et = transitions.keys();
			
			while (et.hasMoreElements()) {
				symbol = (Character) et.nextElement();
				next = (State) transitions.get( symbol );
				transitionTable[s.getIndex()][symbol.charValue()] = next.getIndex();
			}
		}
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public boolean match( String input ) {
		setInput( input );
		if (matchMode == MATCH_ALL) {
			makeTransitionTable();
			return matchByTransitionTable();
		} else {
			return match();
		}
	}
	
	public DFAAutomata getDFA() {
		return fsa;
	}
	
	private boolean matchByTransitionTable() {
		int state;
		char symbol;
		state = fsa.getInitialstate().getIndex();
		for ( int i=0; i<input.length(); i++ ) {
			symbol = input.charAt( i );
			state = transitionTable[state][(int)symbol];
		}
		State endstate = fsa.getStateByIndex(state);
		return (state != 0 && fsa.isFinalstate(endstate));
	}
	
	private boolean match() {
		Character symbol;
		State state = fsa.getInitialstate();
		
		for ( int i=0; i<input.length(); i++ ) {
			symbol = new Character(input.charAt(i));
			state = state.next(symbol);
			if (state == null) {
				state = fsa.getInitialstate(); // Retorna
			} else {
				if (fsa.isFinalstate(state)) // Encontrou?
					return true;
			}
		}
		
		return (state!= null && fsa.isFinalstate(state));
	}
}
