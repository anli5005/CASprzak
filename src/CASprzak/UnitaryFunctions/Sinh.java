package CASprzak.UnitaryFunctions;

import CASprzak.CommutativeFunctions.Multply;
import CASprzak.Function;

public class Sinh extends UnitaryFunction {

    public Sinh(Function function) {
        super(function);
    }

    @Override
    public String toString() {
        return "sinh(" + function.toString() + ")";
    }

    @Override
    public double evaluate(double[] variableValues) {
        return Math.sin(function.evaluate(variableValues));
    }

    @Override
    public Function derivative(int varID) {
        return new Multply(new Cosh(function), function.derivative(varID));

    }
}
