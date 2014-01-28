package lfa.test;

import j2me.regex.Matcher;
import j2me.regex.Pattern;

class Test002 extends TestCase {
	public Test002() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}

	public void test() {
		Pattern pattern = new Pattern("a?b");
		Matcher m = pattern.matcher();

		assertTrue(m.match("b"));
//		assertTrue(m.match("bcab"));
//		assertTrue(m.match("abcahudhaiusda"));
//		
//		assertFalse(m.match("abc"));
//		assertFalse(m.match("ab"));
//		assertFalse(m.match("abacaxi"));
	}
}


class Test003 extends TestCase
{
	public Test003() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("Thiago (Galves ?)Moretto");
		Matcher m = pattern.matcher();

		assertTrue(m.match("Thiago Moretto"));
		assertTrue(m.match("Thiago Galves Moretto"));
		
		assertFalse(m.match("Thiago Moretto Galves"));
		assertFalse(m.match("Thiago Galv Moretto"));
	}
}

class Test004 extends TestCase
{
	public Test004() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("Mari((o|a)?)");
		Matcher m = pattern.matcher();

		assertTrue(m.match("Maria"));
		assertTrue(m.match("Maria"));
		assertTrue(m.match("Mari"));
		
		assertFalse(m.match("Marib"));
		assertFalse(m.match("Mariana"));
		assertFalse(m.match("Jose"));
		assertFalse(m.match("oa"));
	}
}

class Test005 extends TestCase
{
	public Test005() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("Al pacin((o|a)*?)");
		Matcher m = pattern.matcher();

		assertTrue(m.match("Al pacino"));
		assertTrue(m.match("Al pacino"));
		assertTrue(m.match("Al pacinoooooo"));
		assertTrue(m.match("Al pacinaaaaaa"));
		assertTrue(m.match("Al pacinooaaooa"));
		assertTrue(m.match("Al pacin"));
		
		assertFalse(m.match("Al pacinooaabba"));
		assertFalse(m.match("Al pacinooaacca"));
		assertFalse(m.match("pacinooaaeaooa"));
	}
}

class Test006 extends TestCase
{
	public Test006() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("A(l|u)( ?)(((pacin(o|a)( ?))*)?)");
		Matcher m = pattern.matcher();

		assertTrue(m.match("Au pacino"));
		assertTrue(m.match("Au "));
		assertTrue(m.match("Au"));
		assertTrue(m.match("Au pacina"));
		assertTrue(m.match("Al pacino pacina pacino"));
		assertTrue(m.match("Al"));
		
		assertFalse(m.match("Ay pacinooaabba"));
		assertFalse(m.match("Al pacinooaacca"));
		assertFalse(m.match("pacinooaaeaooa"));
	}
}

class Test007 extends TestCase
{
	public Test007() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[0-9][0-9][a-b]");
		Matcher m = pattern.matcher();

		assertTrue(m.match("35a"));
	}
}

class Test008 extends TestCase
{
	public Test008() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[0-9]([0-9]*)\\+[0-9]([0-9]*)=[0-9]([0-9]*)");
		Matcher m = pattern.matcher();

		assertTrue(m.match("758+231=312"));
		assertTrue(m.match("1+231=232"));
		assertTrue(m.match("794794564665+5453465456454546546546546546546465546546546546546=0"));

		assertFalse(m.match("794794564665+5453465456454546546546546546546465546546546546546="));
		assertFalse(m.match("794794564665*5453465456454546546546546546546465546546546546546=13123"));
	}
}

class Test009 extends TestCase
{
	public Test009() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[0-4]+");
		Matcher m = pattern.matcher();

		assertTrue(m.match("3331212"));
		
		assertFalse(m.match("312124499"));
		assertFalse(m.match(""));
		assertFalse(m.match("6664323"));
		assertFalse(m.match("6553221"));
		assertFalse(m.match("8"));
	}
}

class Test010 extends TestCase
{
	public Test010() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[7-9][8-9]");
		Matcher m = pattern.matcher();

