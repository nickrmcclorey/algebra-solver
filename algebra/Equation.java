package algebra;

public class Equation {

	private Expression leftSide;
	private Expression rightSide;
	
	
	public void printEquation() {
		leftSide.printExpression();
		System.out.print(" = ");
		rightSide.printExpression();
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
