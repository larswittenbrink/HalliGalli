package lars.wittenbrink.halligalli.cards;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lars.wittenbrink.halligalli.user.User;

public final class Cards {
    public Cards(){

    }

    public static List<Card> createCards() {
        List<Card> cards = new LinkedList<>();
        for (FruitIcon fruitIcon : FruitIcon.values()) {
            for (FruitNumber fruitNumber : FruitNumber.values()) {
                for (int i = 0; i < fruitNumber.getNumber(); i++) {
                    cards.add(new Card(fruitIcon, fruitNumber));
                }
            }
        }
        return cards;
    }

    public static List<Card> mixCards(List<Card> cards) {
        Collections.shuffle(cards);
        return cards;
    }

    public static void distributeCards(List<Card> cards, List<User> users) {
        while (!cards.isEmpty()) {
            for (User user : users) {
                if (!cards.isEmpty()) {
                    user.getClosedCards().add(cards.get(0));
                    cards.remove(0);
                } else {
                    return;
                }
            }
        }
    }
}
