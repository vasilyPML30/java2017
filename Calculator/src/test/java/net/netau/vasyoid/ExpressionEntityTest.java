package net.netau.vasyoid;

import org.junit.Test;

import static net.netau.vasyoid.ExpressionEntity.EntityType.*;
import static org.junit.Assert.*;

public class ExpressionEntityTest {

    @Test
    public void getTypeTest() {
        assertEquals(VALUE, new ExpressionEntity(0).getType());
        assertEquals(PLUS, new ExpressionEntity(PLUS).getType());
        assertEquals(MINUS, new ExpressionEntity(MINUS).getType());
        assertEquals(MULT, new ExpressionEntity(MULT).getType());
        assertEquals(DIV, new ExpressionEntity(DIV).getType());
        assertEquals(LEFT_BRACKET, new ExpressionEntity(LEFT_BRACKET).getType());
        assertEquals(RIGHT_BRACKET, new ExpressionEntity(RIGHT_BRACKET).getType());
    }

    @Test
    public void getPriorityTest() {
        assertEquals(1, new ExpressionEntity(PLUS).getPriority());
        assertEquals(1, new ExpressionEntity(MINUS).getPriority());
        assertEquals(2, new ExpressionEntity(MULT).getPriority());
        assertEquals(2, new ExpressionEntity(DIV).getPriority());
        assertEquals(0, new ExpressionEntity(LEFT_BRACKET).getPriority());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getPriorityUnsupportedOperationTest() {
        new ExpressionEntity(0).getPriority();
        new ExpressionEntity(RIGHT_BRACKET).getPriority();
    }

    @Test
    public void applyTest() {
        ExpressionEntity one = new ExpressionEntity(1);
        ExpressionEntity two = new ExpressionEntity(2);
        ExpressionEntity plus = new ExpressionEntity(PLUS);
        ExpressionEntity minus = new ExpressionEntity(MINUS);
        ExpressionEntity mult = new ExpressionEntity(MULT);
        ExpressionEntity div = new ExpressionEntity(DIV);
        assertEquals((Double) (1.0 + 2.0), (Double) plus.apply(one, two).getValue());
        assertEquals((Double) (1.0 - 2.0), (Double) minus.apply(one, two).getValue());
        assertEquals((Double) (1.0 * 2.0), (Double) mult.apply(one, two).getValue());
        assertEquals((Double) (1.0 / 2.0), (Double) div.apply(one, two).getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void applyUnsupportedOperationValueTest() {
        ExpressionEntity value = new ExpressionEntity(0);
        value.apply(value, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void applyUnsupportedOperationLeftBracketTest() {
        ExpressionEntity value = new ExpressionEntity(0);
        ExpressionEntity bracket = new ExpressionEntity(LEFT_BRACKET);
        bracket.apply(value, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void applyUnsupportedOperationRightBracketTest() {
        ExpressionEntity value = new ExpressionEntity(0);
        ExpressionEntity bracket = new ExpressionEntity(RIGHT_BRACKET);
        bracket.apply(value, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyIllegalArgumentTest() {
        ExpressionEntity bracket = new ExpressionEntity(LEFT_BRACKET);
        ExpressionEntity value = new ExpressionEntity(0);
        ExpressionEntity plus = new ExpressionEntity(PLUS);
        plus.apply(bracket, value);
    }

        @Test
    public void getValueTest() {
        assertEquals((Double) 12.34, (Double) new ExpressionEntity(12.34).getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueUnsupportedOperationOperatorTest() {
        new ExpressionEntity(PLUS).getValue();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueUnsupportedOperationBracketTest() {
        new ExpressionEntity(LEFT_BRACKET).getValue();
    }

    @Test
    public void toStringTest() {
        assertEquals("12.34", new ExpressionEntity(12.34).toString());
        assertEquals("+", new ExpressionEntity(PLUS).toString());
        assertEquals("-", new ExpressionEntity(MINUS).toString());
        assertEquals("*", new ExpressionEntity(MULT).toString());
        assertEquals("/", new ExpressionEntity(DIV).toString());
    }

    @Test
    public void equalsTest() {
        assertEquals(new ExpressionEntity(2), new ExpressionEntity(2));
        assertNotEquals(new ExpressionEntity(3), new ExpressionEntity(2));
        assertEquals(new ExpressionEntity(LEFT_BRACKET),
                new ExpressionEntity(LEFT_BRACKET));
        assertNotEquals(new ExpressionEntity(LEFT_BRACKET),
                new ExpressionEntity(RIGHT_BRACKET));
        assertNotEquals(new ExpressionEntity(LEFT_BRACKET),
                new ExpressionEntity(2));
        assertNotEquals(new ExpressionEntity(2), 2.0);
    }

    }