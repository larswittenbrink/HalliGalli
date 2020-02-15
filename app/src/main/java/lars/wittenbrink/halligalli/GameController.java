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
        addUser(new Bot("Hubert", 6));
        addUser(new User("HH"));
        addUser(new User("ff"));
        distributeCards(mixCards(createCards()));
        actualUser = users.get(0);
        printCards();
        System.out.println();
        for (int i = 0; i < 60; i++) {
            move();
            selectNextUser();
        }
        printCards();
        System.out.println();
        coverAllCards();
        printCards();
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



    public void printCards(){
        for (User user:users) {
            System.out.println(user.getClosedCards().size());
        }
    }

    public void move(){
        if (!actualUser.getClosedCards().isEmpty())
        actualUser.getOpenedCards().push(actualUser.getClosedCards().poll());
    }

    public void coverAllCards(){
        for (User user:users) {
            while(!user.getOpenedCards().isEmpty()){
                user.getClosedCards().add(user.getOpenedCards().pop());
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
                if(!cards.isEmpty()){
                    user.getClosedCards().add(cards.get(0));
                    cards.remove(0);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameController();
    }




}
