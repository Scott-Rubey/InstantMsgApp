import java.util.Date;
import java.text.SimpleDateFormat;

public class Message {
    protected String timestamp;
    protected String msgText;

    protected Message(String message){
        this.timestamp = new SimpleDateFormat("HH:mm MM/dd/yyyy").format(new Date());
        this.msgText = message;
    }

    protected String getMsgText(){
        return this.msgText;
    }
}
