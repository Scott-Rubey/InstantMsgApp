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

    //finds and returns a room if it exists on the list
    protected Room findRoom(String roomName) {
        Room room = null;
        boolean found = false;
        int i = 0;

        //find room if it exists and exit loop
        while (!found && i < rooms.size()) {
            if (roomName.equals(rooms.get(i).getName())) {
                found = true;
                room = rooms.get(i);
            }
            ++i;
        }

        return room;
    }
}
