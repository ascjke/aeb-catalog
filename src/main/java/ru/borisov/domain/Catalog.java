package ru.borisov.domain;

import ru.borisov.exception.CategoryException;
import ru.borisov.repository.CategoryRepository;
import ru.borisov.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final List<Category> categories = new ArrayList<>();

    public Catalog(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void createCategory(String categoryTitle) {
        Category category = new Category(categoryTitle, productRepository);
        categoryRepository.createCategory(category);
    }

    public void addCategory(String categoryTitle) {
        Category category = new Category(categoryTitle, productRepository);
        if (!categoryRepository.getCategories().containsValue(category)) {
            categoryRepository.getCategories().put(category.getId(), category);
            System.out.println("Creating new category " + categoryTitle + " ...");
        }
        if (!categories.contains(category)) {
            categories.add(category);
            System.out.println("Category " + category.getTitle() + " has added to this Catalog!");
        } else {
            Category.setCounter(Category.getCounter() -1);
            System.out.println("Category " + category.getTitle() + " has already exist in this Catalog!");
        }
    }

    public void renameCategory(int categoryId, String newTitle) {
        Category category = categories.stream()
                .filter(c -> c.getId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new CategoryException("This Catalog doesn't have category with id=" + categoryId));
        String oldTitle = category.getTitle();

        categories.remove(category);
        category.setTitle(newTitle);
        categories.add(category);
        categoryRepository.renameCategory(categoryId, newTitle);

        System.out.println("Category with title: " + oldTitle + " was renamed to: " + newTitle);
    }

    public void removeCategoryFromCatalog(int categoryId) {
        Category category = categoryRepository.getCategories().get(categoryId);
        if (category == null) {
            throw new CategoryException("Category with id= " + categoryId + " not found");
        }
        if (categories.remove(category)) {
            System.out.println("Category " + category.getTitle() + " was removed from these Catalog!");
        } else {
            System.out.println("Category " + category.getTitle() + " don't exist in these Catalog!");
        }
    }

    public void showCatalogCategories() {
        System.out.println("Categories: ");
        if (categories.isEmpty()) {
            System.out.println("There are no categories yet");
        } else {
            for (Category category : categories) {
                System.out.println(category.getId() + ": " + category.getTitle());
            }
        }
    }

    public void showAllCatalogCategories() {
        System.out.println("All categories of catalog: ");
        categories.forEach(Category::toStringWithoutProducts);
    }
}
