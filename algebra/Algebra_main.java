package algebra;

public class Algebra_main {

	public static void main(String[] args) {
		
		Expression left = new Expression();
		left.setFirstExp(new Expression(3));
		left.setSecondExp(new Expression(5));
		left.setOperator(Operator.subtract);
		
		Expression second = new Expression();
		second.setFirstExp(8);
		Expression x = new Expression();
		x.setType(math_type.symbol);
		second.setSecondExp(x);
		
		left.setSecondExp(second);
		left.getSecondExp().setOperator(Operator.subtract);
		
		Expression right = new Expression(5);
		
		Equation master = new Equation(left, right);
		
		
		master.printEquation();

	}

}