		assertTrue(m.match("78"));
		assertTrue(m.match("99"));
		assertTrue(m.match("88"));
		assertTrue(m.match("89"));
		
		assertFalse(m.match("52"));
		assertFalse(m.match("75"));
		assertFalse(m.match("1"));
		assertFalse(m.match("19"));
		assertFalse(m.match("12"));
	}
}

class Test011 extends TestCase
{
	public Test011() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		long start, end;
		start = System.currentTimeMillis();
		Pattern pattern = new Pattern("( *?)([0-9]+)( *?)(\\+|\\*|/|-)( *?)([0-9]+)( *?)=( *?)([0-9]+)( *?)");
		Matcher m = pattern.matcher();
		end = System.currentTimeMillis();
		System.out.println("Tempo: " + (end-start) + "ms");
		
//		long start, end;
//		start = System.currentTimeMillis();
//		java.util.regex.Pattern pa = java.util.regex.Pattern.compile("( *?)([0-9]+)( *?)(\\+|\\*|/|-)( *?)([0-9]+)( *?)=( *?)([0-9]+)( *?)");
//		java.util.regex.Matcher ma = pa.matcher("758   + 231 = 89993");
//		end = System.currentTimeMillis();
//		System.out.println("J2SE REGEX: " + ma.matches());
//		System.out.println("Tempo: " + (end-start) + "ms");
		
		assertTrue(m.match("758   + 231 = 89993"));
		assertTrue(m.match("758+231   = 89993"));
		assertTrue(m.match("758   * 231 = 89993"));
		assertTrue(m.match(" 758   - 231   = 89993  "));
		assertTrue(m.match("758   + 231   = 89993"));
		assertTrue(m.match("  758   / 231              = 89993      "));
		
		assertFalse(m.match("  758   & 231              = 89993      "));
		assertFalse(m.match("  758   + 231              =       "));
		assertFalse(m.match("    + 231              =     "));
		assertFalse(m.match("  758   + 231              =       "));
		assertFalse(m.match("  758   +   =       "));
		assertFalse(m.match("  758   +   1212 ! 2132      "));
	}
}

class Test012 extends TestCase
{
	public Test012() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[0-9]{10}");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("1234567890"));
		assertTrue(m.match("9656996442"));
		
		assertFalse(m.match("99999999"));
		assertFalse(m.match("abcdedasd0"));
		assertFalse(m.match("11"));
		assertFalse(m.match("43"));
		assertFalse(m.match("742"));
		assertFalse(m.match("7"));
	}
}

class Test013 extends TestCase
{
	public Test013() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[a-z]([A-z]{3})([a-z]{2})");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("bACDad"));
		assertTrue(m.match("bADDad"));
		assertTrue(m.match("zDDDad"));
		assertTrue(m.match("fZZZad"));
		
		assertFalse(m.match("aDDdDDa"));
		assertFalse(m.match("aDDda"));
		assertFalse(m.match("AAAAA"));
		assertFalse(m.match("AAAAA"));
		assertFalse(m.match("AAAAA"));
		assertFalse(m.match("0ZZZad")); // 0??
		assertFalse(m.match("bACDadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
	}
}

class Test014 extends TestCase
{
	public Test014() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("mari(a|o)");
		Matcher m = pattern.matcher(Matcher.MATCH_SEARCH);
		
		assertTrue(m.match("maria È mais esperta que mario"));
		assertTrue(m.match("mari È mais jovem que maria"));
		assertTrue(m.match("mario È carpiteniro"));
		assertTrue(m.match("josÈ È t„o esperto quanto mario e seus amigos"));
		
		assertFalse(m.match("aqui nao tem ninugme"));
		assertFalse(m.match("nao deve aceitar"));
		assertFalse(m.match("mari, isso n„o"));
	}
}

