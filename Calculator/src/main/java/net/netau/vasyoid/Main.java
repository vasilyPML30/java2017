package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * The application takes an arithmetic expression, converts it into postfix notation
 * and prints the converted expression and the result.
 */
public class Main {

    public static void main(@NotNull String[] args) {
        if (args.length < 1) {
            System.out.println("illegal arguments");
            System.exit(-1);
        }
        try {
            List<ExpressionEntity> expression = PostfixNotationHelper.infixToPostfix(String.join(" ", args));
            Calculator calculator = new Calculator(new MyStack<>());
            double result = calculator.evaluate(expression);
            System.out.println(PostfixNotationHelper.expressionToString(expression));
            System.out.println(result);
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
