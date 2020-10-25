public class Room {
    protected String name;
    protected UserList users;

    protected Room(String name){
        this.name = name;
        users = new UserList();
    }

    protected String getName(){
        return this.name;
    }

    protected UserList getUsers(){
        return this.users;
    }

    protected void addUser(User u){
        users.addUser(u);
    }
}
