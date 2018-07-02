package algebra;
import java.util.ArrayList;

public class Expression {
	
	// fields
	public ArrayList<Expression> terms;
	public ArrayList<Operator> operators;
	private math_type type;
	private double numericValue;
	
	
	// sets everything to null or NaN
	private void reset() {
		this.type = null;
		this.numericValue = Double.NaN;
		this.operators = null;
		this.terms = null;
	}
	
	// makes this Expression a variable
	public void makeSymbol() {
		this.reset();
		this.setType(math_type.symbol);
	}
	
	
	// does some checks to make sure the data is formatted in a good way. Call this after mutating the object
	public void clean() {
		
		if (terms != null && terms.size() != operators.size()) {
			System.out.println("operators and terms not synched");
			System.exit(0);
		}
		
		if (terms != null && terms.size() == 1) {
			
			// if only term is division, then prepend a 1. in other words, go from "/3" to "1/3"
			// this may happen during the simplify function
			if (operators.get(0) == Operator.divide) {
				operators.add(0, Operator.multiply);
				terms.add(0, new Expression(1));
			} else {
			this.copyFrom(terms.get(0));
			}// end of inner if
			
		} // end of outer if
		
		// prevents the first operator from being of a different order than the others
		if (operators != null && operators.size() >= 2 && !isSameOrder(operators.get(0), operators.get(1))) {
			if (isSameOrder(operators.get(1), Operator.multiply)) {
				operators.set(0, Operator.multiply);
			} else {
				operators.set(0, Operator.add);
			}
		}
		
		
	}
	
	public boolean containsMismatch() {
		if (!this.isParent()) {
			return false;
		}
		
		Operator first = operators.get(0);
		for (Operator k : operators) {
			if (!isSameOrder(first, k)) {
				return true;
			}
		}
		return false;
	}
	
	/* used by String constructor to apply order of operations
	 * Expressions can have Expressions nested inside of them
	 * Multiplication goes before addition so multiplication is put in its own Expression and 
	 * nested inside inside this Expression. This is how I keep track of order of operations
	 */
	private void sortOperators() {
		
		// base cases
		if (operators == null || operators.size() < 2 || !this.containsMismatch()) {
			return;
		}
		
		int k = 1;
		// for loop exists to find a term that is multiplication or division so we can "nest" it
		for (k = 1; k < operators.size(); k++) {
			if (isSameOrder(operators.get(k), Operator.multiply)) {
				break;
			}
		}
		if (k == operators.size()) {
			return;
		}
		
			// make new Expression with the multiplication or division operation inside
			Expression toNest = new Expression(terms.get(k-1));
			terms.remove(k-1);
			operators.remove(k-1);
			k--;
			
			for (int i = k; i < operators.size(); i++) {
				if (isSameOrder(operators.get(i), Operator.multiply)) {
					toNest.append(terms.get(i), operators.get(i));
					terms.remove(i);
					operators.remove(i);
					i--;
				} else if (isSameOrder(operators.get(i), Operator.add)) {
					break;
				}
			}

			// remove the elements, combine them, and nest the new Expression inside this Expressio
			this.append(toNest, Operator.add);
			
			// recursive call - could implement loop instead
			for (Operator j : operators) {
				System.out.println(j);
			}
			System.out.println();
			this.clean();
			this.sortOperators();
			
	}
	
	// the first Operator is usually meaningless but I like to have it as the same order of operation as the other operators
	public void initializeOperatorList(Operator secondOperator) {
		this.operators = new ArrayList<Operator>();
		
		if (secondOperator == Operator.add || secondOperator == Operator.subtract) {
			this.operators.add(Operator.add);
		} else if (secondOperator == Operator.multiply || secondOperator == Operator.divide) {
			this.operators.add(Operator.multiply);
		}
	}
	
