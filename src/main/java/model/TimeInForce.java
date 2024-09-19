package model;

public enum TimeInForce {
    DAY('D'),
    GOOD_TILL_CANCEL('G'),
    IMMEDIATE_OR_CANCEL('I'),
    FILL_OR_KILL('F');

    private final char value;

    TimeInForce(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public static TimeInForce fromValue(char value) {
        switch (value) {
            case 'D':
                return DAY;
            case 'G':
                return GOOD_TILL_CANCEL;
            case 'I':
                return IMMEDIATE_OR_CANCEL;
            case 'F':
                return FILL_OR_KILL;
            default:
                throw new IllegalArgumentException("Invalid TimeInForce value: " + value);
        }
    }
}

