package ru.borisov.domain;

import ru.borisov.exception.ProductException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product {

    private static final int TITLE_MAX_LENGTH = 255;
    private static Map<String, Product> productRegistry = new HashMap<>();
    private final String title;
    private final Unit unit;
    private double price;
    private int amount;

    public Product(String title, Unit unit, double price, int amount) {
        validateAllCtorArgs(title, price, amount);
        this.title = title;
        this.unit = unit;
        this.price = price;
        this.amount = amount;
    }

    public Product(String title, Unit unit, double price) {
        validateTitleAndPrice(title, price);
        this.title = title;
        this.unit = unit;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getAmount() {
        return amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void createProduct(String title, Unit unit, double price) {
        Product product = new Product(title, unit, price);
        int productRegistrySize = productRegistry.size();
        productRegistry.put(title, product);
        if (productRegistrySize == productRegistry.size()) {
            System.out.println("Product \"" + title +"\" already exist!");
        }
    }

    public void showAllProducts() {
        productRegistry.forEach((title, product) -> System.out.println(title + ": " + product));
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
        if (this.amount < 0) {
            this.amount = 0;
        }
    }

    private void validateTitleAndPrice(String title, double price) {
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new ProductException("Product title is too long! (more than 255 characters)");
        }
        if (price < 0) {
            throw new ProductException("Product price can't be negative!");
        }
    }

    private void validateAllCtorArgs(String title, double price, int amount) {
        validateTitleAndPrice(title, price);
        if (amount < 0) {
            throw new ProductException("Product amount can't be negative!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(title, product.title) && unit == product.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, unit);
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", unit=" + unit +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
