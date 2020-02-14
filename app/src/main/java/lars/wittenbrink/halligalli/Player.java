package lars.wittenbrink.halligalli;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Player {
    private String name;
    private Stack<Card> openedStack;
    private Queue<Card> closedStack;
    private boolean onTurn;
    private boolean bot;

    public Player(String name, boolean bot) {
        this.name = name;
        this.bot = bot;
        openedStack = new Stack<>();
        closedStack = new ArrayDeque<>();
        onTurn = false;
    }

    public void move(){
        if(!closedStack.isEmpty()){
            openedStack.push(closedStack.poll());
        }
    }

    public Card getOpenedCard(){
        if(openedStack.isEmpty()) return null;
        return openedStack.peek();
    }

    public void addClosedCard(Card card){
        closedStack.add(card);
    }

    public void removeOpenedCard(){
        if(!openedStack.isEmpty()) openedStack.pop();
    }
    public void removeClosedCard(){
        if(!closedStack.isEmpty()) closedStack.remove();
    }

    public Stack<Card> getOpenedStack() {
        return openedStack;
    }
    public Queue<Card> getClosedStack() {
        return closedStack;
    }
    public void setOpenedStack(Stack<Card> openedStack) {
        this.openedStack = openedStack;
    }
    public void setClosedStack(Queue<Card> closedStack) {
        this.closedStack = closedStack;
    }

    public boolean isOnTurn() {
        return onTurn;
    }
    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
    }

    public String getName() {
        return name;
    }

    public boolean isBot() {
        return bot;
    }
}
