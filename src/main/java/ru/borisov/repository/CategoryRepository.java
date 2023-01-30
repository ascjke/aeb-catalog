package ru.borisov.repository;

import ru.borisov.domain.Category;
import ru.borisov.exception.CategoryException;

import java.util.HashMap;
import java.util.Map;

public class CategoryRepository {

    private final Map<Integer, Category> categories = new HashMap<>();

    public Map<Integer, Category> getCategories() {
        return categories;
    }

    public void createCategory(Category category) {
        categories.put(category.getId(), category);
        System.out.println("Category " + category.getTitle() + " has created!");

    }

    public void renameCategory(int categoryId, String newTitle) {
        Category categoryToBeEdited = categories.get(categoryId);
        if (categoryToBeEdited == null) {
            throw new CategoryException("Category with id=" + categoryId + " doesnt exists!");
        }
        categoryToBeEdited.setTitle(newTitle);
        categories.put(categoryId, categoryToBeEdited);
        System.out.println("The category title has been changed!");
    }

    public void showAllCategories() {
        System.out.println("\nAll categories: ");
        if (!categories.isEmpty()) {
            categories.forEach((id, category) -> System.out.println(id + ": " + category.getTitle()));
        } else {
            System.out.println("There are no categories yet\n");
        }
    }
}
