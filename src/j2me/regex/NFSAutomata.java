package j2me.regex;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NFSAutomata extends Automata {

	private Hashtable freeTransitions;
	
	/**
	 * Adiciona uma transição comum.
	 * 
	 * @param symbol Simbolo do alfabeto.
	 * @param from Estado origem.
	 * @param to Estado destino.
	 */
	public void addTransition( char symbol, State from, State to ) {
		if (!containsState(from))
			addState(from, false);
		if (!containsState(to))
			addState(to, false);
		from.addTransition(symbol, to);
	}
	
	public void setFreeTransitions( State from, Vector fr ) {
		if (fr == null)
			return;
		if (freeTransitions == null)
			freeTransitions = new Hashtable();
		freeTransitions.put(from, fr); 
	}
	
	public Hashtable getFreeTransitions( ) {
		return freeTransitions;
	}
	
	public void addFreeTransition ( State from, State to ) {
		if (!containsState(from))
			addState(from, false);
		if (!containsState(to))
			addState(to, false);
		if (freeTransitions==null)
			freeTransitions=new Hashtable();
		if (freeTransitions.containsKey(from)) {
			Vector dest = (Vector)freeTransitions.get(from);
			dest.addElement(to);
			freeTransitions.put(from, dest);
		} else {
			Vector dest = new Vector();
			dest.addElement(to);
			freeTransitions.put(from, dest);
		}
	}
	
	public Vector getFreeTransitions( State from ) {
		if(freeTransitions==null)
			return null;
		return (Vector) freeTransitions.get(from);
	}
	
	public boolean hasFreeTransition( State from, State to ) {
		if (freeTransitions==null)
			return false;
		Vector dest = (Vector) freeTransitions.get(from);
		if(dest!=null)
			return dest.contains(to);
		return false;
	}

	public NFSAutomata or( NFSAutomata automata ) {
		State initialstate = new State();
		State finalstate = new State();
		
		State temp = getInitialstate();		// ex-estado inicial.
		setInitialstate(initialstate);		// define o novo e.i.
		addState(temp); 					// adiciona o ex-e.i.
		
		addFreeTransition(initialstate, temp);
		addFreeTransition(initialstate, automata.getInitialstate());
		
		// adiciona os estado do outro automato a este.
		Enumeration e = automata.enumerateStates();
		State state;
		while (e.hasMoreElements()) {
			state = (State)e.nextElement();
			addState( state );
			setFreeTransitions(state, automata.getFreeTransitions(state));
			if (automata.isFinalstate(state)) {
				// Adiciona uma transicao em vazio do estado final para o novo final.
				addFreeTransition(state, finalstate);
			}
		}
		
		// Atualiza os estados finais.
		e = enumerateStates();
		while (e.hasMoreElements()) {
			state = (State)e.nextElement();
			if (isFinalstate(state)) {
				// Adiciona uma transicao em vazio do estado final para o novo final.
				addFreeTransition(state, finalstate);
			}
			// Deixa de ser final.
			updateState(state, false);
		}
		
		if (automata.isFinalstate(automata.getInitialstate()))
			updateState(automata.getInitialstate(), true);
		
		// Adiciona o estado final.
		addFinalstate(finalstate);
		updateState(finalstate, true);

		return this;
	}
	
	public NFSAutomata cat( NFSAutomata automata ) {
		State state;
		
		// Atualiza os estados atuais finais para não-finais.
		Enumeration e = enumerateStates();
		while (e.hasMoreElements()) {
			state = (State) e.nextElement();
			if (isFinalstate(state)) {
				updateState(state, false); // Não é mais final.
				addFreeTransition(state, automata.getInitialstate());
			}
		}

		// Adiciona todos os outros estados do outro automato mantendo os finais.
		Enumeration et = automata.enumerateStates();
		while (et.hasMoreElements()) {
			state = (State) et.nextElement();
			addState(state, automata.isFinalstate(state));
			setFreeTransitions(state, automata.getFreeTransitions(state));
		}

		if (automata.isFinalstate(automata.getInitialstate()))
			updateState(automata.getInitialstate(), true);
				
		return this;
	}
	
	public void kleene( ) {
		State state;
		Enumeration e = enumerateStates();
		while (e.hasMoreElements()) {
			state = (State) e.nextElement();
			if (isFinalstate(state)) {
				addFreeTransition(state, getInitialstate()); // Aponta para o inicial
			}
		}
		
		updateState(getInitialstate(), true);
	}
	
	public void optional() {
		updateState(getInitialstate(), true);
	}
}
