package lars.wittenbrink.halligalli.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import lars.wittenbrink.halligalli.game.cards.Card;
import lars.wittenbrink.halligalli.game.cards.Cards;
import lars.wittenbrink.halligalli.game.cards.FruitIcon;
import lars.wittenbrink.halligalli.game.user.Bot;
import lars.wittenbrink.halligalli.game.user.User;


public class GameController {

    private List<User> users;
    private List<User> finishedUsers;
    private User actualUser;
    private final Random random;
    public static GameController INSTANCE;

    //Random (Math.abs(i-100)*(new Random().nextInt(16)+15)+(new Random().nextInt(200)+100))

    public GameController(List<User> users) {
        INSTANCE = this;
        this.users = users;
        finishedUsers = new LinkedList<>();
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

    }



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
            for (User otherUser:users) {
                coverCards(otherUser, user);
            }
        } else {
            for (User otherUser:users) {
                if(otherUser != user){
                    if(!user.getClosedCards().isEmpty()) {
                        otherUser.getClosedCards().addLast(user.getClosedCards().pollFirst());
                    }
                }
            }
        }

        for (User otherUser:users) {
            if(otherUser.getOpenedCards().isEmpty() && otherUser.getClosedCards().isEmpty()){
                finishUser(otherUser);
            }
        }

        if(actualUser instanceof Bot){
            ((Bot) actualUser).move();
        }
    }

    public void finishUser(User user){
        if(user.getClosedCards().isEmpty() && user.getOpenedCards().isEmpty()){
            if(user == actualUser) {
                selectNextUser();
            }
                finishedUsers.add(user);
                users.remove(user);
        }
    }

    public void move(User user){
        if (!actualUser.getClosedCards().isEmpty() && user == actualUser) {
            actualUser.getOpenedCards().addFirst(actualUser.getClosedCards().pollFirst());
        }
        selectNextUser();
        if(users.size() == 1){
            finishUser(users.get(0));
            return;
        }

        if(fiveFruitsOpen()){
            for(User otherUser: users){
                if(otherUser instanceof Bot){
                    ((Bot) otherUser).press();
                }
            }
        }
        if(actualUser instanceof Bot){
            ((Bot) actualUser).move();
        }

        for (User user1:users) {
            print(user1);
        }
        System.out.println();
    }

    public boolean fiveFruitsOpen(){
        for (FruitIcon fruitIcon:FruitIcon.values()) {
            int anz = 0;
            for (User user : users) {
                if(!user.getOpenedCards().isEmpty()) {
                    if (user.getOpenedCards().peekFirst().getFruitIcon() == fruitIcon){
                        anz += user.getOpenedCards().peekFirst().getFruitNumber().getValue();
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
            cards.add(user.getOpenedCards().pollFirst());
        }
        cards = Cards.mixCards(cards);
        for (Card card : cards) {
            toUser.getClosedCards().addLast(card);
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
        System.out.print(user.getName() + " " + user.getOpenedCards().size() + " " + user.getClosedCards().size());

        if(!user.getOpenedCards().isEmpty()){
            System.out.print("   " + user.getOpenedCards().peekFirst().getFruitIcon() + " " + user.getOpenedCards().peekFirst().getFruitNumber());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new GameController(null);
    }
}
