package j2me.regex;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



public class Determinizer {

	private NFSAutomata nfsa;
	
	private Hashtable table;
	
	private DFAAutomata dfsa;
	
	protected Determinizer( NFSAutomata nfsa ) {
		this.nfsa = nfsa;
	}

	private void makeTable() {
		table = new Hashtable() ;
		
		Enumeration e , ei;
		State s, si;
		
		// 1o. Montagem das tabelas
		Hashtable states = nfsa.getStates();
		Vector group;
		e = states.keys();

		while ( e.hasMoreElements() ) { // Para cada S em M
			s = (State ) e.nextElement();
			group =new Vector();
			group.addElement( s );
			table.put(s, group);
			
			if (nfsa.getFreeTransitions(s) != null)
			{
				ei = nfsa.getFreeTransitions(s).elements();
		
				while (ei.hasMoreElements()) { // Para cada estado Si alcançavel com transicao vazio a partir de S
					si =( State ) ei.nextElement() ;
					((Vector)table.get( s )).addElement( si );
				}
			}
		}
	}
	
	private void makeUnion( State a ) {
		// Procura possiveis unioes
		Vector group = (Vector) table.get( a );
		if (group == null)
			return;
		State s, skey;
		for (int i=0;i<group.size();i++) {
			s = (State)group.elementAt(i);
			skey = searchKey( s );
			if (skey == a)
				continue;
			if (skey!=null) {
				union( a, skey );
			}
		}
	}
	
	// Procura o estado a em outro grupo.
	private State searchKey( State a ) {
		Vector group;
		Enumeration e;
		State s;
		e = table.keys();
		while (e.hasMoreElements()) {
			s = (State) e.nextElement();
			if (s == a)
				continue;
			group = (Vector) table.get(s);
			if (group.contains(a)) {
				return s;
			}
		}
		return null;
	}
	
	private void union( State a, State b ) {
		Enumeration e;
		Vector groupA, groupB;
		
		groupA = (Vector) table.get(a);
		groupB = (Vector) table.get(b);
		
		State s;
		e=groupB.elements();
		while (e.hasMoreElements()) {
			s = (State) e.nextElement();
			if (!groupA.contains(s))
				groupA.addElement(s);
		}
		
		table.put(a, groupA);
		table.remove(b);
	}
	
	private void makeAutomata() {
		Hashtable referencetable = new Hashtable();
		dfsa = new DFAAutomata();
		Enumeration e, et;
		e = table.keys();

		State newState;
		State key, s, dest;
		Vector states;
		
		// Cria a tabela de referencia
		boolean isFinal = false, isInitial = false;
		while (e.hasMoreElements()) {
			key = (State) e.nextElement();
			states = (Vector) table.get( key );
			
			newState = new State();
			
			referencetable.put ( newState, states );
			
			isFinal= containsFinalstate( states );
			isInitial= containsInitialstate( states );
			
			if (!isInitial)
				dfsa.addState( newState, isFinal );
			else
				dfsa.setInitialstate( newState );
		}
		
		Character symbol;
		
		// Inicia o processo de criação do automato
		e = table.keys();
		while (e.hasMoreElements()) {
			key = (State) e.nextElement();
			states = (Vector) table.get( key );
			
			// Pego estado e verifica suas transicoes
			// para produzir novas transicoes
			for ( int i=0; i<states.size(); i++ ) {
				s = (State) states.elementAt(i);
				
				et = s.getTransitions().keys();
				while ( et.hasMoreElements() ) {
					symbol = (Character) et.nextElement();
					dest = (State)s.getTransitions().get(symbol);
					
					// Pega o estado de referencia do novo automato
					State ori = getReferenceState(referencetable, s);
					State ref = getReferenceState(referencetable, dest);
					ori.addTransition( symbol.charValue(), ref );
					
					if (nfsa.isInitialstate(s)) {
						dfsa.setInitialstate(ori);
					}
					if (nfsa.isFinalstate(s)) {
						dfsa.updateState(ori,true);
					}
				}
			}
		}
	}
	
	private boolean containsFinalstate( Vector states ) {
		for ( int i=0; i<states.size(); i++) {
			if (nfsa.isFinalstate((State) states.elementAt(i)))
				return true;
		}
		return false;
	}
	
	private boolean containsInitialstate( Vector states ) {
		for ( int i=0; i<states.size(); i++) {
			if (nfsa.isInitialstate((State) states.elementAt(i)))
				return true;
		}
		return false;
	}
	
