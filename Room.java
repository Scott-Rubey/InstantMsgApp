import java.util.ArrayList;

public class Room {
    protected String name;
    protected ArrayList<User> users;
    protected ArrayList<Message> messages;

    protected Room(String name){
        this.name = name;
        this.users = new ArrayList<>();
        this.messages = new ArrayList();
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

    protected ArrayList getMessages(){
        return this.messages;
    }

    protected void addMessage(Message message){
        this.messages.add(message);
    }

    //determines whether a user is on a room's userlist
    //returns true if user on list, false otherwise
    protected boolean findUser(User u){
        boolean found = false;
        int i = 0;

        while(!found && i < users.size()){
            if(u.getName().equals(users.get(i).getName()))
                found = true;
        }

        return found;
    }
}
