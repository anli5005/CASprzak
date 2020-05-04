package functions;

import config.Settings;
import functions.binary.Logb;
import functions.binary.Pow;
import functions.commutative.Sum;
import functions.commutative.Product;
import functions.special.Constant;
import functions.special.Variable;
import functions.unitary.UnitaryFunction;
import org.jetbrains.annotations.NotNull;
import tools.MiscTools;
import functions.unitary.transforms.Integral;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class GeneralFunction implements Evaluable, Differentiable, Simplifiable, Comparable<GeneralFunction>, Iterable<GeneralFunction> {

	/**
	 * Describes the order that a {@link GeneralFunction} should appear in a sorted array (used in {@link #compareTo(GeneralFunction)})
	 */
	@SuppressWarnings("ClassReferencesSubclass")
	public static final Class<?>[] sortOrder = {
			Constant.class, // Must always be first
			Variable.class,
			Product.class,
			Pow.class,
			Logb.class,
			UnitaryFunction.class,
			Sum.class,
			Integral.class,//TODO integral is part of Unitary Function
	};

	/**
	 * Caches derivatives with the key corresponding to the varID of the derivative
	 */
	protected final HashMap<Character, GeneralFunction> derivatives = new HashMap<>();

	/**
	 * Returns a String representation of the GeneralFunction
	 * @return String representation of function
	 */
	public abstract String toString();

	/**
	 * Returns a clone of the {@link GeneralFunction}
	 * @return a clone of the GeneralFunction
	 */
	public abstract GeneralFunction clone();

	/**
	 * Simplifies a {@link GeneralFunction} multiple times
	 * @param times the amount of times it is simplified
	 * @return the simplified {@link GeneralFunction}
	 */
	public GeneralFunction simplifyTimes(int times) {
		GeneralFunction newFunction, currentFunction = this;
		for (int i = 0; i < times; i++) {
			newFunction = currentFunction.simplify();
			if (newFunction.toString().equals(currentFunction.toString()))
				return newFunction;
			currentFunction = newFunction;
		}
		return currentFunction;
	}

	/**
	 * Returns the derivative of the function, simplified
	 * @param varID the ID of the variable being differentiated
	 * @return the derivative of the {@link GeneralFunction} it is called on, simplified
	 */
	public GeneralFunction getSimplifiedDerivative(char varID) {
		if (Settings.cacheDerivatives && derivatives.containsKey(varID))
			return derivatives.get(varID);
		GeneralFunction derivative = getDerivative(varID).simplify();
		if (Settings.cacheDerivatives)
			derivatives.put(varID, derivative);
		return derivative;
	}


	/**
	 * Returns the Nth derivative of the function, simplified
	 * @param varID the ID of the variable being differentiated
	 * @param N     the amount of times to differentiate
	 * @return the Nth derivative of the {@link GeneralFunction} it is called on, simplified
	 */
	public GeneralFunction getNthDerivative(char varID, int N) {
		GeneralFunction currentFunction = this;
		while (N > 0) {
			currentFunction = currentFunction.getSimplifiedDerivative(varID);
			N--;
		}
		return currentFunction;
	}

	/**
	 * Returns the value of the derivative at point
	 * @param varID the ID of the variable being differentiated
	 * @param point the point to find the derivative at
	 * @return the value of the derivative at point
	 */
	@SuppressWarnings("unused")
	public double derivativeAt(char varID, Map<Character, Double> point) {
		return getDerivative(varID).evaluate(point);
	}

	/**
	 * Replaces every GeneralFunction that satisfies the predicate using the action specified by the replacer
	 * @param test checks if the function should be replaced
	 * @param replacer replaces the function
	 * @return a new function with all replacements made
	 */
	public abstract GeneralFunction substituteAll(Predicate<? super GeneralFunction> test, Function<? super GeneralFunction, ? extends GeneralFunction> replacer);

	/**
	 * Substitutes a new {@link GeneralFunction} into a variable
	 * @param varID     the variable to be substituted into
	 * @param toReplace the {@link GeneralFunction} that will be substituted
	 * @return the new {@link GeneralFunction} after all substitutions are preformed
	 */
	public GeneralFunction substituteVariable(char varID, GeneralFunction toReplace) {
		return substituteAll(f -> (f instanceof Variable v && v.varID == varID), f -> toReplace);
	}

	/**
	 * Fixes some variables to the values given in the map by substituting in a {@link Constant} for those {@link Variable}s
	 * @param values the map defining the substitutions to be made
	 * @return a new function with the substitutions made
	 */
	public GeneralFunction setVariables(Map<Character, Double> values) {
		GeneralFunction current = this;
		for (Map.Entry<Character, Double> entry : values.entrySet())
			current = current.substituteVariable(entry.getKey(), new Constant(entry.getValue()));
		return current;
	}


	/**
	 * Returns true when the two functions simplified are equal
	 * @param that The {@link GeneralFunction} that the current function is being checked equal to
	 * @return true when the two functions are equal
	 */
	public abstract boolean equalsFunction(GeneralFunction that);

	/**
	 * Simplifies the two functions, then compares them with {@link #equalsFunction(GeneralFunction)}
	 * @param that the object compared to
	 * @return true if they're equal
	 */
	public boolean equals(Object that) {
		if (!(that instanceof GeneralFunction))
			return false;
		return this.simplify().equalsFunction(((GeneralFunction) that).simplify());
	}

	/**
	 * Used internally for comparing two functions of **the same exact type**
	 * @param that the {@link GeneralFunction} compared to
	 * @return comparison
	 */
	protected abstract int compareSelf(GeneralFunction that);

	/**
	 * Two different GeneralFunction types are sorted according to {@link #sortOrder} and {@link MiscTools#findClassValue(GeneralFunction)}, and same types are sorted using {@link #compareSelf(GeneralFunction)}
	 * @param that the {@link GeneralFunction} compared to
	 * @return comparison
	 */
	public int compareTo(@NotNull GeneralFunction that) {
		if (this.equalsFunction(that))
			return 0;
		else if (this.getClass().equals(that.getClass()))
			return compareSelf(that);
		else
			return (MiscTools.findClassValue(this) - MiscTools.findClassValue(that));
	}
}