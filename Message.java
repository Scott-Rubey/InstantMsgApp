import java.util.Date;
import java.text.SimpleDateFormat;

public class Message {
    protected String timestamp;
    protected String message;

    protected Message(String message){
        this.timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.message = message;
    }

    protected String getMessage(){
        return this.message;
    }
}
