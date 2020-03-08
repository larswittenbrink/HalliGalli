package lars.wittenbrink.halligalli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import lars.wittenbrink.halligalli.cards.Card;
import lars.wittenbrink.halligalli.cards.Cards;
import lars.wittenbrink.halligalli.cards.FruitIcon;
import lars.wittenbrink.halligalli.cards.FruitNumber;
import lars.wittenbrink.halligalli.user.Bot;
import lars.wittenbrink.halligalli.user.User;


public class GameController {

    private List<User> users;
    private List<User> losers;
    private User actualUser;
    private final Random random;
    public static GameController INSTANCE;

    //Random (Math.abs(i-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100))

    public GameController(List<User> users) {
        INSTANCE = this;
        users = new LinkedList<>();
        //users.add(new User("Alfred"));
        users.add(new Bot("Dieter", 10));
        users.add(new Bot("gg", 100));
        this.users = users;
        losers = new LinkedList<>();
        random = new Random();
        Cards.distributeCards(Cards.mixCards(Cards.createCards()), users);

        for (User user:users) {
            print(user);
        }
        System.out.println();

        selectNextUser();
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

    //wählt den nächsten Spieler aus, der noch nicht aufgedeckte Karten hat. Falls alle Karten aufgedeckt sind, werden alle wieder zugedeckt
    public void selectNextUser(){

        if(actualUser == null && !users.isEmpty()){
            actualUser = users.get(0);
        }
        if(allCardsOpen()){
            coverAllCards();
            selectNextUser();
        } else{
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
        if(actualUser instanceof Bot){
            ((Bot) actualUser).getExecutorService().execute(((Bot) actualUser).getMoveThread());

            //new Thread(((Bot) actualUser).getMoveThread()).start();

            //((Bot) actualUser).getMoveThread().start();
        }
    }

    public void lose(User user){
        if(user.getClosedCards().isEmpty() && user.getOpenedCards().isEmpty()){
            if(user == actualUser) {
                selectNextUser();
            }
                losers.add(user);
                users.remove(user);
        }
    }

    public void move(User user){
        if (!actualUser.getClosedCards().isEmpty() && user == actualUser) {
            actualUser.getOpenedCards().push(actualUser.getClosedCards().poll());
        }

        selectNextUser();

//Tessst
        for (User user1:users) {
            print(user1);
        }
        System.out.println();

        if(fiveFruitsOpen()){
            for(User user1: users){
                if(user1 instanceof Bot){
                    ((Bot) user1).getPressThread().start();
                }
            }
        }

        if(actualUser instanceof Bot){
            ((Bot) actualUser).getExecutorService().execute(((Bot) actualUser).getMoveThread());

            //new Thread(((Bot) actualUser).getMoveThread()).start();
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
        cards = Cards.mixCards(cards);
        for (Card card : cards) {
            toUser.getClosedCards().add(card);
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



    public void print(User user){
        System.out.print(user.getOpenedCards().size() + " " + user.getClosedCards().size());

        if(!user.getOpenedCards().isEmpty()){
            System.out.print("   " + user.getOpenedCards().peek().getFruitIcon() +  user.getOpenedCards().peek().getFruitNumber());
            System.out.println();
        }
        else {
            System.out.println();
        }
    }

    public void printCards(User user){
        for (Card card:user.getClosedCards()) {
            System.out.println(card.getFruitIcon().toString() + card.getFruitNumber().toString());
        }
        System.out.println();
    }


    public static void main(String[] args) {
        new GameController(null);
    }
}
