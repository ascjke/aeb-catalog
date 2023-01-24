package ru.borisov.domain;

import ru.borisov.exception.CatalogException;

import java.util.*;

public class Catalog {

    private final Map<Integer, Category> categories = new TreeMap<>();

    public void addCategory(Category category) {
         if (categories.put(category.getId(), category) == null) {
             throw new CatalogException("Category " + category.getTitle() + " is already exists!");
         }
        System.out.println("Category " + category.getTitle() + " have been added!");
    }

    public void removeCategory(int categoryId) {
        if (categories.remove(categoryId) == null) {
            throw new CatalogException("Category with id=" + categoryId + " doesnt exists!");
        }
        System.out.println("Category with id=" + categoryId + " have been removed!");
    }

    public void showCatalog() {
        List<Category> nonEmptyCategories = categories.values().stream()
                .filter(category -> !category.getProducts().isEmpty())
                .toList();

        System.out.println("Non empty categories:");
        nonEmptyCategories.forEach(c -> System.out.println(c.getId() + ") " + c));
    }
}
