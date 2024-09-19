package model;

public enum OrderSide {
    BUY(0),
    SELL(1);

    private final int value;

    OrderSide(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderSide fromValue(int value) {
        if (value == 0) {
            return BUY;
        } else if (value == 1) {
            return SELL;
        } else {
            throw new IllegalArgumentException("Invalid OrderSide value: " + value);
        }
    }
}

