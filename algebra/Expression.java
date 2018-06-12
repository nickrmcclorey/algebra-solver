package algebra;
import java.util.ArrayList;

public class Expression {
	
	public ArrayList<Expression> terms;
	public Operator operator;
	
	private math_type type;
	private double numericValue;
	
	private void reset() {
		this.type = null;
		this.numericValue = Double.NaN;
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

	
	
	public void add(Expression newTerm) {
		if (this.type == math_type.number) {
			Expression remember = new Expression(this.numericValue);
			this.reset();
			this.setType(math_type.parent);
			this.terms = new ArrayList<Expression>(2);
			this.terms.add(remember);
			
		} else if (this.type == math_type.symbol) {
			this.reset();
			Expression x = new Expression();
			x.setType(math_type.symbol);
			this.terms = new ArrayList<Expression>();
			this.terms.add(x);
		}
		this.terms.add(newTerm);
	}
	
	
	
	
	// TODO: implement function
	public Expression simplify() {
		
		return new Expression();
	}
	
	/* === utility functions === */
	
	public void makeSymbol() {
		this.reset();
		this.setType(math_type.symbol);
	}
	
	public void printExpression() {
		if (this.type == math_type.number) {
			System.out.print(this.numericValue);
			return;
		} else if (this.type == math_type.symbol) {
			System.out.print("x");
		} else if (this.type == math_type.parent) {
		
			for (int k = 0; k < terms.size() - 1; k++) {
				this.terms.get(k).printExpression();
				System.out.print(" + ");
			}
			
			this.terms.get(terms.size() - 1).printExpression();
			
		} else {
			System.out.println("this Expression doesn't have a type"); 
			System.exit(0);
		}
	}
	
	public String printOperatorString() {
		
//		if (this.operator == Operator.add) {
//			return "+";
//		} else if (this.operator == Operator.subtract) {
//			return "-";
//		} else if (this.operator == Operator.multiply) {
//			return "*";
//		} else if (this.operator == Operator.divide) {
//			return "/";
//		} else if (this.operator == Operator.power) {
//			return "^";
//		} else {
//			System.out.println("\n" + "ERROR: Operator not set");
//			System.exit(1);
//		}
		
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
	
	public Expression(ArrayList<Expression> terms) {
		this.reset();
		this.setType(math_type.parent);
		this.terms = terms;
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
		this.reset();
		this.numericValue = numericValue;
		this.type = math_type.number;
	}


	
}
