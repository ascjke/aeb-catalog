package ru.borisov.repository;

import ru.borisov.domain.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    public Map<String, Product> getProducts() {
        return products;
    }

    public void addProductToRepository(Product product) {
        Product _product = new Product(
                product.getTitle(),
                product.getUnit(),
                product.getPrice(),
                product.getAmount()
        );
        products.put(product.getTitle(), _product);
    }

    public void showAllProducts() {
        System.out.println("\nAll products: ");
        if (!products.isEmpty()) {
            int i = 1;
            for (Product product : products.values()) {
                System.out.println(i++ + ": " + product);
            }
        } else {
            System.out.println("There are no products yet\n");
        }
    }
}