class Test015 extends TestCase
{
	public Test015() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("[a-z]+");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("a"));
		assertTrue(m.match("abcdef"));
		assertTrue(m.match("abacaxi"));
		assertTrue(m.match("dasdjasahiu"));
		assertTrue(m.match("daskdajsiodjasiodjasiodjoajdojasoda"));
		assertTrue(m.match("daskdajsiodjasiodjasiodjoajdojasodadaskdajsiodjasiodjasiodjoajdojasoda"));
		assertTrue(m.match("abacaxi"));
		
		assertFalse(m.match("")); // N„o aceita cadeia vazia
		assertFalse(m.match("381290312830123801")); // simbolos desocnhecidos
		assertFalse(m.match("381290312830123801")); // simbolos desocnhecidos
		assertFalse(m.match(";·sda][[p12]312]3")); // simbolos desocnhecidos
		assertFalse(m.match("·······sda3232131")); 
		assertFalse(m.match("aaaaaaaaaaaabcsdas.")); // simbolos desocnhecidos
	}
}

class Test016 extends TestCase
{
	public Test016() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("thiago( moretto)+");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("thiago moretto moretto"));
		
		assertFalse(m.match("thiago"));
		assertFalse(m.match("moretto thiago")); // N„o mesmo
	}
}

class Test017 extends TestCase
{
	public Test017() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("( *?)SELECT( *?)((\\*)|[a-z]+)( *?)FROM( *?)[a-z]+( *?)((WHERE)?)( *?)");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("SELECT    * FROM tabela   "));
		assertTrue(m.match("  SELECT id FROM tabela WHERE"));
		assertTrue(m.match("SELECT    id FROM    tabela    WHERE"));
		
		assertFalse(m.match("SELECT FROM tabela WHERE"));
		assertFalse(m.match("SELECT * FROM WHERE"));
	}
}


class Test018 extends TestCase
{
	public Test018() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}
	
	public void test() {
		Pattern pattern = new Pattern("([0-9]{3})([0-9]?)-([0-9]{4})");
		Matcher m = pattern.matcher();
		
		assertTrue(m.match("3363-2749"));
		assertTrue(m.match("363-2749"));
		assertTrue(m.match("241-2649"));
		
		assertFalse(m.match("53-2649"));
		assertFalse(m.match("241-649"));
	}
}
public class Test001 extends TestCase {
	
	public static void main(String[] args) {
//		new Test001();
//		new Test002();
//		new Test003();
//		new Test004();
//		new Test005();
//		new Test006();
//		new Test007();
		new Test008();
//		new Test009();
		new Test010();
		new Test011();
		new Test012();
		new Test013();
		new Test014();
//		new Test015();
//		new Test016();
		new Test017();
		new Test018();
	}

	public Test001() {
		Tester t = new Tester(this);
		t.perform();
		t.generate();
	}

	public void test() {
		// TODO Auto-generated method stub
		Pattern pattern = new Pattern("(a|c)d");
		
		long start = System.currentTimeMillis();
		Matcher m = pattern.matcher();
		long end = System.currentTimeMillis();
		
		System.out.println("Tempo para criar o reconhecedor: " + (end-start) + "ms");

		// Deve aceitar
		assertTrue(m.match( "ad" ));
		assertTrue(m.match( "cd" ));
		assertFalse(m.match("d"));
		assertFalse(m.match("acd"));
//		assertTrue(m.match( "mario" ));
//		assertTrue(m.match( "maric" ));
//		assertTrue(m.match( "marid" ));
//		assertTrue(m.match( "marij" ));
//		assertTrue(m.match( "marik" ));
//		assertTrue(m.match( "marie" ));
//		assertTrue(m.match( "maril" ));
//		assertTrue(m.match( "mariq" ));
//		
//		// N„o deve aceitar
//		assertFalse(m.match( "dasdasdhdauis" ));
//		assertFalse(m.match( "mariglpokp" ));
//		assertFalse(m.match( "mar" ));
//		assertFalse(m.match( "mariaaaaaa" ));
//		assertFalse(m.match( "marioapa" ));
//		assertFalse(m.match( "marii" ));
//		assertFalse(m.match( "maraai" ));
//		assertFalse(m.match( "mariao" ));
//		assertFalse(m.match( "mariaa" ));
//		assertFalse(m.match( "maari" ));
//		assertFalse(m.match( "aia" ));
//		assertFalse(m.match( "ariam" ));
	}
}