import java.util.ArrayList;

public class UserList {
    protected ArrayList<User> users;

    protected UserList(){
        users = new ArrayList<User>();
    }

    protected void addUser(User user){
        users.add(user);
    }

    protected ArrayList<User> getUsers(){
        return users;
    }
}
