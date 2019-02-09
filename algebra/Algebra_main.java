package algebra;

import java.util.Scanner;

public class Algebra_main {

	public static void main(String[] args) {
		Testing.runAllTests();
		System.out.println("Enter equation");
		Scanner keyboard = new Scanner(System.in);
		String toEvaluate = keyboard.nextLine();

		Equation test = new Equation(toEvaluate);
		System.out.println("parsed equation: ");
		test.printEquation();

		System.out.println("x = " + test.solveEquation());
	
	}
}
