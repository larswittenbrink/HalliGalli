package lars.wittenbrink.halligalli.user;

interface IBot extends IUser {
    int getDifficulty();
    Runnable getMoveThread();
    Thread getPressThread();
    void moveMethod();
    void pressMethod();
}
