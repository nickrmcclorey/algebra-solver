package algebra;

public class Transfer {

	private double number;
	private Operator operator;
	private Transfer_type type;
	
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator original) {
		this.operator = original;
	}
	public Transfer_type getType() {
		return type;
	}
	public void setType(Transfer_type type) {
		this.type = type;
	}
	
	
	
}
