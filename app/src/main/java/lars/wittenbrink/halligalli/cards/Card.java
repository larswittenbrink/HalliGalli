package lars.wittenbrink.halligalli.cards;

public final class Card {
    private FruitIcon fruitIcon;
    private FruitNumber fruitNumber;

    public Card(FruitIcon fruitIcon, FruitNumber fruitNumber) {
        this.fruitIcon = fruitIcon;
        this.fruitNumber = fruitNumber;
    }

    public FruitIcon getFruitIcon() {
        return fruitIcon;
    }

    public FruitNumber getFruitNumber() {
        return fruitNumber;
    }
}
