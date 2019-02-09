package algebra;

import java.util.HashMap;

public class Testing {
	public static void runAllTests() {
		HashMap<String, Double> tests = new HashMap<String, Double>();
		tests.put("3+x=12", 9.0);
		tests.put("4*x*(5+3)*2=8*1", 0.125);
		tests.put("3 * 5 = x + 2 + 1 + 4* 1 * 2 + 1", 3.0);
		tests.put("3 * (x + 8) = 30", 2.0);

		testHashMap(tests);
	}
	
	public static void testHashMap(HashMap<String, Double> tests) {
		for (String key : tests.keySet()) {
			Equation equation = new Equation(key);
			double answer = equation.solveEquation();
			
			if (answer != tests.get(key)) {
				System.out.println("Test failed: " + key);
				System.out.println("Answer expected: " + tests.get(key));
				System.out.println("Answer received: " + answer);
				System.exit(-1);
			}
		}
	}
}
