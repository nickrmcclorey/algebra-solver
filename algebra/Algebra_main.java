package algebra;


public class Algebra_main {

	public static void main(String[] args) {
	
		
		String toEvaluate = "3 + 8 = 2 + x";
		Equation test = new Equation(toEvaluate);
	    //Expression test = new Expression("8 + 4 - 1");
		test.printEquation();
		
		System.out.println(test.solveEquation());

		
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
