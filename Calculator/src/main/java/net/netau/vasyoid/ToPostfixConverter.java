package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static net.netau.vasyoid.ExpressionEntity.EntityType.*;

/**
 * This class is used to convert an expression in infix notation represented by a String
 * into an expression in postfix notation represented by a list of ExpressionEntry-s.
 */
public class ToPostfixConverter {

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
     * @param expression expression to convert.
     * @return resulting expression.
     * @throws InvalidParameterException if given expression is incorrect.
     */
    public static List<ExpressionEntity> convert(String expression)
            throws InvalidParameterException {
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
                        ExpressionEntity value = new ExpressionEntity(VALUE);
                        value.setValue(Double.parseDouble(curToken));
                        result.add(value);
                        break;
                }
            }
            if (!stack.isEmpty()) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            throw new InvalidParameterException("Expression format is invalid");
        }
        return result;
    }
}
