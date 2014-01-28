package lfa.test;

public abstract class TestCase {

	private int succededTests = 0;
	
	private int failedTests = 0;
	
	public void assertTrue( boolean expression ) {
		if (expression == true)
			countSucceded();
		else {
			countFailed();
		}
	}
	
	public void assertFalse( boolean expression ) {
		if (expression == false)
			countSucceded();
		else {
			countFailed();
		}
	}
	
	private void countFailed() {
		failedTests ++;
	}
	
	private void countSucceded() {
		succededTests ++;
	}

	public int getFailedTests() {
		return failedTests;
	}

	public int getSuccededTests() {
		return succededTests;
	}
	
	public abstract void test();
}
