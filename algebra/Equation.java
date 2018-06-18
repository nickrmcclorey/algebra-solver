package algebra;

public class Equation {

	private Expression leftSide;
	private Expression rightSide;
	private Expression cleanSide;
	private Expression varSide;
	
	
	public double solveEquation() {
		
		if (leftSide.containsSymbol() && !rightSide.containsSymbol()) {
			varSide = leftSide;
			cleanSide = rightSide;
		} else if (rightSide.containsSymbol() && !leftSide.containsSymbol()) {
			varSide = rightSide;
			cleanSide = leftSide;
		} else {
			System.out.println("error, two variables found, that feature is not yet available");
			System.exit(0);
		}
		
		
		
		return 0;
	}
	
	public void printEquation() {
		leftSide.printExpression(false);
		System.out.print(" = ");
		rightSide.printExpression(false);
		System.out.println();
	}
	
	public void simplify() {
		leftSide.simplify();
		rightSide.simplify();
		 
	}
	
	public Equation(Expression left, Expression right) {
		this.setLeftSide(left);
		this.setRightSide(right);
	}
	
	/* === getters and setters === */
	public Expression getLeftSide() {
		return leftSide;
	}
	public void setLeftSide(Expression leftSide) {
		this.leftSide = leftSide;
	}
	public Expression getRightSide() {
		return rightSide;
	}
	public void setRightSide(Expression rightSide) {
		this.rightSide = rightSide;
	}	
	
}
