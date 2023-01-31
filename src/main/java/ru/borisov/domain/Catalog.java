package ru.borisov.domain;

import ru.borisov.exception.CategoryException;
import ru.borisov.repository.CategoryRepository;
import ru.borisov.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void addCategory(String categoryTitle) {
        Category category = new Category(categoryTitle, productRepository);

        if (!categoryRepository.getCategories().containsValue(category)) {
            categoryRepository.getCategories().put(category.getId(), category);
            System.out.println("Creating new category " + categoryTitle + " ...");
        } else {
            for (Map.Entry<Integer, Category> entry : categoryRepository.getCategories().entrySet()) {
                if (category.equals(entry.getValue())) {
                    int id = entry.getValue().getId();
                    category.setId(id);
                }
            }
        }

        if (!categories.contains(category)) {
            categories.add(category);
            System.out.println("Category " + category.getTitle() + " has added to this Catalog!");
        } else {
            System.out.println("Category " + category.getTitle() + " has already exist in this Catalog!");
        }
    }

    public void removeCategoryFromCatalog(int categoryId) {
        Category category = categoryRepository.getCategories().get(categoryId);
        if (category == null) {
            throw new CategoryException("Category with id= " + categoryId + " not found");
        }
        if (categories.remove(category)) {
            Category.setCounter(Category.getCounter() - 1);
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
            categories.forEach(cat -> System.out.println(categories.indexOf(cat) + 1 + ". " + cat.getTitle()));
        }
    }
}
