import java.util.ArrayList;

public class MessageList {
    protected ArrayList<Message> messages;

    protected MessageList(){
        messages = new ArrayList<Message>();
    }

    protected void addMessage(Message message){
        messages.add(message);
    }

    protected ArrayList<Message> getMessages(){
        return messages;
    }
}
