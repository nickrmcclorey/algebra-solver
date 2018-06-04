package algebra;

public class Algebra_main {

	public static void main(String[] args) {
		
		Expression master = new Expression();
		master.setFirstExp(new Expression(3));
		master.setSecondExp(new Expression(5));
		master.setOperator(Operator.add);
		
		System.out.println(master.getType());
		master.printExpression();

	}

}
