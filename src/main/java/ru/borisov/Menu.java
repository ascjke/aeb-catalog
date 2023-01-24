package ru.borisov;

public class Menu {

    private final String[] options;

    public Menu(String[] options) {
        this.options = options;
    }

    public void printMenu() {
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
}
