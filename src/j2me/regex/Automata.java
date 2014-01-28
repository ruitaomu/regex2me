package j2me.regex;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Definição de um automato.
 * 
 * @author Thiago Moretto.
 * @version 1.0
 */
public class Automata {
	/**
	 * Indexador de estados.
	 */
	private short index = 2;
	
	/**
	 * Hash das configurações dos estados dentro
	 * do automato.
	 */
	private Hashtable states;
	
	/**
	 * Indexes
	 * 
	 */
	private Hashtable indexes;
	
	/**
	 * Configuração de um estado no automato.
	 * 
	 * @author Thiago Moretto.
	 * @version 1.0
	 */
	class Configuration {
		
		private boolean finalstate = false;
		
		public Configuration( boolean finalstate ) {
			this.finalstate = finalstate;
		}
		
		public boolean isFinalstate() {
			return finalstate;
		}
		public void setFinalstate(boolean finalstate) {
			this.finalstate = finalstate;
		}
	}
	
	/**
	 * Estado inicial.
	 */
	private State initialstate;
	
	/**
	 * Construtor padrão.
	 *
	 */
	protected Automata() {
		states = new Hashtable();
		// Crio um estado inicial, afinal, um automato precisa
		// no mínimo isto.
		initialstate = new State();
		initialstate.setIndex((short)1);
		states.put(initialstate, new Boolean(false));
		indexes = new Hashtable();
		index( initialstate );
	}
	
	/**
	 * Indexador
	 * 
	 */
	private void index( State state ) {
		indexes.put(new Integer(state.getIndex()), state);
	}
	
	/**
	 * Genérico.
	 * 
	 * @param state Estado.
	 * @param initialstate true se ele é inicial, false caso contrário.
	 * @param finalstate false se ele é final, false caso contrário.
	 */
	public void addState(State state, boolean finalstate ) {
		if(!states.containsKey(state)) {
			state.setIndex(index++);
			states.put(state, new Boolean(finalstate));
			index(state);
		}
	}
	
	/**
	 * Adiciona um estado.
	 * 
	 */
	public void addState(State state ) {
		addState(state, false);
	}
	
	/**
	 * Especializado.
	 * 
	 * @param state Estado final.
	 */
	public void addFinalstate(State state) {
		addState(state, true);
	}
	
	/**
	 * Retona o estado inicial do automato.
	 * 
	 * @return o estado (State) inicial.
	 */
	public State getInitialstate() {
		return initialstate;
	}
	
	/**
	 * Pergunta ao automato se este estado pertence ao automato.
	 * 
	 */
	public boolean containsState(State state) {
		return states.containsKey(state);
	}
	
	/**
	 * Enumera os estados.
	 * 
	 * @return objeto Enumeration dos estados pertencentes a este automato.
	 */
	public Enumeration enumerateStates() {
		return states.keys();
	}
	
	/**
	 * Get states
	 * 
	 */
	public Hashtable getStates() {
		return states;
	}
	
	public void setFinalstate(State state) {
		if (containsState(state)){
			states.put(state, new Boolean(true));
		}
	}
	
	public void setInitialstate(State state) {
		states.remove(initialstate);
		initialstate = state;
		initialstate.setIndex((short)1);
		index(initialstate);
		if(!states.containsKey(initialstate))
			states.put(initialstate,new Boolean(false));
	}
	
	public boolean isFinalstate(State state) {
		if (!containsState(state)) return false;
		return ((Boolean)states.get(state)).booleanValue();
	}
	
	public boolean isInitialstate(State state) {
		return state == initialstate;
	}
	
	public void updateState( State state, boolean finalstate ) {
		if(states.containsKey(state)) {
			states.put(state, new Boolean(finalstate));
		}
	}
	
	public State getStateByIndex( int index ) {
		return (State) indexes.get(new Integer(index));
	}
	
	/**
	 * Override
	 */
	public String toString() 
	{
		StringBuffer buffer = new StringBuffer();
	
		buffer.append( "Automata("+super.toString() + ")\n" );
		
		State state;
		Hashtable states = getStates();
		Enumeration e = states.keys();
		Enumeration et;
		while (e.hasMoreElements()) {
			state = (State) e.nextElement();
			
			buffer.append( " state("+state+")" );
			
			if (isFinalstate(state)) 
				buffer.append( " final(true) ");
			if (state.equals(getInitialstate()))
				buffer.append( " initial(true) ");
			
			buffer.append( "\n" ) ;
			
			Hashtable tr = state.getTransitions(); 
			et = tr.keys();
			
			Character symbol;
			while (et.hasMoreElements()) {
				symbol = (Character ) et.nextElement();
				buffer.append("   f("+symbol+" >> "+tr.get(symbol)+")\n");
			}
			
			if (this instanceof NFSAutomata){
				NFSAutomata nfsa = (NFSAutomata)this;
				Vector dest = nfsa.getFreeTransitions(state);
				if (dest!=null) {
					if(dest.size()==0)
						System.out.println("vetor vazio para "+state);
					Enumeration fr = dest.elements();
					State freeDest;
					while (fr.hasMoreElements()){
						freeDest=(State)fr.nextElement();
						buffer.append("   free("+freeDest+")\n");
					}
				} else {
					System.out.println("vetor nulo para "+state);
				}
			}
		}
		
		return buffer.toString();
	}
}
