package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.List;

import static net.netau.vasyoid.ExpressionEntity.EntityType.VALUE;

/**
 * This class calculates the value of an expression in postfix notation.
 */
public class Calculator {

    private MyStack<ExpressionEntity> stack;

    Calculator(@NotNull MyStack<ExpressionEntity> stack) {
        this.stack = stack;
    }

    /**
     * Evaluates an expression value.
     * @param expression expression to evaluate.
     * @return the result of evaluation.
     * @throws InvalidParameterException if given expression is incorrect.
     */
    double evaluate(@NotNull List<ExpressionEntity> expression) throws InvalidParameterException {
        double result;
        try {
            expression.forEach(entity -> {
                if (entity.getType() == VALUE) {
                    stack.push(entity);
                } else {
                    ExpressionEntity b = stack.pop();
                    ExpressionEntity a = stack.pop();
                    stack.push(entity.apply(a, b));
                }
            });
            result = stack.pop().getValue();
            if (!stack.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new InvalidParameterException("Expression format is invalid");
        }
        return result;
    }
}
