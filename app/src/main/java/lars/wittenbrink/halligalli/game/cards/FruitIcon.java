package lars.wittenbrink.halligalli.game.cards;

import lars.wittenbrink.halligalli.R;

public enum FruitIcon {
    ICONA(R.attr.iconA), ICONB(R.attr.iconB), ICONC(R.attr.iconC), ICOND(R.attr.iconD);

    private final int id;

    FruitIcon(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}