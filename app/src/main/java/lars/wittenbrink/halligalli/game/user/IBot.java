package lars.wittenbrink.halligalli.game.user;

interface IBot extends IUser {
    int getDifficulty();
    void move();
    void press();
}
