package algebra;
import java.util.ArrayList;

public class Expression {
	
	// fields
	public ArrayList<Expression> terms;
	public ArrayList<Operator> operators;
	private math_type type;
	private double numericValue;
	
	
	
	private void reset() {
		this.type = null;
		this.numericValue = Double.NaN;
		this.operators = null;
		this.terms = null;
	}
	
	
	
	public void clean() {
		if (this.getType() != math_type.parent) {
			operators = null;
			terms = null;
		} else if (this.getType() != math_type.number) {
			this.numericValue = Double.NaN;
		}
		
		if (terms != null && terms.size() == 1) {
			if (terms.get(0).isNumber()) {
				this.setNumericValue(terms.get(0).getNumericValue());
			} else if (terms.get(0).isParent() && this.terms.get(0).terms != null) {
				this.terms = terms.get(0).terms;
				this.operators = terms.get(0).operators;
				this.setType(math_type.parent);
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
	
	// add another expression into the terms array
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
		} else if (this.type == math_type.parent && !isSameOrder(newOperator, operators.get(0))) {
			
			Expression oldMe = new Expression (this);
			initializeOperatorList(newOperator);
			operators.add(newOperator);
			this.terms = new ArrayList<Expression>();
			terms.add(oldMe);
			terms.add(newTerm);
			
		} else if (this.getType() == null) {
			
			operators = new ArrayList<Operator>();
			terms = new ArrayList<Expression>();
			operators.add(newOperator);
			terms.add(newTerm);
			this.setType(math_type.parent);
			return;
		}
		

		
		if (operators == null) {
			this.initializeOperatorList(newOperator);
		}
		if (terms == null) {
			terms = new ArrayList<Expression>();
		}
		
		this.setType(math_type.parent);
		this.terms.add(newTerm);
		this.operators.add(newOperator);
	}
	
	// returns true if there is a variable somewhere inside the Expression
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
	
	
	// does the appropriate mathematical functions to simplify as much as possible
	public void simplify() {
		if (this.type != math_type.parent) {
			return;
		}
		
		double answer = 0;
		if (operators.get(0) == Operator.multiply || operators.get(0) == Operator.divide) {
			answer = 1;
		}
		int symbolIndex = -1;
		Operator symbolOperator = null;
		
		for (int k = 0; k < terms.size(); k++) {
			terms.get(k).simplify();
			if (terms.get(k).getType() != math_type.symbol) {
				
				if (operators.get(k) == Operator.add) {
					answer += terms.get(k).getNumericValue();
				} else if (operators.get(k) == Operator.subtract) {
					answer -= terms.get(k).getNumericValue();
				} else if (operators.get(k) == Operator.multiply) {
					answer *= terms.get(k).getNumericValue();
				} else if (operators.get(k) == Operator.divide) {
					answer /= terms.get(k).getNumericValue();
				} else {
					System.out.println("operator not found");
					System.exit(0);
				} // end of inner if
				
			} else {
				symbolIndex = k;
				symbolOperator = operators.get(k);
				
			} // end of outer if
		} // end of loop
		
		
		// if a variable is present, we simplify as much as we can, otherwise we just make this Expression a number
		if (this.containsSymbol()) {	
			// It's easier to use constructors to get things the way we want
			Expression newMe = new Expression(answer);
			newMe.append(new Expression(), symbolOperator);
			this.setType(math_type.parent);
			
			this.terms = newMe.terms;
			this.operators = newMe.operators;
		} else {
			this.setNumericValue(answer);
		}
		
		
	
	}// end of function simplify()
	
	/* === utility functions === */
	
	public void invert() {
		if (this.type == math_type.number) {
			this.numericValue = 1/numericValue;
		} else if (this.type == math_type.symbol) {
			this.reset();
			operators = new ArrayList<Operator>();
			terms = new ArrayList<Expression>();
			
			terms.add(new Expression(1));
			operators.add(Operator.multiply);
			
			terms.add(new Expression());
			operators.add(Operator.divide);
		}
	}
	
	// static method. used when parsing math expression
	public static boolean isOperator(char operator) {
		return (operator == '*' || operator == '/' || operator == '+' || operator == '-');
	}
	
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
	
	public void printExpression(boolean parenthesis) {
		if (parenthesis) {
			System.out.print("(");
		}
		
		
		if (this.type == math_type.number) {
			System.out.print(this.numericValue);
			
		} else if (this.type == math_type.symbol) {
			System.out.print("x");
		} else if (this.type == math_type.parent) {
		
			terms.get(0).printExpression(false);
			for (int k = 1; k < terms.size(); k++) {
				printOperator(this.operators.get(k));
				boolean useParenthesis = false;
				if (this.terms.get(k).type == math_type.parent) {
					useParenthesis = true;
				}
				this.terms.get(k).printExpression(useParenthesis);
				
			}

			
		} else {
			System.out.println("this Expression doesn't have a type"); 
			System.exit(0);
		}
		
		if (parenthesis) {
			System.out.print(")");
		}
		
	}
	
	public Transfer isolateVariable() {
		if (!this.containsSymbol() || this.type == math_type.symbol) {
			System.out.println("didn't find symbol");
			return null;
		// if this Expression is 1/x	
		} else if (terms.get(0).getNumericValue() == 1 && terms.get(1).isSymbol() && operators.get(1).equals(Operator.divide)) {
			this.setType(math_type.symbol);
			Transfer pleaseInvert = new Transfer();
			pleaseInvert.setType(Transfer_type.invert);
			return pleaseInvert;
		}
		
		
		// find a numeric value
		for (int k = 0; k < terms.size(); k++) {
			// find a term that isn't a symbol
			if (terms.get(k).containsSymbol()) {
				continue;
			}
			
			Transfer toReturn = new Transfer();
			toReturn.setNumber(terms.get(k).getNumericValue());
			toReturn.setOperator(this.operators.get(k));
			
			
			terms.remove(k);
			operators.remove(k);
			if (operators.size() == 1 && terms.get(0).isSymbol()) {
				if (operators.get(0) == Operator.subtract) {
					operators.set(0, Operator.multiply);
					this.append(new Expression(-1), Operator.multiply);
				} else if (operators.get(0) == Operator.divide) {
					operators.set(0, Operator.multiply);
					terms.set(0, new Expression(1));
					this.append(new Expression(), Operator.divide);
				} else {
			
					this.reset();
					this.setType(math_type.symbol);
				}
				
			}
			
			return toReturn;
		}
		System.out.println("couldn't return anything");
		System.out.println(operators.get(0));
		return null;
	}
	
	public static Operator parseOperator(char current) {
		if (current == '+') {
			return Operator.add;
		} else if (current == '-') {
			return Operator.subtract;
		} else if (current == '*') {
			return Operator.multiply;
		} else if (current == '/') {
			return Operator.divide;
		}
		return null;
	}
	

	/* === constructors === */
	public Expression() {
		this.reset();
		this.type = math_type.symbol;
	}
	
	public Expression(String input) {
		this.reset();
		
		input = input.replace(" ", "");
		//this.setType(math_type.parent);
		double lastNumber = Double.NaN;
		Operator lastOperator = Operator.add;
		String currentNumber = null;
		boolean lookingForOperator = false;
		boolean insideNumber = false;
		for (int k = 0; k < input.length(); k++) {
			char current = input.charAt(k);
			
			if (current == 'x') {
				this.append(new Expression(), lastOperator);
				continue;
			}
			
			if ((current >= '1' && current <= '9') && !lookingForOperator)  {
				
				if (insideNumber) {
					currentNumber = currentNumber + current;
				} else {
					insideNumber = true;
					currentNumber = Character.toString(current);
				}
				
			} else if (Expression.isOperator(current)) {
				
				if (currentNumber != null) {
					
					lastNumber = Double.parseDouble(currentNumber);
					
					this.append(new Expression(lastNumber), lastOperator);
				
					currentNumber = null;
					lastOperator = null;
					lastNumber = Double.NaN;
					insideNumber = false;
					lookingForOperator = true;
				}
				
				lastOperator = parseOperator(current);
				lookingForOperator = false;
				
			} else {
				System.out.println("error evaluating equation");
				System.exit(0);
			}
			
		}
		
		// parsing and adding the last number to the equation
		if (currentNumber != null) {
			lastNumber = Double.parseDouble(currentNumber);
			this.append(new Expression(lastNumber), lastOperator);
		}
		
		
	}
	
	public Expression(double number) {
		this.reset();
		this.setNumericValue(number);
	}
	
	
	
	public Expression(Expression toCopy) {
		this.terms = toCopy.terms;
		this.operators = toCopy.operators;
		this.numericValue = toCopy.numericValue;
		this.type = toCopy.getType();
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

	public Operator getOperator(int index) {
		if (index >= operators.size()) {
			System.out.println("invalid Operator");
			System.exit(1);
		}
		return operators.get(index);
	}
	
	public void setOperator(int index, Operator newOperator) {
		operators.set(index, newOperator);
	}
	// === simple information getters === //
	public boolean isParent() {
		return (this.getType() == math_type.parent) ;
		
	}
	
	public boolean isSymbol() {
		return (this.getType() == math_type.symbol) ;
	}

	public boolean isNumber() {
		return (this.getType() == math_type.number) ;
	}
	
	public boolean isSameOrder(Operator x, Operator y)  {
		return (((x == Operator.add || x == Operator.subtract) && (y == Operator.add || y == Operator.subtract))    ||     ((x == Operator.multiply || x == Operator.divide) && (y == Operator.multiply || y == Operator.divide)));
	}
	
}



