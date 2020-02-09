package lars.wittenbrink.halligalli;

public enum FruitNumber {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    FruitNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
