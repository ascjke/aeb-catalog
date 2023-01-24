package ru.borisov;


import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    private Menu[] appMenus;
    private static final String[] MAIN_MENU_OPTIONS =
            {"1- Option 1",
                    "2- Option 2",
                    "3- Option 3",
                    "0- Exit"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu mainMenu = new Menu(MAIN_MENU_OPTIONS);
        int option = 1;
        while (true) {
            mainMenu.printMenu();
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 0 -> System.exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 and " + MAIN_MENU_OPTIONS.length);
                scanner.next();
            }
        }
    }

    private static void option1() {
        System.out.println("Thanks for choosing option 1");
    }

    private static void option2() {
        System.out.println("Thanks for choosing option 2");
    }

    private static void option3() {
        System.out.println("Thanks for choosing option 3");
    }
}
