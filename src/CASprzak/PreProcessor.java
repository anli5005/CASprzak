package CASprzak;

import CASprzak.SpecialFunctions.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class PreProcessor {
	public static final String[] operations = {"^", "*", "/", "+", "-", "logb", "log", "ln", "sqrt", "exp", "sinh", "cosh", "tanh"};
	public static final String[] operationsTrig = {"sin", "cos", "tan", "csc", "sec", "cot", "asin", "acos", "atan"};


	private int getPrecedence(String input) {
		switch (input) {
			case "^":
				return 4;
			case "*":
			case "/":
				return 3;
			case "+":
				return 2;
			case "(":
				return 0;
			default:
				return 5;
		}
	}

	public boolean isAnOperator(String input) {
		for (String x : operations) {
			if (x.equals(input)) return true;
		}
		for (String x : operationsTrig) {
			if (x.equals(input)) return true;
		}
		return false;
	}

	public String[] toPostfix(String infix) {
		infix = infix.replaceAll("(?<![\\^\\-+*/ ])\\s*-","+-");
		String[] tokens = infix.split("\\s+|(((?<=\\W)(?=[\\w-])((?<!-)|(?!\\d))|(?<=\\w)(?=\\W))|(?<=[()])|(?=[()]))(?<![ .])(?![ .])");
		System.out.println(Arrays.toString(tokens));
		ArrayList<String> postfix = new ArrayList<>();
		Stack<String> operators = new Stack<>();

		for (String token : tokens) {
//			System.out.println(token);
//			System.out.println(operators.toString());
			if (Constant.isSpecialConstant(token)) {
				postfix.add(token);
			} else if (isAnOperator(token)) {
				if (operators.empty()) {
					operators.push(token);
				} else if (getPrecedence(token) <= getPrecedence(operators.peek()) && !token.equals("^")) {
					postfix.add(operators.pop());
					operators.push(token);
				} else {
					operators.push(token);
				}
			} else if (token.equals("(")) {
				operators.push(token);
			} else if (token.equals(")")) {
				while (!operators.peek().equals("(")) {
					postfix.add(operators.pop());
				}
				operators.pop();
			} else {
				postfix.add(token);
			}
		}

		while (operators.size() != 0) {
			postfix.add(operators.pop());
		}

		return postfix.toArray(new String[0]);
	}
}
