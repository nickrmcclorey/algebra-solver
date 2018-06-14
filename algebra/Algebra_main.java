package algebra;

import java.util.ArrayList;

public class Algebra_main {

	public static void main(String[] args) {
		
		
		Expression right = new Expression(5);
		Expression left = new Expression(89);
		left.append(new Expression(3), Operator.add);
		left.append(new Expression(4), Operator.add);
		left.append(new Expression(12), Operator.add);
		
	
		
		Expression x = new Expression(0);
		x.setType(math_type.symbol);
		Equation master = new Equation(left, right);
		
		right.append(new Expression(2), Operator.add);
		right.append(x, Operator.subtract);
		master.printEquation();

		
	}

}
