package lars.wittenbrink.halligalli.user;

import java.util.Queue;
import java.util.Stack;

import lars.wittenbrink.halligalli.Card;

interface IUser {

    String getName();
    Stack<Card> getOpenedCards();
    Queue<Card> getClosedCards();

}
