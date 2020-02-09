package lars.wittenbrink.halligalli;

public enum FruitIcon {
    ICONA(R.attr.iconA), ICONB(R.attr.iconB), ICONC(R.attr.iconC), ICOND(R.attr.iconD);

    private final long id;

    FruitIcon(int id) {
        this.id = id;
    }

    public long getValue() {
        return id;
    }
}