package ru.borisov;

import ru.borisov.domain.Catalog;
import ru.borisov.domain.Category;
import ru.borisov.domain.Product;
import ru.borisov.domain.Unit;
import ru.borisov.exception.ProductException;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLI {

    private Scanner scanner;
    private static final String[] MAIN_MENU_OPTIONS =
            {"1. Enter into catalog",
                    "2. Categories repository",
                    "3. Products repository",
                    "0. Exit"};

    private static final String[] CATALOG_MENU_OPTIONS =
            {"\n1. Show all existing categories. Enter here if you want add/remove products",
                    "2. Show categories with products",
                    "3. Add category to this catalog",
                    "4. Remove category from this catalog",
                    "0. Back"};

    private static final String[] PRODUCT_MENU_OPTIONS =
            {"\n1. Change product amount",
                    "2. Change product price",
                    "0. Back"};

    private static final String[] ADD_PRODUCT_MENU_OPTIONS =
            {"\n1. Add a new product",
                    "2. Remove product by id",
                    "0. Back"};

    private static final String[] CATEGORY_REPO_MENU_OPTIONS =
            {"\n1. Show all existing categories",
                    "2. Create new category",
                    "3. Rename existing category",
                    "0. Back"};


    private static final String[] PRODUCT_REPO_MENU_OPTIONS =
            {"1- Show all existing products",
                    "2- Create new product",
                    "0- Back"};

    private final Catalog catalog;

    public CLI(Catalog catalog) {
        this.catalog = catalog;
    }

    public void mainMenu() {
        scanner = new Scanner(System.in);
        while (true) {
            printMenu(MAIN_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, MAIN_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> categoryMenu();
                    case "2" -> categoriesRepoMenu();
                    case "3" -> productsRepoMenu();
                    case "0" -> System.exit(0);
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (MAIN_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void categoryMenu() {
        while (true) {
            printMenu(CATALOG_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, CATALOG_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> categoriesAllMenu();
                    case "2" -> categoriesWithProductsMenu();
                    case "3" -> addCategoryMenu();
                    case "4" -> removeCategoryMenu();
                    case "0" -> {
                        System.out.println();
                        mainMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (CATALOG_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void categoriesAllMenu() {
        System.out.println();
        if (!catalog.getCategories().isEmpty()) {
            catalog.showCatalogCategories();
            System.out.print("Choose the category number to see the products or 0 to back: ");

            String categoryNum = scanner.next();
            int categoriesAmount = catalog.getCategories().size() + 1;
            if (isValidOption(categoryNum, categoriesAmount)) {
                if (categoryNum.equals("0")) {
                    categoryMenu();
                }
                Category category = catalog.getCategories().get(Integer.parseInt(categoryNum) - 1);
                Map<Integer, Product> products = category.getProducts();
                if (products.isEmpty()) {
                    System.out.println("The are no products in category " + category.getTitle() + " yet");
                    addOrRemoveProductOption(category);
                } else {
                    int index = 0;
                    for (Product product : products.values()) {
                        System.out.println(++index + ". " + product);
                    }
                    System.out.print("\nChoose the product number to change his price or amount.\nIf you want add or remove some new product type \"add\" or \"remove\".\nOr enter the 0 to go back.\n");
                    String productNumber = scanner.next();
                    if (productNumber.equals("0")) {
                        categoriesAllMenu();
                    }
                    if (productNumber.equals("add")) {
                        addOrRemoveProductOption(category);
                    }
                    if (isValidOption(productNumber, products.size() + 1)) {
                        int _productNumber = Integer.parseInt(productNumber);
                        productsOfCategoryMenu(category, _productNumber);
                    } else {
                        System.err.println("Please enter an integer value between 1 and " + (products.size()));
                        categoriesAllMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (categoriesAmount - 1));
                categoriesAllMenu();
            }
        } else {
            catalog.showCatalogCategories();
            System.out.println("0- Back");
            String input = scanner.next();
            if (input.equals("0")) {
                categoryMenu();
            } else {
                System.out.println("Type 0 to back");
                categoriesAllMenu();
            }
        }
    }

    private void categoriesWithProductsMenu() {
        System.out.println();
        List<Category> categories = catalog.getCategories().stream()
                .filter(category -> !category.getProducts().isEmpty())
                .toList();
        if (categories.isEmpty()) {
            System.out.println("There are no categories with products");
            categoryMenu();
        } else {
            System.out.println("Categories with products:");
            categories.forEach(System.out::println);
            categoryMenu();
        }
    }


    private void addOrRemoveProductOption(Category category) {
        while (true) {
            printMenu(ADD_PRODUCT_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, ADD_PRODUCT_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> addProductToCategoryMenu(category);
                    case "2" -> removeProductFromCategoryMenu(category);
                    case "0" -> {
                        System.out.println();
                        categoriesAllMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (ADD_PRODUCT_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void removeProductFromCategoryMenu(Category category) {
        category.showProducts();
        System.out.print("Choose the product number to remove: \nOr enter 0 to go back: ");
        String productNum = scanner.next();
        if (productNum.equals("0")) {
            categoriesAllMenu();
        }
        int productsCountBeforeRemoving = category.getProducts().size() + 1;
        if (isValidOption(productNum, productsCountBeforeRemoving)) {
            Product product = category.getProducts().get(Integer.parseInt(productNum));
            category.removeProduct(Integer.parseInt(productNum), product);
        } else {
            System.err.println("Please enter an integer value between 1 and " + (productsCountBeforeRemoving - 1));
            removeProductFromCategoryMenu(category);
        }
    }

    private void addProductToCategoryMenu(Category category) {
        System.out.print("Please enter the title of new product: ");
        scanner.nextLine();
        String title = scanner.nextLine();
        while (true) {
            Unit[] units = Unit.values();
            int i = 1;
            for (Unit unit : units) {
                System.out.println(i++ + "- " + unit.toString());
            }
            System.out.print("Please choose the unit of the product: ");
            String unitId = scanner.next();
            if (isValidOption(unitId, units.length + 1) && !unitId.equals("0")) {
                Unit unit = Unit.values()[Integer.parseInt(unitId) - 1];
                System.out.print("Please enter the price of the product for each unit: ");
                double price;
                while (true) {
                    String _price = scanner.next();
                    if (isDouble(_price)) {
                        price = Double.parseDouble(_price);
                        System.out.print("Please enter the amount of the product: ");
                        String amount = scanner.next();
                        if (Pattern.matches("^\\d{1,}$", amount)) {
                            Product product = new Product(title, unit, price, Integer.parseInt(amount));
                            category.addProductToCategory(product);
                            System.out.println("Product " + product.getTitle() + " has been added to category " + category.getTitle());
                            if (!catalog.getProductRepository().getProducts().containsValue(product)) {
                                catalog.getProductRepository().addProductToRepository(product);
                            } else {
                                Product _product = catalog.getProductRepository().getProducts().get(product.getTitle());
                                _product.addAmount(product.getAmount());
                                catalog.getProductRepository().getProducts().put(_product.getTitle(), _product);
                            }
                            categoriesAllMenu();
                        } else {
                            System.err.println("Amount can be only integer number!");
                        }
                    } else {
                        System.err.println("Please enter the digit value (non 0)");
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (Unit.values().length));
            }
        }
    }

    private void productsOfCategoryMenu(Category category, int productNumber) {
        while (true) {
            printMenu(PRODUCT_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, PRODUCT_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> changeProductAmount(category, productNumber);
                    case "2" -> changeProductPrice(category, productNumber);
                    case "0" -> {
                        System.out.println();
                        categoriesAllMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (PRODUCT_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void changeProductAmount(Category category, int productNumber) {
        Product product = category.getProducts().get(productNumber);
        System.out.println("If you want add amount, type: + <amount>");
        System.out.println("If you want subtract amount, type: - <amount>");
        scanner.nextLine();
        while (true) {
            String command = scanner.nextLine();
            String[] args = command.trim().split("\\s");
            if (Pattern.matches("^[+-]$", args[0]) && Pattern.matches("^\\d{1,}$", args[1])) {
                switch (args[0]) {
                    case "+" -> {
                        product.addAmount(Integer.parseInt(args[1]));

                        Map<String, Product> productsRegistry = catalog.getProductRepository().getProducts();
                        Product productFromRegistry = productsRegistry.get(product.getTitle());
                        productFromRegistry.addAmount(Integer.parseInt(args[1]));
                        productsRegistry.put(product.getTitle(), productFromRegistry);

                        System.out.println("Amount of product " + product.getTitle() + " was increased by " + args[1]);
                        productsOfCategoryMenu(category, productNumber);
                    }
                    case "-" -> {
                        try {
                            product.subtractAmount(Integer.parseInt(args[1]));
                        } catch (ProductException ex) {
                            System.out.println(ex.getMessage());
                            productsOfCategoryMenu(category, productNumber);
                        }

                        Map<String, Product> productRepository = catalog.getProductRepository().getProducts();
                        Product productFromRegistry = productRepository.get(product.getTitle());
                        try {
                            productFromRegistry.subtractAmount(Integer.parseInt(args[1]));
                        } catch (ProductException ex) {
                            productFromRegistry.setAmount(0);
                        }
                        productRepository.put(product.getTitle(), productFromRegistry);

                        System.out.println("Amount of product " + product.getTitle() + " was decreased by " + args[1]);
                        productsOfCategoryMenu(category, productNumber);
                    }
                }
            } else {
                System.err.println("First argument must be + or -, second argument digit!");
            }
        }

    }

    private void changeProductPrice(Category category, int productNumber) {
        Product product = category.getProducts().get(productNumber);
        System.out.print("Enter the new price of product: ");
        while (true) {
            String price = scanner.next();
            if (isDouble(price)) {
                double _price = Double.parseDouble(price);
                product.setPrice(_price);
                Map<String, Product> productsRegistry = catalog.getProductRepository().getProducts();
                productsRegistry.put(product.getTitle(), product);
                System.out.println(product.getTitle() + " price has been changed!");
                productsOfCategoryMenu(category, productNumber);
            } else {
                System.err.println("Please enter the digit value (non 0)");
            }
        }
    }


    private void addCategoryMenu() {
        System.out.print("\nEnter the category title: ");
        String title = scanner.next();
        catalog.addCategory(title);
        categoryMenu();
    }

    private void removeCategoryMenu() {
        catalog.showCatalogCategories();
        System.out.println("Choose category number, which you want to remove: \nOr enter 0 to go back: ");
        while (true) {
            String input = scanner.next();
            if (input.equals("0")) {
                categoriesAllMenu();
            }
            if (isValidOption(input, catalog.getCategories().size() + 1)) {
                int categoryId = Integer.parseInt(input);
                catalog.removeCategoryFromCatalog(categoryId);
                categoryMenu();
            } else {
                System.err.println("Please enter an integer value between 1 and " + (catalog.getCategories().size()));
            }
        }
    }

    private void categoriesRepoMenu() {
        while (true) {
            printMenu(CATEGORY_REPO_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, CATEGORY_REPO_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> showAllCategoriesMenu();
                    case "2" -> createNewCategoryMenu();
                    case "3" -> renameCategoryMenu();
                    case "0" -> {
                        System.out.println();
                        mainMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (CATEGORY_REPO_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void showAllCategoriesMenu() {
        catalog.getCategoryRepository().showAllCategories();
        System.out.println("0- Back");
        String option = scanner.next();
        if (!option.equals("0")) {
            System.out.println("Type 0 to back");
            showAllCategoriesMenu();
        } else {
            categoriesRepoMenu();
        }
    }

    private void createNewCategoryMenu() {
        System.out.print("Please enter the title of new category: ");
        String title = scanner.next();
        Category category = new Category(title, catalog.getProductRepository());
        if (catalog.getCategoryRepository().getCategories().containsValue(category)) {
            Category.setCounter(Category.getCounter() - 1);
            System.out.println("Category " + category.getTitle() + " already exist!");
        } else {
            catalog.getCategoryRepository().createCategory(category);
            categoriesRepoMenu();
        }
    }

    private void renameCategoryMenu() {
        catalog.getCategoryRepository().showAllCategories();
        System.out.println("Enter the id of category, which you want to rename: \nOr enter 0 to go back: ");
        while (true) {
            String input = scanner.next();
            if (input.equals("0")) {
                categoriesRepoMenu();
            }
            if (isValidOption(input, catalog.getCategoryRepository().getCategories().size() + 1)) {
                int categoryId = Integer.parseInt(input);
                Category category = catalog.getCategoryRepository().getCategories().get(categoryId);
                System.out.print("Enter the new title of category " + category.getTitle() + ": ");
                String newTitle = scanner.next();
                catalog.getCategoryRepository().renameCategory(categoryId, newTitle);
                categoriesRepoMenu();
            } else {
                System.err.println("Please enter an integer value between 1 and " + (catalog.getCategoryRepository().getCategories().size()));
            }
        }
    }

    private void productsRepoMenu() {
        System.out.println();
        while (true) {
            printMenu(PRODUCT_REPO_MENU_OPTIONS);
            String option = scanner.next();
            if (isValidOption(option, PRODUCT_REPO_MENU_OPTIONS.length)) {
                switch (option) {
                    case "1" -> showALlProductsMenu();
                    case "2" -> createNewProductMenu();
                    case "0" -> {
                        System.out.println();
                        mainMenu();
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (PRODUCT_REPO_MENU_OPTIONS.length - 1));
            }
        }
    }

    private void showALlProductsMenu() {
        catalog.getProductRepository().showAllProducts();
        System.out.println("0- Back");
        String option = scanner.next();
        if (!option.equals("0")) {
            System.out.println("Type 0 to back");
            showALlProductsMenu();
        } else {
            productsRepoMenu();
        }
    }

    private void createNewProductMenu() {
        System.out.print("Please enter the title of new product: ");
        scanner.nextLine();
        String title = scanner.nextLine();
        while (true) {
            Unit[] units = Unit.values();
            int i = 1;
            for (Unit unit : units) {
                System.out.println(i++ + "- " + unit.toString());
            }
            System.out.print("Please choose the unit of the product: ");
            String unitId = scanner.next();
            if (isValidOption(unitId, units.length + 1) && !unitId.equals("0")) {
                Unit unit = Unit.values()[Integer.parseInt(unitId) - 1];
                System.out.print("Please enter the price of the product for each unit: ");
                double price;
                while (true) {
                    String _price = scanner.next();
                    if (isDouble(_price)) {
                        price = Double.parseDouble(_price);
                        Product product = new Product(title, unit, price);
                        if (catalog.getProductRepository().getProducts().containsValue(product)) {
                            System.out.println("Product " + product.getTitle() + " already exists!");
                        } else {
                            catalog.getProductRepository().addProductToRepository(product);
                            System.out.println("Product " + product.getTitle() + " has been created!");
                            productsRepoMenu();
                        }
                    } else {
                        System.err.println("Please enter the digit value (non 0)");
                    }
                }
            } else {
                System.err.println("Please enter an integer value between 1 and " + (Unit.values().length));
            }
        }
    }

    public void printMenu(String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }

    private static boolean isValidOption(String option, int length) {
        if (option.matches("\\d")) {
            int n = Integer.parseInt(option);
            return (n >= 0 && n < length);
        } else {
            return false;
        }
    }

    private static boolean isDouble(String price) {
        if (price.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            return true;
        } else {
            return false;
        }
    }
}
