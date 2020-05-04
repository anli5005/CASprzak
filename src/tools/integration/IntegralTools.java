package tools.integration;

import functions.GeneralFunction;
import functions.commutative.Product;
import functions.special.Variable;
import tools.DefaultFunctions;
import tools.SearchTools;
import tools.helperclasses.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


public class IntegralTools {

    /**
     * Strips a {@link GeneralFunction} of any Constants and returns a {@link Pair} of the constant and the stripped GeneralFunction
     * @param function The GeneralFunction whose Constant is being Stripped
     * @return A {@link Pair} of the constant and the stripped GeneralFunction
     */
    public static Pair<GeneralFunction, GeneralFunction> stripConstants(GeneralFunction function, char varID) {
        if (function instanceof Product multiply) {
            GeneralFunction[] terms = multiply.simplifyConstants().getFunctions();
            List<GeneralFunction> constants = new ArrayList<>();
            List<GeneralFunction> termsWithConstantRemoved = new ArrayList<>(Arrays.asList(terms));
            ListIterator<GeneralFunction> iter = termsWithConstantRemoved.listIterator();
            while (iter.hasNext()) {
                GeneralFunction current = iter.next();
                if (doesNotContainsVariable(current, varID)) {
                    constants.add(current);
                    iter.remove();
                }
            }
            return new Pair<>((new Product(constants.toArray(new GeneralFunction[0]))).simplify(), (new Product(termsWithConstantRemoved.toArray(new GeneralFunction[0]))).simplifyPull().simplifyTrivialElement());
        } else {
            return new Pair<>(DefaultFunctions.ONE, function);
        }
    }

    /**
     * Returns true if the {@link Variable} is found in the {@link GeneralFunction}
     * @param function The GeneralFunction that is being searched
     * @param varID The variable ID of the variable that is being looked for
     * @return true if the {@link Variable} is found in the {@link GeneralFunction}
     */
    public static boolean doesNotContainsVariable(GeneralFunction function, char varID) {
        Variable variable = new Variable(varID);
        return !SearchTools.existsAny(function, variable::equals);
    }
}