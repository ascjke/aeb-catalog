package ru.borisov;

import ru.borisov.exception.CatalogException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Catalog {

    private final Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
         if (!categories.add(category)) {
             throw new CatalogException("Category " + category.getTitle() + " is already exists!");
         }
        System.out.println("Category " + category.getTitle() + " have been added!");
    }

    public void removeCategory(Category category) {
        if (!categories.remove(category)) {
            throw new CatalogException("Category " + category.getTitle() + " doesnt exists!");
        }
        System.out.println("Category " + category.getTitle() + " have been removed!");
    }

    public void showCatalog() {
        List<Category> nonEmptyCategories = categories.stream()
                .filter(category -> !category.getProducts().isEmpty())
                .toList();

        System.out.println("Non empty categories:");
        nonEmptyCategories.forEach(System.out::println);
    }
}
