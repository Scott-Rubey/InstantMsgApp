public class User {
    //TODO: does the user require its own roomlist?

    protected String name;

    protected User(String name){
        this.name = name;
    }

    protected String getName(){
        return this.name;
    }
}
