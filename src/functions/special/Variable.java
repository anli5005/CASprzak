package functions.special;

import config.Settings;
import functions.GeneralFunction;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


public class Variable extends SpecialFunction {

	/**
	 * The String representing this variable
	 */
	public final String varID;

	/**
	 * This Pattern matches all valid variable and function names
	 */
	public static final Pattern validVariables = Pattern.compile("[a-zA-Z[^\\x00-\\x7F]]|\\\\[a-zA-Z[^\\x00-\\x7F]][a-zA-Z0-9_[^\\x00-\\x7F]]*");

	/**
	 * Constructs a new {@link Variable}
	 * @param varID The variable's representative String
	 */
	public Variable(String varID) {
		if (!validVariables.matcher(varID).matches())
			throw new IllegalArgumentException(
					"Invalid variable name '" + varID + "'. " +
					"If this is not user error, it may indicate a splitting or parsing failure. " +
					"Valid variable names are a single letter character, or an escaped letter character followed by up to " + (Settings.maxEscapeLength - 2) + " letters, numbers, or underscores."
			);
		this.varID = varID;
	}


	public String toString() {
		return varID;
	}


	public GeneralFunction getDerivative(String varID) {
		return new Constant(varID.equals(this.varID) ? 1 : 0);
	}

	public double evaluate(Map<String, Double> variableValues) {
		if (!variableValues.containsKey(varID))
			throw new NoSuchElementException("No value was assigned to variable " + varID);
		else
			return variableValues.get(varID);
	}


	public GeneralFunction clone() {
		return new Variable(varID);
	}

	public GeneralFunction simplify() {
		return clone();
	}


	public boolean equalsFunction(GeneralFunction that) {
		return (that instanceof Variable variable) && (varID.equals(variable.varID));
	}

	public int compareSelf(GeneralFunction that) {
		return this.varID.compareTo(((Variable) that).varID);
	}

	public int hashCode() {
		return varID.hashCode();
	}
}
