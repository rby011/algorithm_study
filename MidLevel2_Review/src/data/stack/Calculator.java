package data.stack;

import java.util.Scanner;

/**
 * OTHER OPERATORS & FLOATING POINTS
 */
public class Calculator {
	static final char[] OPERATOR = { '-', '+', '0', '*', '/' };
	static final char EOF = 'E';

	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);
		String expr_str = scan.next();
		scan.close();

		// COVERT IN-ORDER EXPR INTO POST-ORDER EXPR
		Stack rstack = convertToPostOrder(expr_str);
		if (rstack != null) {
			rstack.printstack();
			// CALCULATE
			int result = calculate(rstack);
			System.out.println(" = " + result);
		} else {
			System.out.println("GRAMMAR ERROR");
		}

	}

	private static int calculate(Stack stack) throws Exception {
		Stack istack = new Stack(stack.size);

		while (!stack.isempty()) {
			char expr = stack.pop();
			if (isDigit(expr)) {
				istack.push(expr);
			} else if (isOperator(expr)) {
				int oprd1 = istack.pop() - '0';
				int oprd2 = istack.pop() - '0';
				int result = 0;
				if (expr == '-') {
					result = oprd2 - oprd1;
				} else if (expr == '+') {
					result = oprd2 + oprd1;
				} else if (expr == '*') {
					result = oprd2 * oprd1;
				} else if (expr == '/') {
					result = oprd2 / oprd1;
				}
				istack.push((char) (result + '0'));
			}
		}

		return istack.pop() - '0';
	}

	private static Stack convertToPostOrder(String expStr) throws Exception {
		char expr[] = (expStr + "E").toCharArray();

		Stack istack = new Stack(expr.length);
		Stack rstack = new Stack(expr.length);

		int opencnt = 0;

		for (int e = 0; e < expr.length; e++) {
			if (isDigit(expr[e])) {
				istack.push(expr[e]);
			} else if (isOpenParenthis(expr[e])) {
				rstack.push(expr[e]);
				opencnt = opencnt + 1;
			} else if (isCloseParenthis(expr[e])) {
				boolean matched = false;
				while (!rstack.isempty()) {
					char iexpr = rstack.pop();
					if (!isOpenParenthis(iexpr)) {
						istack.push(iexpr);
					} else {
						matched = true;
						break;
					}
				}
				opencnt = opencnt - 1;
				if (!matched) {
					// ERROR! NOT MATCHED PARENTHIS
					return null;
				}
			} else if (isOperator(expr[e])) {
				if (!rstack.isempty() && !isOpenParenthis(rstack.peek())) {
					while (!rstack.isempty() && isHigher(rstack.peek(), expr[e])) {
						istack.push(rstack.pop());
					}
				}
				rstack.push(expr[e]);
			} else if (isEOF(expr[e])) {
				while (!istack.isempty())
					rstack.push(istack.pop());
			} else {
				// ERROR! UNDEFINED EXPRESSION
				return null;
			}
		}

		if (opencnt == 0)
			return rstack;
		else
			return null;
	}

	private static boolean isEOF(char expr) {
		return expr == EOF;
	}

	private static boolean isHigher(char expr1, char expr2) throws Exception {
		int weight1 = -1, weight2 = -1;
		for (int o = 0; o < OPERATOR.length; o++) {
			if (OPERATOR[o] == expr1) {
				weight1 = o;
			}
			if (OPERATOR[o] == expr2) {
				weight2 = o;
			}
		}

		if (weight1 == -1 || weight2 == -1)
			return false;

		return weight1 - weight2 > 1;
	}

	private static boolean isOperator(char expr) {
		for (int o = 0; o < OPERATOR.length; o++) {
			if (expr == OPERATOR[o]) {
				return true;
			}
		}
		return false;
	}

	private static boolean isDigit(char expr) {
		if (expr >= '0' && expr <= '9') {
			return true;
		}
		return false;
	}

	private static boolean isOpenParenthis(char expr) {
		return expr == '(';
	}

	private static boolean isCloseParenthis(char expr) {
		return expr == ')';
	}

	static class Stack {
		char exprs[] = null;
		int size = 0;

		Stack(int capacity) {
			this.exprs = new char[capacity];
		}

		void push(char expr) throws Exception {
			if (isfull())
				throw new Exception("Overflow");
			exprs[size++] = expr;
		}

		char pop() throws Exception {
			if (isempty())
				throw new Exception("Underflow");
			return exprs[(size--) - 1];
		}

		char peek() throws Exception {
			if (isempty())
				throw new Exception("Underflow");
			return exprs[size - 1];
		}

		boolean isempty() {
			return size == 0;
		}

		boolean isfull() {
			return size == exprs.length - 1;
		}

		void printstack() {
			for (int s = size - 1; s >= 0; s--) {
				System.out.print(exprs[s]);
			}
			System.out.println();
		}

	}

	static class Expression {
		boolean isDigit;
		double digit;
		char operator;

		Expression(double digit) {
			this.isDigit = true;
			this.digit = digit;
		}

		Expression(char operator) {
			this.isDigit = false;
			this.operator = operator;
		}

	}
}
