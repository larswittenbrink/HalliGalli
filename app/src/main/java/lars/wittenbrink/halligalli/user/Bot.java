package lars.wittenbrink.halligalli.user;

public class Bot extends User implements IBot{

    private int difficulty;

    public Bot(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
    }

    @Override
    public int getDifficulty() {
        return 0;
    }
}
