package lfa.test;

public class Tester {

	
	private TestCase testCase;
	
	public Tester( TestCase testCase ) {
		this.testCase = testCase;
	}
	
	public void perform() {
		long start, end;
		start = System.currentTimeMillis();
		testCase.test();
		end = System.currentTimeMillis();
		System.out.println(testCase.getClass() + " time: " + (end-start) + "ms");
	}
	
	public void generate() {
		//System.out.println("succeded tests: " + testCase.getSuccededTests() );
		System.err.println(testCase.getClass() + " failed tests: " + testCase.getFailedTests() );
		System.out.println(testCase.getClass() + " total tests: " + testCase.getSuccededTests());
	}
}
