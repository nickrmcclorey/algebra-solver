package algebra;

import java.util.ArrayList;

public class Algebra_main {

	public static void main(String[] args) {
		
		
		Expression right = new Expression(5);
		ArrayList<Expression> left = new ArrayList<Expression>(3);
		left.add(new Expression(3));
		left.add(new Expression(4));
		left.add(new Expression(12));
		
		Expression actualLeft = new Expression(left);
		
		Expression x = new Expression(0);
		x.setType(math_type.symbol);
		Equation master = new Equation(actualLeft, right);
		
		right.add(new Expression(2));
		right.add(x);
		master.printEquation();

		
	}

}
