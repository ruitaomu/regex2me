package j2me.regex;

public class Pattern 
{
	/**
	 * Non-deterministic automata
	 */
	private NFSAutomata automata;
	
	private boolean makeExec = false; 
	
	/**
	 * Factory.
	 * 
	 * @param regex A regex pattern.
	 * @return A Pattern object.
	 */
	public static Pattern compile( String regex ) {
		return new Pattern( regex );
	}

	/**
	 * Instance attributes and methods.
	 * 
	 */
	private String regex;
	
	/**
	 * Default and private algorithm.
	 * @param regex
	 */
	public Pattern( String regex ) {
		System.out.println("Expressão: " + regex);
		this.regex = regex; 
	}
	
	/**
	 * Método facade que retorna o objeto matcher.
	 * 
	 */
	public Matcher matcher( ) {
		if (!makeExec)
			make();
		Determinizer d = new Determinizer( automata );
		DFAAutomata fsa = d.determinize();
		Matcher m = new Matcher(fsa, Matcher.MATCH_ALL);
		return m;
	}

	/**
	 * Método facade que retorna o objeto matcher.
	 * 
	 */
	public Matcher matcher( int matchMode ) {
		if (!makeExec)
			make();
		Determinizer d = new Determinizer( automata );
		DFAAutomata fsa = d.determinize();
		Matcher m = new Matcher(fsa,matchMode);
		return m;
	}
	
	private void make() {
		RegularExpression parser = new RegularExpression();
		automata = parser.parse(regex);
	}
}
