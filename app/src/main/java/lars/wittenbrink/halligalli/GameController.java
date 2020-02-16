package lars.wittenbrink.halligalli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import lars.wittenbrink.halligalli.user.Bot;
import lars.wittenbrink.halligalli.user.User;

public class GameController {

    private List<User> users;
    private User actualUser;
    private final Random random;

    //Random (Math.abs(i-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100))

    public GameController() {
        users = new ArrayList<>();
        random = new Random();
        addUser(new Bot("Alfred", 100));
        addUser(new Bot("Felix", 50));

        distributeCards(mixCards(createCards()));

        for (User user:users) {
            print(user);
        }
        System.out.println();

        actualUser = users.get(0);
        if(actualUser instanceof Bot){
            move(actualUser);
        }

        Scanner scanner = new Scanner(System.in);


        while(true){

            while(!scanner.hasNext()){}
            if(scanner.nextLine().equals("n")) move(actualUser);
        }


    }

    // TODO: 16.02.2020   Was passiert mit Spielern die verloren haben??
    
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

    public void press(User user){
        if (fiveFruitsOpen()){
            for (User user1:users) {
                coverCards(user1, user);
            }
        } else {
            for (User user1:users) {
                if(user1 != user){
                    if(!user.getClosedCards().isEmpty()) {
                        user1.getClosedCards().add(user.getClosedCards().poll());
                    }
                }
            }
        }


    }

    public boolean fiveFruitsOpen(){
        for (FruitIcon fruitIcon:FruitIcon.values()) {
            int anz = 0;
            for (User user : users) {
                if(!user.getOpenedCards().isEmpty()) {
                    if (user.getOpenedCards().peek().getFruitIcon() == fruitIcon){
                        anz += user.getOpenedCards().peek().getFruitNumber().getValue();
                    }
                }
            }
            if(anz == 5) return true;
        }
        return false;
    }

    public void coverCards(User user, User toUser){
        List<Card> cards = new LinkedList<>();
        while (!user.getOpenedCards().isEmpty()) {
            cards.add(user.getOpenedCards().pop());
        }
        cards = mixCards(cards);
        for (Card card : cards) {
            toUser.getClosedCards().add(card);
        }
    }

    public void move(User user){
        if (!actualUser.getClosedCards().isEmpty() && user == actualUser)
        actualUser.getOpenedCards().push(actualUser.getClosedCards().poll());

        if(allCardsOpen()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            coverAllCards();
        }
        selectNextUser();
//Tessst
        for (User user1:users) {
            print(user1);
        }
        System.out.println();

        if(actualUser instanceof Bot){
            try {
                Thread.sleep((Math.abs(((Bot) actualUser).getDifficulty()-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move(actualUser);
        }
    }

    public void coverAllCards(){
        for (User user:users) {
            coverCards(user, user);
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
