package lars.wittenbrink.halligalli.game.user;

interface IBot extends IUser {
    int getDifficulty();
    void moveMethod();
    void pressMethod();
}
