package functions.unitary;

import functions.Function;
import functions.special.Constant;

import java.util.Map;


public class Dirac extends UnitaryFunction {
	/**
	 * Constructs a new Dirac
	 * @param operand The function which the Dirac-Delta function is operating on
	 */
	public Dirac(Function operand) {
		super(operand);
	}

	@Override
	public double evaluate(Map<Character, Double> variableValues) {
		if (operand.evaluate(variableValues) == 0)
			return Double.POSITIVE_INFINITY;
		else
			return 0;
	}

	@Override
	public Function getDerivative(char varID) {
		return new Constant(0);
	}

	public UnitaryFunction me(Function operand) {
		return new Dirac(operand);
	}
}

