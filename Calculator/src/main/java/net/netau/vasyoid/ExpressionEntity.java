package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * An entity of an arithmetic expression.
 * It may be a value, a bracket or a binary operator (+, -, *, /).
 */
public class ExpressionEntity {

    private EntityType type;
    private double value;

    ExpressionEntity(EntityType type) {
        this.type = type;
    }

    @NotNull
    public EntityType getType() {
        return type;
    }

    /**
     * Gets priority of an operator.
     * This method is not supported by brackets and values.
     * @return the priority of the operator.
     * @throws UnsupportedOperationException if called not on an operator.
     */
    public int getPriority() throws UnsupportedOperationException {
        return type.getPriority();
    }

    /**
     * Applies an operator to two values.
     * This method is not supported by brackets and values
     * and can be applied only to entities of type VALUE.
     * @param l left operand.
     * @param r right operand.
     * @return the result of the application.
     * @throws UnsupportedOperationException if called not on an operator.
     * @throws IllegalArgumentException if applied not to values.
     */
    @NotNull
    public ExpressionEntity apply(@NotNull ExpressionEntity l,
                                  @NotNull ExpressionEntity r)
            throws UnsupportedOperationException, IllegalArgumentException {
        return type.apply(l, r);
    }

    /**
     * Sets the entity value.
     * This method is supported only by entities of type VALUE.
     * @param value value to set.
     * @throws UnsupportedOperationException if called not for a VALUE.
     */
    public void setValue(double value) throws UnsupportedOperationException {
        if (type != EntityType.VALUE) {
            throw new UnsupportedOperationException("this entity does not have a value");
        } else {
            this.value = value;
        }
    }

    /**
     * Gets the entity value.
     * This method is supported only by entities of type VALUE.
     * @return value of the entity.
     * @throws UnsupportedOperationException if called not for a VALUE.
     */
    public double getValue() throws UnsupportedOperationException {
        if (type != EntityType.VALUE) {
            throw new UnsupportedOperationException("this entity does not have a value");
        } else {
            return value;
        }
    }

    @Override
    @NotNull
    public String toString() {
        if (type == EntityType.VALUE) {
            return String.valueOf(getValue());
        }
        return type.toString();
    }

    public enum EntityType {

        /**
         * Binary operator '+'
         */
        PLUS(1) {

            @NotNull
            @Override
            protected ExpressionEntity apply(@NotNull ExpressionEntity l,
                                          @NotNull ExpressionEntity r)
                    throws IllegalArgumentException {
                checkArguments(l, r);
                ExpressionEntity result = new ExpressionEntity(VALUE);
                result.setValue(l.getValue() + r.getValue());
                return result;
            }

            @Override
            public String toString() {
                return "+";
            }
        },

        /**
         * Binary operator '-'
         */
        MINUS(1) {

            @NotNull
            @Override
            protected ExpressionEntity apply(@NotNull ExpressionEntity l,
                                          @NotNull ExpressionEntity r)
                    throws IllegalArgumentException {
                checkArguments(l, r);
                ExpressionEntity result = new ExpressionEntity(VALUE);
                result.setValue(l.getValue() - r.getValue());
                return result;
            }

            @Override
            public String toString() {
                return "-";
            }
        },

        /**
         * Binary operator '*'
         */
        MULT(2) {

            @NotNull
            @Override
            protected ExpressionEntity apply(@NotNull ExpressionEntity l,
                                          @NotNull ExpressionEntity r)
                    throws IllegalArgumentException {
                checkArguments(l, r);
                ExpressionEntity result = new ExpressionEntity(VALUE);
                result.setValue(l.getValue() * r.getValue());
                return result;
            }

            @Override
            public String toString() {
                return "*";
            }
        },

        /**
         * Binary operator '/'
         */
        DIV(2) {

            @NotNull
            @Override
            protected ExpressionEntity apply(@NotNull ExpressionEntity l,
                                          @NotNull ExpressionEntity r)
                    throws IllegalArgumentException {
                checkArguments(l, r);
                ExpressionEntity result = new ExpressionEntity(VALUE);
                result.setValue(l.getValue() / r.getValue());
                return result;
            }

            @Override
            public String toString() {
                return "/";
            }
        },

        /**
         * Left bracket '('
         */
        LEFT_BRACKET(0),

        /**
         * Right bracket '('
         */
        RIGHT_BRACKET(-1),

        /**
         * A double value
         */
        VALUE(-1);

        private final int priority;

        EntityType(int priority) {
            this.priority = priority;
        }

        private int getPriority() {
            if (priority < 0) {
                throw new UnsupportedOperationException("this entity does not have a priority");
            }
            return priority;
        }

        @NotNull
        protected ExpressionEntity apply(@NotNull ExpressionEntity l,
                                      @NotNull ExpressionEntity r) {
            throw new UnsupportedOperationException("this entity is inapplicable");
        }

        protected void checkArguments(@NotNull ExpressionEntity l,
                                    @NotNull ExpressionEntity r) {
            if (l.getType() != VALUE || r.getType() != VALUE) {
                throw new IllegalArgumentException("an operator can be applied to values only");
            }
        }
    }
}
