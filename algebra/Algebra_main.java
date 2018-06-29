package algebra;

import java.util.Scanner;

public class Algebra_main {

	public static void main(String[] args) {
		System.out.println("Enter equation");
		Scanner keyboard = new Scanner(System.in);
		//String toEvaluate = keyboard.nextLine();
		
		//String toEvaluate = "3 * 5 = x + 2 + 1 + 4* 1 * 2 + 1";
		String toEvaluate = "4*x*(5+3)*2=8*1";
		
		Equation test = new Equation(toEvaluate);
	    //Expression test = new Expression("8 + 4 - 1");
		System.out.println("parsed equation: ");
		test.printEquation();
		System.out.print("# of operators");
		System.out.println(test.getLeftSide().operators.size());
		
		
		System.out.println(test.solveEquation());
		System.out.println("answer above");
		
		Expression right = new Expression(5);
		Expression left = new Expression(9);
		left.append(new Expression(3), Operator.add);
		left.append(new Expression(4), Operator.add);
		left.append(new Expression(12), Operator.add);
		
		Expression innerLeft = new Expression(3);
		innerLeft.append(new Expression(5), Operator.multiply);
		left.append(innerLeft, Operator.add);
		
		
		Expression x = new Expression(0);
		x.setType(math_type.symbol);
		Equation master = new Equation(left, right);
		
		right.append(new Expression(7), Operator.multiply);
		right.append(x, Operator.divide);
		master.printEquation();
		master.simplify();
		
		master.printEquation();
		System.out.println(master.solveEquation());
		
	}

}
