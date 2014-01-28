package j2me.regex;


public class Transition 
{
	private char input;
	
	private State to;
	
	protected Transition ( char input, State to ) {
		this.input = input;
		this.to = to;
	}
	
	public char getInputSymbol() {			
		return input;
	}
	
	public State getNextState() {
		return to;
	}
	
	public String toString() {
		return "f("+input+","+to+")";
	}
}
