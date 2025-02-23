import java.util.List;
import java.util.Scanner;
public class MUDcontroller {
    private Player player;
    private boolean running;
    private Scanner in;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.in = new Scanner(System.in);
    }

    public void runGameLoop() {
        System.out.println("Welcome to the MUD! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = in.nextLine().trim();
            handleInput(input);
        }
    }

    private void handleInput(String input) {
        if (input.isEmpty()) return;
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : null;

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                if (argument != null) {
                    move(argument);
                } else {
                    System.out.println("move <direction> (e.g., move north)");
                }
                break;
            case "pick":
                if (argument != null && argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("pick up <item name>");
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
                System.out.println("Game over. Thanks for playing.");
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private void lookAround() {
        player.getCurrentRoom().describe();
    }

    private void move(String direction) {
        Room nextRoom = player.getCurrentRoom().getExit(direction.toLowerCase());
        if (nextRoom != null) {
            System.out.println("You leave: " + player.getCurrentRoom().getName());
            player.setCurrentRoom(nextRoom);
            System.out.println("You have moved to: " + nextRoom.getName());
            nextRoom.describe();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String itemName) {
        Item item = player.getCurrentRoom().removeItem(itemName);
        if (item != null) {
            player.getInventory().add(item);
            System.out.println("You picked up: " + item.getName());
        } else {
            System.out.println("There is no such item in this room.");
        }
    }

    private void checkInventory() {
        List<Item> inventory = player.getInventory();
        System.out.println("Your inventory:");
        if (inventory.isEmpty()) {
            System.out.println("Empty.");
        } else {
            for (Item item : inventory) {
                System.out.println("- " + item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("- look : Observe the current room.");
        System.out.println("- move <direction>: north, south, east, west.");
        System.out.println("- pick up <item> : Pick up an item from the room.");
        System.out.println("- inventory : View items in your inventory.");
        System.out.println("- help : Show available commands.");
        System.out.println("- quit/exit : Exit the game.");
    }
}
