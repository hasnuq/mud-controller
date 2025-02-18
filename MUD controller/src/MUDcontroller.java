import java.util.Scanner;
public class MUDcontroller {

    private final Player player;
    private boolean running;

    public MUDcontroller(Player player) {
        this.player = player;
        this.running = true;
    }
    public void runGameLoop() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the MUD! Type 'help' for a list of commands.");

        while (running) {
            System.out.print("> ");
            String input = in.nextLine().trim();
            handleInput(input);
        }

        System.out.println("Game over. Thanks for game!");
    }
    public void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("Invalid command. Write 'pick up <itemName>'.");
                }
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
                break;
        }
    }
    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.getDescription());

        if (currentRoom.getItems().isEmpty()) {
            System.out.println("No items here.");
        } else {
            System.out.println("Items here: " + currentRoom.getItems());
        }
    }
    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Where should I go? Write 'move forward', 'move left', etc.");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Room nextRoom = currentRoom.getConnectedRoom(direction);

        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You move " + direction);
            lookAround();
        } else {
            System.out.println("You can't go!");
        }
    }

    private void pickUp(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("What should I pick up? Write 'pick up <itemName>");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);

        if (item != null) {
            player.addItemToInventory(item);
            currentRoom.removeItem(item);
            System.out.println("You pick up the " + itemName);
        } else {
            System.out.println("No item named '" + itemName + "' here!");
        }
    }
    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("You are carrying nothing");
        } else {
            System.out.println("You are carrying: " + player.getInventory());
        }
    }
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Show the current room");
        System.out.println("move <forward|back|left|right> - Move in a direction");
        System.out.println("pick up <itemName> - Pick up an items");
        System.out.println("inventory - List the items you have");
        System.out.println("help - Show help menu");
        System.out.println("quit/exit - End the game");
    }
}
