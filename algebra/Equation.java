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
		
		// while the variable side has numbers in addition to the variable
		while (!varSide.isSymbol()) {
			this.printEquation();
			// varSide removes an Expression from itself and gives it to us so we can transfer it to the other side
			Transfer toTransfer = varSide.isolateVariable();
			
			
			if (toTransfer == null) {
				System.out.println("Error while solving equation");
				System.exit(0);
			} else if (toTransfer.getType() == Transfer_type.invert) {
				cleanSide.invert();
			}
			
			if (toTransfer.getOperator() == Operator.add) {
				toTransfer.setOperator(Operator.subtract);
			} else if (toTransfer.getOperator() == Operator.subtract) {
				toTransfer.setOperator(Operator.add);
			} else if (toTransfer.getOperator() == Operator.divide) {
				toTransfer.setOperator(Operator.multiply);
			} else if (toTransfer.getOperator() == Operator.multiply) {
				toTransfer.setOperator(Operator.divide);
			} else {
				System.out.println("Failed to transfer variable");
				System.exit(0);
			}
			
			cleanSide.append(new Expression(toTransfer.getNumber()), toTransfer.getOperator());
			cleanSide.simplify();
			
			
		}
		
		cleanSide.simplify();
		return cleanSide.getNumericValue();
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
