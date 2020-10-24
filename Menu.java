import java.util.Scanner;

public class Menu {
    protected static Scanner input = new Scanner(System.in);

    public static int topMenu()
    {
        int menuOption = 0;

        do
        {
            System.out.print("\nPlease choose from the following options:\n");
            System.out.print("\n1 - List existing rooms");
            System.out.print("\n2 - Join existing room");
            System.out.print("\n3 - Create new room");
            System.out.print("\n4 - Exit Program\n");
            System.out.print("\nEnter menu option here: ");

            menuOption = input.nextInt();
            input.nextLine();
            System.out.print("\n");

            if (1 > menuOption || 4 < menuOption)
                System.out.print("***Option out of range***\n");

        }
        while (1 > menuOption || 4 < menuOption);

        return menuOption;
    }

    public static int roomMenu()
    {
        int menuOption = 0;

        do
        {
            System.out.print("\nPlease choose from the following options:\n");
            System.out.print("\n1 - Show messages");
            System.out.print("\n2 - Post message");
            System.out.print("\n3 - List users in room");
            System.out.print("\n4 - Leave room\n");
            System.out.print("\nEnter menu option here: ");

            menuOption = input.nextInt();
            input.nextLine();
            System.out.print("\n");

            if (1 > menuOption || 4 < menuOption)
                System.out.print("***Option out of range***\n");

        }
        while (1 > menuOption || 4 < menuOption);

        return menuOption;
    }
}
