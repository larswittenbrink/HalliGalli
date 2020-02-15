package lars.wittenbrink.halligalli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lars.wittenbrink.halligalli.user.Bot;
import lars.wittenbrink.halligalli.user.User;

public class GameController {

    private List<User> users;
    private User actualUser;

    public GameController() {
        users = new ArrayList<>();
        addUser(new Bot("Alfred", 6));
        addUser(new User("Felix"));
        addUser(new User("Lars"));

        distributeCards(mixCards(createCards()));

        for (User user:users) {
            print(user);
        }
        System.out.println();

        actualUser = users.get(0);

        for (int i = 0; i < 60; i++) {
            move(actualUser);
        }


        for (User user:users) {
            print(user);
        }
        System.out.println();
    }

    public void addUser(User user){
        users.add(user);
    }

    public void selectNextUser(){
        if(!allCardsOpen()){
            if(users.indexOf(actualUser)+1 >= users.size()){
                actualUser = users.get(0);
            } else {
                actualUser = users.get(users.indexOf(actualUser)+1);
            }
            if(actualUser.getClosedCards().isEmpty()) selectNextUser();
        }
    }


    public void move(User user){
        if (!actualUser.getClosedCards().isEmpty() && user == actualUser)
        actualUser.getOpenedCards().push(actualUser.getClosedCards().poll());

        if(allCardsOpen()) coverAllCards();
        selectNextUser();
    }

    public void coverAllCards(){
        for (User user:users) {
            List<Card> cards = new LinkedList<>();
            while (!user.getOpenedCards().isEmpty()) {
                cards.add(user.getOpenedCards().pop());
            }
            cards = mixCards(cards);
            for (Card card : cards) {
                user.getClosedCards().add(card);
            }
        }
    }

    public boolean allCardsOpen(){
        boolean allCardsOpen = true;
        for (User user:users) {
            if(!user.getClosedCards().isEmpty()) allCardsOpen = false;
        }
        return allCardsOpen;
    }

    //Cards
    public List<Card> createCards() {
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

    public List<Card> mixCards(List<Card> cards) {
        Collections.shuffle(cards);
        return cards;
    }

    public void distributeCards(List<Card> cards) {
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



    public void print(User user){
        System.out.println(user.getOpenedCards().size() + " " + user.getClosedCards().size());
    }
    public void printCards(User user){
        for (Card card:user.getClosedCards()) {
            System.out.println(card.getFruitIcon().toString() + card.getFruitNumber().toString());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new GameController();
    }

}
