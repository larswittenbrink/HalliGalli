package lars.wittenbrink.halligalli.game.user;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lars.wittenbrink.halligalli.game.GameController;

public class Bot extends User implements IBot{

    private int difficulty;
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()/2);
    private GameController gameController;

    public Bot(String name, final int difficulty) {
        super(name);
        this.difficulty = difficulty;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public void move() {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            GameController.INSTANCE.move(this);
        }, getMoveDelay(difficulty), TimeUnit.MILLISECONDS);
    }

    @Override
    public void press() {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            if(GameController.INSTANCE.fiveFruitsOpen() || !randomBoolean(difficulty/100)){
                GameController.INSTANCE.press(this);
            }
        }, getPressDelay(difficulty), TimeUnit.MILLISECONDS);
    }

    public static long getMoveDelay(int difficulty){
        return (Math.abs(difficulty-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100));
    }

    public static long getPressDelay(int difficulty){
        return (Math.abs(difficulty-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100));
    }

    public static boolean randomBoolean(float f){
        return new Random().nextFloat() < f;
    }
}
