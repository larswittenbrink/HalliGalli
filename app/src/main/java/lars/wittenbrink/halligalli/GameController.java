package lars.wittenbrink.halligalli;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lars.wittenbrink.halligalli.user.User;

public class GameController {

    private List<User> users;
    private User actualUser;

    public GameController() {
        users = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public void selectNextUser(){
        if(users.indexOf(actualUser)+1 >= users.size()){
            actualUser = users.get(0);
        }else{
            actualUser = users.get(users.indexOf(actualUser)+1);
        }

        if(actualUser.getClosedCards().isEmpty()){
            selectNextUser();
        }
    }

}
