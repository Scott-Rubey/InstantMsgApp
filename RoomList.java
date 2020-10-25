import java.util.ArrayList;

public class RoomList {
    protected ArrayList<Room> rooms;

    protected RoomList(){
        rooms = new ArrayList<>();
    }

    protected void addRoom(Room room){
        rooms.add(room);
    }

    protected ArrayList<Room> getRooms(){
        return rooms;
    }
}
