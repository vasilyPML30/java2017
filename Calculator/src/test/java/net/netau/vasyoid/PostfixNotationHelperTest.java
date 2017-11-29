package net.netau.vasyoid;

import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static net.netau.vasyoid.ExpressionEntity.EntityType.*;
import static org.junit.Assert.*;
public class PostfixNotationHelperTest {

    @Test
    public void expressionToStringTest() {
        List<ExpressionEntity> expression = new ArrayList<>();
        expression.add(new ExpressionEntity(1));
        expression.add(new ExpressionEntity(2));
        expression.add(new ExpressionEntity(PLUS));
        expression.add(new ExpressionEntity(3));
        expression.add(new ExpressionEntity(MULT));
        expression.add(new ExpressionEntity(4));
        expression.add(new ExpressionEntity(DIV));
        expression.add(new ExpressionEntity(5));
        expression.add(new ExpressionEntity(MINUS));
        assertEquals("1.0 2.0 + 3.0 * 4.0 / 5.0 - ",
                PostfixNotationHelper.expressionToString(expression));
    }

    @Test
    public void infixToPostfixTest() {
        List<ExpressionEntity> expected = new ArrayList<>();
        expected.add(new ExpressionEntity(1));
        expected.add(new ExpressionEntity(2));
        expected.add(new ExpressionEntity(PLUS));
        expected.add(new ExpressionEntity(3));
        expected.add(new ExpressionEntity(MULT));
        expected.add(new ExpressionEntity(4));
        expected.add(new ExpressionEntity(DIV));
        expected.add(new ExpressionEntity(5));
        expected.add(new ExpressionEntity(MINUS));
        assertEquals(PostfixNotationHelper.expressionToString(expected),
                PostfixNotationHelper.expressionToString(
                        PostfixNotationHelper.infixToPostfix("(1 + 2) * 3 / 4 - 5")));
    }

    @Test(expected = InvalidParameterException.class)
    public void invalidBracketsTest() {
        PostfixNotationHelper.infixToPostfix("2+3)");
    }
}