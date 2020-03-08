package lars.wittenbrink.halligalli.user;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lars.wittenbrink.halligalli.GameController;

public class Bot extends User implements IBot{

    private int difficulty;
    private Runnable moveThread;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Thread pressThread;
    private GameController gameController;


    public Bot(String name, final int difficulty) {
        super(name);
        this.difficulty = difficulty;


        moveThread = new Runnable() {
            @Override
            public void run() {
                moveMethod();
            }
        };

        pressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                pressMethod();
            }
        });
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public Runnable getMoveThread() {
        return moveThread;
    }

    @Override
    public Thread getPressThread() {
        return pressThread;
    }

    @Override
    public void moveMethod() {
        try {
            Thread.sleep((Math.abs(difficulty-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GameController.INSTANCE.move(this);
    }

    @Override
    public void pressMethod() {
        try {
            Thread.sleep((Math.abs(difficulty-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(gameController.fiveFruitsOpen() || randomBoolean(Math.abs(difficulty-100)/100)){
            GameController.INSTANCE.press(this);
        }
    }


    public static boolean randomBoolean(float f){
        return new Random().nextFloat() < f;
    }
}
