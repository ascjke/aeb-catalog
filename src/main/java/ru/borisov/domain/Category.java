package ru.borisov.domain;

import ru.borisov.exception.CategoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Category {

    private static final int TITLE_MAX_LENGTH = 255;
    private static Map<String, Category> categoryRegistry = new HashMap<>();
    private static int counter = 0;
    private final int id;
    private String title;
    private final Map<Integer, Product> products = new TreeMap<>();

    private Category(String title) {
        id = ++counter;
        if (title.length() <= TITLE_MAX_LENGTH) {
            this.title = title;
        } else {
            throw new CategoryException("Category title is too long!(more than 255 characters)");
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void createCategory(String title) {
        Category category = new Category(title);
        int categoryRegistrySize = categoryRegistry.size();
        categoryRegistry.put(category.getTitle(), category);
        if (categoryRegistrySize == categoryRegistry.size()) {
            System.out.println("Category \"" + category.getTitle() + "\" already exist!");
            counter--;
        }
    }

    public void renameCategory(String oldTitle, String newTitle) {
        Category editedCategory = categoryRegistry.get(oldTitle);
        editedCategory.setTitle(newTitle);
        categoryRegistry.remove(oldTitle);
        categoryRegistry.put(newTitle, editedCategory);
    }

    public void showAllCategories() {
        categoryRegistry.forEach((title, category) -> System.out.println(title + ": " + category));
    }

    public void showCategory() {
        Map<Integer, Product> productsEdited = products.entrySet().stream()
                .filter(es -> es.getValue().getAmount() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String output = "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", products=" + productsEdited +
                '}';
        System.out.println(output);
    }

    public void addProduct(Product product) {
        if (!products.containsValue(product)) {
            products.put(products.size() + 1, product);
            System.out.println("Product \"" + product.getTitle() + "\" has been added!");
        }
    }

    public void removeProduct(int productId) {
        Product product = products.get(productId);
        if (product != null) {
            products.remove(productId);
            System.out.println("Product " + product.getTitle() + " deleted!");
        } else {
            System.out.println("Product with id=" + productId + " doesn't exist!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(title, category.title) && Objects.equals(products, category.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, products);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", products=" + products +
                '}';
    }
}
