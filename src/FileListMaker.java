import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {
    private static List<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFilename = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to File List Maker!");

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim().toUpperCase();

            try {
                switch (choice) {
                    case "A" -> addItem();
                    case "D" -> deleteItem();
                    case "I" -> insertItem();
                    case "M" -> moveItem();
                    case "V" -> viewList();
                    case "C" -> clearList();
                    case "O" -> openFile();
                    case "S" -> saveFile();
                    case "Q" -> {
                        if (needsToBeSaved) {
                            promptToSaveBeforeExit();
                        }
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid entry. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
                \nMenu:
                A - Add item
                D - Delete item
                I - Insert item
                M - Move item
                V - View list
                C - Clear list
                O - Open list from file
                S - Save list to file
                Q - Quit
                Choose an option:""");
    }

    private static void addItem() {
        System.out.print("Enter item to add: ");
        list.add(scanner.nextLine());
        needsToBeSaved = true;
    }

    private static void deleteItem() {
        viewList();
        System.out.print("Enter index to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem() {
        viewList();
        System.out.print("Enter index to insert: ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter item to insert: ");
        String item = scanner.nextLine();
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem() {
        viewList();
        System.out.print("Enter index of item to move: ");
        int from = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new index: ");
        int to = Integer.parseInt(scanner.nextLine());
        if (from >= 0 && from < list.size() && to >= 0 && to <= list.size()) {
            String item = list.remove(from);
            list.add(to, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d: %s%n", i, list.get(i));
        }
    }

    private static void clearList() {
        list.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }

    private static void openFile() throws IOException {
        if (needsToBeSaved) {
            System.out.print("Unsaved changes. Would you like to save current list before opening new file? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                saveFile();
            }
        }

        System.out.print("Enter filename to open (without .txt): ");
        String filename = scanner.nextLine().trim() + ".txt";
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            list = Files.readAllLines(path);
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("File loaded.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void saveFile() throws IOException {
        if (currentFilename == null) {
            System.out.print("Enter filename to save as (without .txt): ");
            currentFilename = scanner.nextLine().trim() + ".txt";
        }
        Path path = Paths.get(currentFilename);
        Files.write(path, list);
        needsToBeSaved = false;
        System.out.println("List saved to " + currentFilename);
    }

    private static void promptToSaveBeforeExit() throws IOException {
        System.out.print("You have unsaved changes. Would you like to save before quitting? (Y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            saveFile();
        }
    }
}