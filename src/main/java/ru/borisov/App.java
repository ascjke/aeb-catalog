package ru.borisov;


import ru.borisov.domain.Catalog;
import ru.borisov.repository.CategoryRepository;
import ru.borisov.repository.ProductRepository;

public class App {

    public static void main(String[] args) {
        CategoryRepository categoryRepository = new CategoryRepository();
        ProductRepository productRepository = new ProductRepository();
        Catalog catalog = new Catalog(categoryRepository, productRepository);
        CLI cli = new CLI(catalog);
        cli.mainMenu();
    }
}
