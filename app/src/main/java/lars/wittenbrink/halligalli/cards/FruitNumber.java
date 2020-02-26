package lars.wittenbrink.halligalli.cards;

public enum FruitNumber {
    ONE(1, 5), TWO(2, 3), THREE(3, 3), FOUR(4, 2), FIVE(5, 1);

    private final int value;
    private final int number;

    FruitNumber(int value, int number) {
        this.value = value;
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }
}
