package lars.wittenbrink.halligalli.user;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import lars.wittenbrink.halligalli.cards.Card;

public class User implements IUser {

    private String name;
    private Stack<Card> openedCards;
    private Queue<Card> closedCards;

    public User(String name) {
        this.name = name;
        this.openedCards = new Stack<>();
        this.closedCards = new ArrayDeque<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stack<Card> getOpenedCards() {
        return openedCards;
    }

    @Override
    public Queue<Card> getClosedCards() {
        return closedCards;
    }
}
