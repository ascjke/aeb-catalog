package ru.borisov.domain;

import ru.borisov.exception.CategoryException;
import ru.borisov.exception.ProductException;
import ru.borisov.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Category {

    private static final int TITLE_MAX_LENGTH = 255;
    private static int counter = 0;
    private final ProductRepository productRepository;
    private int id;
    private String title;
    private final Map<Integer, Product> products = new HashMap<>();
    private int productNumber = 0;

    public Category(String title, ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Category.counter = counter;
    }

    public void addProductToCategory(Product product) {
        if (!products.containsValue(product)) {
            if (!productRepository.getProducts().containsValue(product)) {
                productRepository.createProduct(product);
                System.out.println("Creating new product " + product.getTitle() + " ...");
            }
            products.put(++productNumber, product);
        } else {
            System.out.println("Product \"" + product.getTitle() + "\" already exist in this Category");
        }
    }

    public void removeProduct(int productNumber) {
        Product product = productRepository.getProducts().get(productNumber);
        if (product == null) {
            throw new ProductException("Product with number=" + productNumber + " doesn't exist!");
        }
        products.remove(productNumber);
        System.out.println("Product " + product.getTitle() + " was deleted from this Category!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(title, category.title) && Objects.equals(products, category.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, products);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", products=" + products +
                '}';
    }

    public String toStringWithoutProducts() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
