package algebra;


public class Expression {
	
	private math_type type;
	private double numericValue;
	
	private Expression firstExp;
	private Expression secondExp;
	private Operator operator;
	
	private void reset() {
		this.type = null;
		this.numericValue = Double.NaN;
		this.firstExp = null;
		this.secondExp = null;
		this.operator = null;
	}
	
	
	
	public void parseExpression(String input) {

		final char space = ' ';
		
		double parsedNumber = Double.NaN;
		String doubleAsString = "";
		boolean expectingNumber = true;
		boolean insideNumber = false;
	
		for (int k = 0; k < input.length(); k++) {
			char currentChar = input.charAt(k);
			// if char is a number and we're expecting a number
			if (currentChar >= '1' && currentChar <= '9' && expectingNumber) {
				doubleAsString = doubleAsString + input.charAt(k);
				insideNumber = true;
			} else if (insideNumber && currentChar == space) {
				parsedNumber = Double.parseDouble(doubleAsString);
				insideNumber = false;
				expectingNumber = false;
			}
		}
	}

	// TODO: implement function
	public Expression simplify() {
		
		return new Expression();
	}
	
	/* === utility functions === */
	
	public void printExpression() {
		if (this.type == math_type.number) {
			System.out.print(this.numericValue);
		} else if (this.type == math_type.symbol) {
			System.out.print('x');
		} else {
			
			if (this.operator == Operator.subtract || this.operator == Operator.divide) {
				System.out.print("(");
			}
			
			firstExp.printExpression();
			System.out.print(" " + this.printOperatorString() + " ");
			secondExp.printExpression();
			
			if (this.operator == Operator.subtract || this.operator == Operator.divide) {
				System.out.print(")");
			}
			
		}
	}
	
	public String printOperatorString() {
		
		if (this.operator == Operator.add) {
			return "+";
		} else if (this.operator == Operator.subtract) {
			return "-";
		} else if (this.operator == Operator.multiply) {
			return "*";
		} else if (this.operator == Operator.divide) {
			return "/";
		} else if (this.operator == Operator.power) {
			return "^";
		} else {
			System.out.println("\n" + "ERROR: Operator not set");
			System.exit(1);
		}
		
		// hopefully something has been found but this just keeps the compiler happy
		return "";
	}
	
	/* === constructors === */
	public Expression() {
		this.reset();
	}
	
	public Expression(String inputToParse) {
		
	}
	
	public Expression(double number) {
		this.reset();
		this.setNumericValue(number);
	}
	
	/* === getters and setters === */
	public math_type getType() {
		return type;
	}
	public void setType(math_type type) {
		if (type == math_type.symbol) {
			this.reset();
		}
		this.type = type;
	}
	public double getNumericValue() {
		return numericValue;
	}
	public void setNumericValue(double numericValue) {
		// make sure the firstExp and secondExp are null
		this.reset();
		this.numericValue = numericValue;
		this.type = math_type.number;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Expression getSecondExp() {
		return secondExp;
	}

	public void setSecondExp(Expression secondExp) {
		this.secondExp = secondExp;
		this.setType(math_type.parent);
	}
	
	public void setSecondExp(double number) {
		this.secondExp = new Expression(number);
	}

	public Expression getFirstExp() {
		return firstExp;
	}

	public void setFirstExp(Expression firstExp) {
		this.firstExp = firstExp;
		this.setType(math_type.parent);
	}
	
	public void setFirstExp(double number) {
		this.firstExp = new Expression(number);
	}
	
}
