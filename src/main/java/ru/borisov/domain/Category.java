package ru.borisov.domain;

import ru.borisov.exception.CategoryException;
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

    public void setId(int id) {
        this.id = id;
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
            products.put(++productNumber, product);
        } else {
            System.out.println("Product \"" + product.getTitle() + "\" already exist in this Category");
        }
    }

    public void removeProduct(int productNum, Product product) {
        if (productRepository.getProducts().containsValue(product)) {
            int amountToBeSubtracted = product.getAmount();
            Product _product = productRepository.getProducts().get(product.getTitle());
            _product.setAmount(_product.getAmount() - amountToBeSubtracted);
            productRepository.getProducts().put(_product.getTitle(), _product);
        }
        products.remove(productNum);
        this.productNumber = this.productNumber - 1;
        System.out.println("Product " + product.getTitle() + " was deleted from this Category!");
    }

    public void showProducts() {
        if (products.isEmpty()) {
            System.out.println("The are no products in category Laptop yet\n");
        } else {
            products.forEach((k, v) -> System.out.println(k + ". " + v));
            System.out.println();
        }
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
}
