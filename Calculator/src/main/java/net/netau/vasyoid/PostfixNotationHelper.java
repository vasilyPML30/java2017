package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static net.netau.vasyoid.ExpressionEntity.EntityType.*;

/**
 * This class is used to infixToPostfix an expression in infix notation represented by a String
 * into an expression in postfix notation represented by a list of ExpressionEntry-s.
 */
public class PostfixNotationHelper {

    private static void addOperator(@NotNull ExpressionEntity operator,
                             @NotNull MyStack<ExpressionEntity> stack,
                             @NotNull List<ExpressionEntity> result) {
        while (!stack.isEmpty() && operator.getPriority() <= stack.peek().getPriority()) {
            result.add(stack.pop());
        }
        stack.push(operator);
    }

    /**
     * Converts an expression into postfix notation.
     * @param expression expression to infixToPostfix.
     * @return resulting expression.
     * @throws IllegalArgumentException if given expression is incorrect.
     */
    public static List<ExpressionEntity> infixToPostfix(String expression)
            throws IllegalArgumentException {
        List<ExpressionEntity> result = new ArrayList<>();
        MyStack<ExpressionEntity> stack = new MyStack<>();
        stack.push(new ExpressionEntity(LEFT_BRACKET));
        expression += ')';
        StringTokenizer tokenizer = new StringTokenizer(expression, " ()+-*/", true);
        try {
            while (tokenizer.hasMoreTokens()) {
                String curToken = tokenizer.nextToken();
                switch (curToken) {
                    case " ":
                        break;
                    case "(":
                        stack.push(new ExpressionEntity(LEFT_BRACKET));
                        break;
                    case ")":
                        while (stack.peek().getType() != LEFT_BRACKET) {
                            result.add(stack.pop());
                        }
                        stack.pop();
                        break;
                    case "+":
                        addOperator(new ExpressionEntity(PLUS), stack, result);
                        break;
                    case "-":
                        addOperator(new ExpressionEntity(MINUS), stack, result);
                        break;
                    case "*":
                        addOperator(new ExpressionEntity(MULT), stack, result);
                        break;
                    case "/":
                        addOperator(new ExpressionEntity(DIV), stack, result);
                        break;
                    default:
                        result.add(new ExpressionEntity(Double.parseDouble(curToken)));
                        break;
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Expression format is invalid");
        }
        return result;
    }

    /**
     * Takes a list containing an expression in postfix notation and converts it into String.
     * @param expression expression to print.
     * @return resulting string.
     */
    @NotNull
    public static String expressionToString(@NotNull List<ExpressionEntity> expression) {
        return expression.stream()
                .map(entity -> entity.toString() + " ")
                .collect(Collectors.joining());
    }
}
