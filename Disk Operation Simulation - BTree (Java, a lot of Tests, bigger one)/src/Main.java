import BTree.BTree;
import Memory.Memory;
import Memory.MemoryOverflowException;
import Memory.Record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BTree bTree = new BTree(2);
        Main apk = new Main();
        // from file
        for (String path : args) {
            File f = new File(path);
            try (Scanner scanner = new Scanner(f)) {
                int line = 1;
                while (scanner.hasNext()) {
                    System.out.println(".........." + line + "..........");
                    String command = apk.interpret(scanner, bTree);
                    if (!command.isEmpty()) {
                        if (command.startsWith("#")) {
                            while (!Objects.equals(command = scanner.next(), "#")) {
                                System.out.print(command + " ");
                            }
                            System.out.println();
                        } else {
                            System.err.println("Cannot resolve a command on line: " + line);
                        }
                    }
                    line++;
                }
            } catch (FileNotFoundException e) {
                System.err.println("File \"" + path + "\" not found.");
            }
        }
        // interactive
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("........................");
            String command = apk.interpret(scanner, bTree);
            if (!command.isEmpty()) {
                switch (command) {
                    case "quit" -> running = false;
                    default -> System.err.println("No such command. Try \"quit\" to exit interactive mode.");
                }
            }
        }

    }

    private String interpret(Scanner scanner, BTree bTree) {
        System.out.print("Processing...");
        try {
            String command = scanner.next();
            switch (command) {
                case "add" -> {
                    int key = 0;
                    int[] recordData = new int[Record.SIZE];
                    for (int i = 0; i < Record.SIZE + 1; i++) {
                        // there is no argument checking but only 1 byte values are valid
                        // otherwise the older part is going to be forgotten
                        if (i == 0) {
                            key = scanner.nextInt();
                        } else {
                            recordData[i - 1] = scanner.nextInt();
                        }
                    }
                    bTree.add(new Record(key, recordData));
                }
                case "del" -> {
                    int key = scanner.nextInt();
                    bTree.remove(key);
                }
                case "show" -> System.out.println("\r" + bTree.asTree() + "\n");
                case "records" -> System.out.println("\r" + bTree.readAllRecords() + "\n");
                default -> {
                    System.out.print("\r");
                    return command;
                }
            }
            System.out.println("\r" + Memory.getInstance().statistics());
            System.out.println(bTree.statistics());
            return "";
        } catch (MemoryOverflowException e) {
            System.err.println("Memory file was too small. Overflow occurred.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Memory file not accessible.");
            throw new RuntimeException(e);
        }
    }
}