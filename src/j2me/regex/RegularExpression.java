package j2me.regex;


public class RegularExpression {

	protected RegularExpression( ) {
	}
	
	public NFSAutomata parse( String expression ) {
		NFSAutomata nfsa = null;
		
		int j, level;
		char s;
		boolean expok = false;
		boolean look = false;
		NFSAutomata n;
		
		parser:
		for ( int i=0; i<expression.length() ; i++ ) {
			s = expression.charAt( i );
			
			switch( s ) {
				case '(':
					// Subexpressão.
					j = i;
					level = 0;
					while(true) {
						j++;
						if (j>=expression.length()) break;
						if (expression.charAt(j)=='(') {
							level ++;
						}
						if (expression.charAt(j)==')'){
							if (level == 0) break;
							level --;
						}
					}

					if (level > 0)
						throw new IllegalArgumentException("')' missing.");
					if (level < 0)
						throw new IllegalArgumentException("'(' missing.");
					
					// lookahead
					look = false;

					n = parse(expression.substring(i+1, j));
					
					if(j+1<expression.length() && expression.charAt(j+1) == '*') {
						n.kleene();
						look = true;
						j ++ ;
					} 
					else if(j+1<expression.length() && expression.charAt(j+1) == '?') {
						n= parse("("+ expression.substring(i+1, j)+"?)");
						look = true;
					} else if(j+1<expression.length() && expression.charAt(j+1) == '+') {
						n = parse("(("+expression.substring(i+1, j)+")("+expression.substring(i+1, j)+")*)");
						look = true;
						j ++ ;
					}
					
					if (look) {
						if (nfsa != null)
							nfsa.cat(n);
						else
							nfsa = n;
					} else {
						if (nfsa!=null)
							nfsa.cat(parse(expression.substring(i+1, j)));
						else
							nfsa = parse(expression.substring(i+1, j));
					}
					
					i=j;
					
					break;
			
				case '*': // Kleene*
					if (nfsa == null)
						throw new IllegalArgumentException("* need a symbol before");
					nfsa.kleene();
					break;
				
				case '+': // Kleene+
					if (nfsa == null)
						throw new IllegalArgumentException("+ need a symbol before");
					nfsa.cat(parse("("+expression.substring(0,i)+"*)"));
					break;
					
				case '?': // Opcional
					if (nfsa == null)
						throw new IllegalArgumentException("? need a symbol before");
					nfsa.optional();
					break;
	
				case '\\': // Escape
					i++;
					s = expression.charAt(i);
					if (nfsa == null)
						nfsa = mkSymbol( s );
					else 
						nfsa.cat(mkSymbol( s ));
					break;
				
				case '{': // Reply
					
					j = i;
					expok = false;
					while(true) {
						j++;
						if (j>=expression.length()) break;
						if (expression.charAt(j)=='}'){
							expok = true;
							break;
						}
					}

					if (!expok)
						throw new IllegalArgumentException("'}' missing.");
					
					int reply = parseReply(expression.substring(i+1, j));

					for ( int k=0;k<reply-1; k++) {
						nfsa.cat(parse(expression.substring(0,i)));
					}
					
					i=j;
					
					break;

				case '[': // Range
					
					j = i;
					expok = false;
					while(true) {
						j++;
						if (j>=expression.length()) break;
						if (expression.charAt(j)==']'){
							expok = true;
							break;
						}
					}

					if (!expok)
						throw new IllegalArgumentException("']' missing.");
					
					n = parseRange(expression.substring(i+1, j));
					
					// lookahead
					look = false;

					if(j+1<expression.length() && expression.charAt(j+1) == '*') {
						n.kleene();
						look = true;
						j ++ ;
					} else if(j+1<expression.length() && expression.charAt(j+1) == '?') {
						look = true;
						n.optional();
					} 
					else if(j+1<expression.length() && expression.charAt(j+1) == '+') {
						n = parse("(["+expression.substring(i+1, j)+"]["+expression.substring(i+1, j)+"]*)");
						look = true;
						j ++ ;
					}

					if (look) {
						if (nfsa != null)
							nfsa.cat(n);
						else
							nfsa = n;
					} else {
						if (nfsa!=null)
							nfsa.cat(parseRange(expression.substring(i+1, j)));
						else
							nfsa = parseRange(expression.substring(i+1, j));
					}
					
					i=j;
					
					break;
					
				case '|':
					if (nfsa!=null)
						nfsa.or(parse(expression.substring(i+1)));
					else
						throw new IllegalArgumentException("illegal expression");
					break parser;
					
				default:
					if (nfsa == null)
						nfsa = mkSymbol( s );
					else 
						nfsa.cat(mkSymbol( s ));
			}
		}
		
		return nfsa;
	}
	
	private int parseReply( String expression ) {
		char s;
		String number = "";
		parser:
		for ( int i=0; i<expression.length() ; i++ ) {
			s = expression.charAt(i);
			switch(s) {
				case '}':
					break parser;
				
				default: 
					number += (char) s;
			}
		}
		
		return Integer.parseInt(number);
	}
	
	private NFSAutomata parseRange( String expression )  {
		NFSAutomata nfsa = null;
		char s;
		char start = 0, end = 0;
		boolean not = false;
		for ( int i=0; i<expression.length() ; i++ ) {
			s = expression.charAt(i);
			switch(s) {
				case '^': not= true; break;
				case '-': break;
				default: 
					if(start == 0)
						start = s;
					else
						end = s;
			}
		}
		
		if (start != 0 && end != 0) {
			if (start > end)
				throw new IllegalArgumentException("Illegal range");
			
			if (!not) {
				nfsa = mkSymbol(start);
				for ( int i=start+1;i<=end;i++) {
					nfsa.or(mkSymbol((char)i));
				}
			} else {
				nfsa = mkSymbol(start);
				for ( int i=1;i<=255;i++) {
					if (i<start || i>end)
						nfsa.or(mkSymbol((char)i));
				}	
			}
		} else {
			throw new IllegalArgumentException("Illegal range");
		}
		
		return nfsa;
	}
	
	private NFSAutomata mkSymbol( char symbol ) {
		NFSAutomata res = new NFSAutomata();
		
		State final_state = new State();
		res.addFinalstate(final_state);
		res.addTransition(symbol, res.getInitialstate(), final_state);
		
		return res;
	}
}
