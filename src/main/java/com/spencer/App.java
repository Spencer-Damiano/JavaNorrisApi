package com.spencer;

import java.io.IOException;
import java.util.Scanner;


public class App
{
    public static void main( String[] args ) throws IOException {

        System.out.println("Chuck Norris API Call App");
        System.out.println("These are true facts about Chuck Norris");

        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine();

            // Skip empty inputs
            if (input.isEmpty()) {
                continue;
            }

            // Check for exit command
            if (input.equals("exit")) {
                running = false;
                break;
            }

            switch (input) {
                case "0" -> ApiCall.getCategories();
                case "1" -> ApiCall.getJoke();
                case "2" -> {
                    ApiCall.getCategories();
                    System.out.println("Pick a Category");
                    String category = scanner.nextLine();
                    if (category.equals("0")) { continue; }
                    ApiCall.getJoke(Integer.parseInt(category));
                }
                case "3", "quit" -> {
                    running = false;
                    break;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("end of test");
        scanner.close();
    }

    public static void printMenu() {
        System.out.println(" === MENU ===" +
                "\n 0 - show available joke categories" +
                "\n 1 - give random joke" +
                "\n 2 - get joke by category" +
                "\n 3 or 'quit' - end program" );
    }
}