	private State getReferenceState( Hashtable referencetable,  State a ) {
		//	Procura 
		Enumeration e = referencetable.keys();
		State state;
		Vector group;
		
		while ( e.hasMoreElements() ) {
			state = (State)e.nextElement();
			group = (Vector) referencetable.get( state );
			
			if (group.contains(a))
				return state;
		}
		
		return null;
	}
	
//	private void eliminator() {
//		Enumeration e, ei;
//		Vector g;
//		Vector readed = new Vector();
//		Vector candidates = new Vector();
//		State s, si;
//		boolean candidate = false;
//		
//		e = table.keys();
//		while (e.hasMoreElements()) {
//			s = (State) e.nextElement();
//			
//			ei = table.keys();
//			while (ei.hasMoreElements()) {
//				si = (State) ei.nextElement();
//				if(si==s) continue;
//				g = (Vector) table.get(si);
//				
//				candidate = false;
//			//	System.out.println(s + " cmp "+ si + " g: " + g.toString());
//				for ( int i=0; i<g.size(); i++) {
//					if (g.contains(s))
//					{
//						candidate = true;
//					//	readed.addElement(g.elementAt(i));
//					}
//				}
//				
//				if(candidate) {
//				//	System.out.println(s + " é candidado!");
//					candidates.add(s);
//					table.remove(s);
//				}
//			}
//		}
//	}
	
	public DFAAutomata determinize() {
		// 1o. passo, montar a tabela de transicoes em vazio
		// 2o. verificar possiveis unioes
		// 3o. eliminar os já agrupados
		// 4o. conveter para automato e criar as transicoes
		
//		Modelo 1
		
//		table = new Hashtable() ;
//
//		Enumeration e = nfsa.getStates().keys();
//		while ( e.hasMoreElements() ) {
//			makeTableRecursive( (State) e.nextElement() );
//		}
//		
//		State s;
//		e = table.keys();
//		while (e.hasMoreElements()) {
//			s = (State) e.nextElement();
//			if (findSuper( s ))	table.remove( s ); else makeUnion( s );
//		}
//		
//		makeAutomata();
		
		// MODELO 2
		Enumeration e;  State s;

		System.out.println("-----+ NFSA: " + nfsa.getStates().size());
		
		makeTable () ;

		e = table.keys();
		
		while (e.hasMoreElements()) {
			s = (State) e.nextElement() ;
			makeUnion ( s ) ;
		}
		
		e = table.keys();

		while (e.hasMoreElements()) {
			s = (State) e.nextElement() ;
			makeUnion ( s ) ;
		}
		
		makeAutomata();
		
		System.out.println("-----+ DFSA: " + dfsa.getStates().size());
		System.out.println("-----+ Redução: " + (100 - (dfsa.getStates().size()*100)/nfsa.getStates().size()) + "%");
		
		//System.out.println(dfsa);
		
		return dfsa;
	}
	
//	public boolean findSuper( State state ) {
//		Enumeration e;
//		State s;
//		Vector v, vstate;
//		e = table.keys();
//		vstate = (Vector) table.get(state);
//		boolean find = false;
//	
//		while (e.hasMoreElements()) {
//			s = (State) e.nextElement();
//			v = (Vector) table.get( s );
//			
//			if (state == s)
//				continue;
//			
//			find = true;
//			for ( int i=0; i<vstate.size();i++ ) {
//				if(!v.contains(vstate.elementAt(i))) {
//					find = false;
//				}
//			}
//			if (find == true) {
//				return true;
//			}
//		}
//		
//		return find;
//	}
//	
//	public Vector makeTableRecursive( State s ) {
//		State a;
//		Vector free = nfsa.getFreeTransitions( s );
//		Vector group = new Vector();
//		Vector subgroup;
//		
//		group.addElement(s);
//	
//		if (free == null) {
//			table.put(s, group);
//			return group;
//		}
//		
//		Enumeration e = free.elements();
//		while (e.hasMoreElements() ) {
//			a = (State ) e.nextElement();
//			if (!group.contains(a))
//			group.addElement(a);
//			
//			subgroup = makeTableRecursive( a );
//			for (int i=0; i<subgroup.size(); i++) {
//				if (!group.contains((State) subgroup.elementAt(i)))
//					group.addElement((State) subgroup.elementAt(i));
//			}
//		}
//		
//		table.put(s, group);
//		return group;
//	}
}
