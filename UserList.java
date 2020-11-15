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

    //find user in userlist
    protected boolean findUser(String name){
        User user = null;
        boolean found = false;

        //if user found in list
        for(int i = 0; i < users.size(); ++i){
            if(name.equals(users.get(i).getName())) {
                user = users.get(i);
                found = true;
            }
        }

        return found;
    }
}