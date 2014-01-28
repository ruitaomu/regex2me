package j2me.regex;



/**
 * DFA, Deterministic Finite Automata.
 * 
 * Automato finito determinístico.
 * 
 * @author Thiago Galves Moretto <thiago@moretto.eng.br>
 * @since 12/09/2006
 * @version 0.1b
 * @see lfa.fsa#Automata
 */
public class DFAAutomata extends Automata {

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
		from.addTransition(symbol, to);
	}
}
