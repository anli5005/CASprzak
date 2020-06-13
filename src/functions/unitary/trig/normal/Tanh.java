package functions.unitary.trig.normal;

import config.Settings;
import functions.GeneralFunction;
import functions.binary.Pow;
import functions.commutative.Product;
import functions.unitary.piecewise.DomainRestrictor;
import functions.unitary.specialcases.Ln;
import functions.unitary.UnitaryFunction;
import functions.unitary.trig.inverse.Atanh;
import tools.DefaultFunctions;

import java.util.Map;


public class Tanh extends TrigFunction {

	/**
	 * Constructs a new {@link Tanh}
	 * @param operand The function which tanh is operating on
	 */
	public Tanh(GeneralFunction operand) {
		super(operand);
	}

	/**
	 * Returns the hyperbolic tangent of the stored {@link #operand} evaluated
	 * @param variableValues The values of the variables of the {@link GeneralFunction} at the point
	 * @return the tanh of {@link #operand} evaluated
	 */
	@Override
	public double evaluate(Map<String, Double> variableValues) {
		return Math.tanh(operand.evaluate(variableValues));
	}

	@Override
	public GeneralFunction getDerivative(String varID) {
		return new Product(operand.getSimplifiedDerivative(varID), new Pow(DefaultFunctions.NEGATIVE_TWO, new Cosh(operand)));
	}

	public UnitaryFunction getInstance(GeneralFunction operand) {
		return new Tanh(operand);
	}


	public GeneralFunction getElementaryIntegral() {
		return new Ln(new Cosh(operand));
	}

	public Class<?> getInverse() {
		return Atanh.class;
	}

	@Override
	public GeneralFunction simplifyInverse() {
		if (operand.getClass().isAssignableFrom(getInverse())) {
			GeneralFunction insideFunction = ((UnitaryFunction) operand).operand;
			if (Settings.enforceDomainAndRange)
				return new DomainRestrictor(insideFunction, a -> a > -1 && a < 1);
			else
				return insideFunction;
		} else
			return this;
	}
}
