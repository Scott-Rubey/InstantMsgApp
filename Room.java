import java.util.ArrayList;

public class Room {
    protected String name;
    protected ArrayList<User> users;

    protected Room(String name){
        this.name = name;
        this.users = new ArrayList<>();
    }

    protected String getName(){
        return this.name;
    }

    protected ArrayList getUsers(){
        return this.users;
    }

    protected void addUser(User u){
        users.add(u);
    }

    protected void removeUser(User u) { users.remove(u); }
}