	// add another expression into the terms array along with adding an operator
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
			
		} else if (this.getType() == null) {
			
			operators = new ArrayList<Operator>();
			terms = new ArrayList<Expression>();
			operators.add(newOperator);
			terms.add(newTerm);
			this.setType(math_type.parent);
			return;
		}
		

		// initialize ArrayLists if needed
		
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
		this.clean();
		if (this.type != math_type.parent) {
			return;
		}
		
		double answer = 0;
		if (operators.get(0) == Operator.multiply || operators.get(0) == Operator.divide) {
			answer = 1;
		}

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
				
				symbolOperator = operators.get(k);
				
			} // end of outer if
		} // end of loop
		
		
		// if a variable is present, we simplify as much as we can, otherwise we just make this Expression a number
		if (this.containsSymbol()) {	
			// It's easier to use constructors to get things the way we want
			Expression newMe = new Expression(answer);
			newMe.append(new Expression(), symbolOperator);
			this.copyFrom(newMe);
			
		} else {
			this.setNumericValue(answer);
		}
		
		
	
	}// end of function simplify()
	
	// USE FUNCTION CAREFULLY - it is mutable and significantly changes this expression. 
	// It removes part of the Expression and returns it along with its operator in a special class, Transfer
	// This function is used by the Equation class to transfer terms from one side of the equation to the other
	public Transfer isolateVariable() {
		
		// if this Expression doesn't have a variable or is a variable, tell the equation to multiply by one
		if (!this.containsSymbol() || this.type == math_type.symbol) {
			Transfer timesOne = new Transfer();
			timesOne.setNumber(1);
			timesOne.setOperator(Operator.multiply);
			return timesOne;
		// if this Expression is 1/x	
		} else if (terms.size() == 2 && terms.get(0).getNumericValue() == 1 && terms.get(1).containsSymbol() && operators.get(1).equals(Operator.divide)) {
			operators.set(1, Operator.multiply);
			Transfer pleaseInvert = new Transfer();
			pleaseInvert.setType(Transfer_type.invert);
			return pleaseInvert;
		} else if (this.isSimpleFraction() && terms.get(1).containsSymbol()) {
			
		}
		
		//TODO: clean up this loop. The structure of the code below is aweful
		// finding a numeric value
		for (int k = 0; k < terms.size(); k++) {
			// find a term that isn't a symbol
			if (terms.get(k).containsSymbol()) {
				continue;
			}
			
			Expression clean = terms.get(k);
			clean.simplify();
			
			Transfer toReturn = new Transfer();
			toReturn.setNumber(clean.getNumericValue());
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
	
	
	// function asks: are any of the TOP LEVEL operators multiplication or division?
	// this could return false if there is nested multiplication
	public boolean containsMultOrDiv() {
		if (operators == null) {
			return false;
		}
		
		// for each loop looking for multiplication or division
		for (Operator k : operators) {
			if (k == Operator.multiply || k == Operator.divide) {
				return true;
			}
		}
		
		// loop didn't find multiplication or division in operators
		return false;
		
	}
	
	
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
		} else {
			//TODO: implement this feature
			System.out.println("inverting polynomials isn't a feature yet");
			System.exit(0);
		}
	}
	
	
	
	
	// parenthesis parameter determines whether parenthesis surounds this expression
	public void printExpression(boolean parenthesis) {
		if (parenthesis) {
			System.out.print("(");
		}
		
		// different instructions depending whether this is a number, variable or parent
		if (this.type == math_type.number) {
			System.out.print(this.numericValue);
			
		} else if (this.type == math_type.symbol) {
			System.out.print("x");
		} else if (this.type == math_type.parent) {
		
			// first Operator isn't printed
			if (this.terms.get(0).type == math_type.parent) {
				terms.get(0).printExpression(true);
			} else {
				terms.get(0).printExpression(false);
			}
			
			// only parent Expressions that have multiple terms inside them get parenthesis
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
	
	// prints out expression but with a new line afterwards
	public void printExpressionln(boolean parenthesis) {
		this.printExpression(parenthesis);
		System.out.println();
	}
	

	public void copyFrom(Expression other) {
		this.numericValue = other.numericValue;
		this.type = other.type;
		this.terms = other.terms;
		this.operators = other.operators;
	}
	
	/* === constructors === */
	public Expression() {
		this.reset();
		this.type = math_type.symbol;
	}
	
	//TODO: add support for parenthesis
	public Expression(String input) {
		// remove spaces from input
		input = input.replace(" ", "");
		
		Operator lastOperator = Operator.add;

		
		double currentNumber_double = Double.NaN;
		String currentNumberStr = null;
		boolean insideNumber = false;
		
		
		for (int k = 0; k < input.length(); k++) {
			char current = input.charAt(k);
			
			if (current == 'x') {
				if (lastOperator == null) {
					this.makeSymbol();
				}
				this.append(new Expression(), lastOperator);
				continue;
			}
			// found number
			if ((current >= '0' && current <= '9') || current == '.')  {
				
				// numbers may be multiple characters long so we need to keep track if
				// we're currently parsing a number or this is the first letter
				if (insideNumber) {
					currentNumberStr = currentNumberStr + current;
				} else {
					insideNumber = true;
					currentNumberStr = Character.toString(current);
				}
				
			} else if (Expression.isOperator(current)) {
				
				// if we've reached the end of a number we need to parse
				   // parse it and append the number
				if (currentNumberStr != null) {
					
					currentNumber_double = Double.parseDouble(currentNumberStr);
					
					
					if (lastOperator == null) {
						this.setNumericValue(currentNumber_double);
					} else {
						this.append(new Expression(currentNumber_double), lastOperator);
					}
					currentNumberStr = null;
					lastOperator = null;
					currentNumber_double = Double.NaN;
					insideNumber = false;
					
				}
				
				// parse and remember this operator for later
				lastOperator = parseOperator(current);
				
			} else if (current == '(') {
				
				int parenthesisLevel = 1;
				int i = k+1;
				while (parenthesisLevel > 0 && i < input.length()) {
					if (input.charAt(i) == '(') {
						parenthesisLevel++;
					} else if (input.charAt(i) == ')') {
						parenthesisLevel--;
					}
					i++;
				}
				
				this.append(new Expression(input.substring(k+1, i-1)), lastOperator);
				
				k = i;
			} else {
				System.out.println("error parsing equation");
				System.exit(0);
			}
			
			
		}
		
		// parsing and adding the last number to the equation. not necessary if last term is variable
		if (currentNumberStr != null) {
			currentNumber_double = Double.parseDouble(currentNumberStr);
			this.append(new Expression(currentNumber_double), lastOperator);
		}
		
		// apply order of operations
		sortOperators();
		
	}
	
	public Expression(double number) {
		this.reset();
		this.setType(math_type.number);
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
	
	public boolean isSimpleFraction( ) {
		return (operators != null && operators.size() == 2 && operators.get(1) == Operator.divide);
	}
	
	
	// ==== static utility functions ==== \\

	
	// checks if char is supported operator
		public static boolean isOperator(char operator) {
			return (operator == '*' || operator == '/' || operator == '+' || operator == '-');
		}
		
		public static boolean isSameOrder(Operator x, Operator y)  {
			return (((x == Operator.add || x == Operator.subtract) && (y == Operator.add || y == Operator.subtract))    ||     ((x == Operator.multiply || x == Operator.divide) && (y == Operator.multiply || y == Operator.divide)));
		}
		
		public static void printOperator(Operator toPrint) {
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
		
		// returns correct Operator enum for given char
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
	
}



