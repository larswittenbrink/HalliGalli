package lars.wittenbrink.halligalli;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Player {
    private String name;
    private Stack<Card> openedStack;
    private Queue<Card> closedStack;
    private boolean onTurn;

    public Player(String name) {
        this.name = name;
        openedStack = new Stack<>();
        closedStack = new LinkedList<>();
        onTurn = false;
    }

    public Card getOpenedCard(){
        if(openedStack.isEmpty()) return null;
        return openedStack.peek();
    }
    public Card getClosedCard(){
        if(closedStack.isEmpty()) return null;
        return closedStack.peek();
    }

    public void addClosedCard(Card card){
        closedStack.add(card);
    }
    public void addOpenedCard(Card card){
        openedStack.push(card);
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

    public boolean isOnTurn() {
        return onTurn;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
    }
}
