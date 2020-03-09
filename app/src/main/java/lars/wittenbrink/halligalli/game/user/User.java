package lars.wittenbrink.halligalli.game.user;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Stack;

import lars.wittenbrink.halligalli.game.cards.Card;

public class User implements IUser {

    private String name;
    private Deque<Card> openedCards;
    private Deque<Card> closedCards;

    public User(String name) {
        this.name = name;
        this.openedCards = new ArrayDeque<>();
        this.closedCards = new ArrayDeque<>();
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Deque<Card> getOpenedCards() {
        return openedCards;
    }

    @Override
    public Deque<Card> getClosedCards() {
        return closedCards;
    }
}
