package lars.wittenbrink.halligalli.game.user;

import java.util.Deque;
import java.util.Queue;
import java.util.Stack;

import lars.wittenbrink.halligalli.game.cards.Card;

interface IUser {

    String getName();
    Deque<Card> getOpenedCards();
    Deque<Card> getClosedCards();

}
