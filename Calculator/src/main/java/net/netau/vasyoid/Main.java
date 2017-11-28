package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * The application takes an arithmetic expression, converts it into postfix notation
 * and prints the converted expression and the result.
 */
public class Main {

    /**
     * Takes a list containing an expression in postfix notation and prints it.
     * @param expression expression to print
     */
    public static void printPostfix(@NotNull List<ExpressionEntity> expression) {
        expression.forEach(e -> System.out.print(e.toString() + " "));
        System.out.println();
    }

    public static void main(@NotNull String[] args) {
        if (args.length < 1) {
            System.out.println("illegal arguments");
            System.exit(-1);
        }
        try {
            List<ExpressionEntity> expression = ToPostfixConverter.convert(String.join(" ", args));
            Calculator calculator = new Calculator(new MyStack<>());
            double result = calculator.evaluate(expression);
            printPostfix(expression);
            System.out.println(result);
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
