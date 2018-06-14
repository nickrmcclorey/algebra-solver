package algebra;
import java.util.ArrayList;

public class Expression {
	
	public ArrayList<Expression> terms;
	public ArrayList<Operator> operators;
	
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

	
	
	public void initializeOperatorList(Operator secondOperator) {
		this.operators = new ArrayList<Operator>();
		
		if (secondOperator == Operator.add || secondOperator == Operator.subtract) {
			this.operators.add(Operator.add);
		} else if (secondOperator == Operator.multiply || secondOperator == Operator.divide) {
			this.operators.add(Operator.multiply);
		}
	}
	
	public void append(Expression newTerm, Operator newOperator) {
		
		// if this expression is a number of symbol, we must change this expression to a parent
		if (this.type == math_type.number) {
			Expression remember = new Expression(this.numericValue);
			this.reset();
			this.setType(math_type.parent);
			
			this.terms = new ArrayList<Expression>();
			this.terms.add(remember);
			this.initializeOperatorList(newOperator);
			
		} else if (this.type == math_type.symbol) {
			Expression x = new Expression();
			x.setType(math_type.symbol);
			
			this.terms = new ArrayList<Expression>();
			this.terms.add(x);
			this.initializeOperatorList(newOperator);
			
		}
		
		this.terms.add(newTerm);
		this.operators.add(newOperator);
	}
	
	public boolean containsSymbol() {
		if (this.type == math_type.number) {
			return false;
		} else if (this.type == math_type.symbol) {
			return true;
		}
		// else this expression is a parent
		for (Expression k : this.terms) {
			if (k.containsSymbol()) {
				return true;
			}
		}
		
		// no symbols have been found so return false
		return false;
		
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
	
	public void printOperator(Operator toPrint) {
		if (toPrint == Operator.add) {
			System.out.print(" + ");
		} else if (toPrint == Operator.subtract) {
			System.out.print(" - ");
		} else if (toPrint == Operator.divide) {
			System.out.print(" / ");
		} else if (toPrint == Operator.multiply) {
			System.out.print(" * ");
		}
	}
	
	public void printExpression() {
		if (this.type == math_type.number) {
			System.out.print(this.numericValue);
			return;
		} else if (this.type == math_type.symbol) {
			System.out.print("x");
		} else if (this.type == math_type.parent) {
		
			terms.get(0).printExpression();
			for (int k = 1; k < terms.size(); k++) {
				printOperator(this.operators.get(k));
				this.terms.get(k).printExpression();
				
			}

			
		} else {
			System.out.println("this Expression doesn't have a type"); 
			System.exit(0);
		}
	}
	
	
	/* === constructors === */
	public Expression() {
		this.reset();
		this.type = math_type.symbol;
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
