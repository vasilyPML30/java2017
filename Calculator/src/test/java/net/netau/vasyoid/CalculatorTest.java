package net.netau.vasyoid;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CalculatorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void evaluateTest() {
        MyStack<ExpressionEntity> stack = mock(MyStack.class);
        when(stack.pop()).thenReturn(
                new ExpressionEntity(2),
                new ExpressionEntity(1),
                new ExpressionEntity(3),
                new ExpressionEntity(3),
                new ExpressionEntity(4),
                new ExpressionEntity(9),
                new ExpressionEntity(5),
                new ExpressionEntity(2.25),
                new ExpressionEntity(-2.75)
        );
        when(stack.isEmpty()).thenReturn(true);

        List<ExpressionEntity> expression = PostfixNotationHelper.
                infixToPostfix("(1 + 2) * 3 / 4 - 5");

        Calculator calculator = new Calculator(stack);
        assertEquals((Double) (-2.75), (Double) calculator.evaluate(expression));

        InOrder checker = inOrder(stack);
        checker.verify(stack).push(new ExpressionEntity(1));
        checker.verify(stack).push(new ExpressionEntity(2));
        checker.verify(stack, times(2)).pop();
        checker.verify(stack, times(2)).push(new ExpressionEntity(3));
        checker.verify(stack, times(2)).pop();
        checker.verify(stack).push(new ExpressionEntity(9));
        checker.verify(stack).push(new ExpressionEntity(4));
        checker.verify(stack, times(2)).pop();
        checker.verify(stack).push(new ExpressionEntity(2.25));
        checker.verify(stack).push(new ExpressionEntity(5));
        checker.verify(stack, times(2)).pop();
        checker.verify(stack).push(new ExpressionEntity(-2.75));
        checker.verify(stack).pop();
        checker.verify(stack).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateNoOperandsTest() {
        Calculator calculator = new Calculator(new MyStack<>());
        calculator.evaluate(PostfixNotationHelper.infixToPostfix("+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateOneOperandTest() {
        Calculator calculator = new Calculator(new MyStack<>());
        calculator.evaluate(PostfixNotationHelper.infixToPostfix("3+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateNoOperatorTest() {
        Calculator calculator = new Calculator(new MyStack<>());
        calculator.evaluate(PostfixNotationHelper.infixToPostfix("3 4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateIncorrectBracketsTest() {
        Calculator calculator = new Calculator(new MyStack<>());
        calculator.evaluate(PostfixNotationHelper.infixToPostfix("(3 + 5))"));
    }
}