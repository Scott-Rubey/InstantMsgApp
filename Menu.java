import java.util.Scanner;

public class Menu {
    protected static Scanner input = new Scanner(System.in);

    protected static int topMenu()
    {
        int menuOption = 0;

        do
        {
            System.out.print("\nPlease choose from the following options:\n");
            System.out.print("\n1 - List chat rooms");
            System.out.print("\n2 - Join chat room");
            System.out.print("\n3 - Create new chat room");
            System.out.print("\n4 - List users in chat room");
            System.out.print("\n5 - Post message to a chat room");
            System.out.print("\n6 - Show all messages in a chat room");
            System.out.print("\n7 - Leave chat room");
            System.out.print("\n8 - Exit Program\n");
            System.out.print("\nEnter menu option here: ");

            menuOption = input.nextInt();
            input.nextLine();
            System.out.print("\n");

            if (1 > menuOption || 8 < menuOption)
                System.out.print("***Option out of range***\n");

        }
        while (1 > menuOption || 8 < menuOption);

        return menuOption;
    }
}
